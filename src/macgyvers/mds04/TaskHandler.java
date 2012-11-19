package macgyvers.mds04;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.bind.JAXBException;

import macgyvers.mds04.xml.Cal;
import macgyvers.mds04.xml.CalSerializer;
import macgyvers.mds04.xml.Task;

/**
 * This class holds the queues with taskList, and nonexecuted Tasks.
 * New Tasks can be submitted and waiting jobs can be popped from the queues.
 * The tasks are taskList by the inner class taskexecuter (see below)
 * @author Morten
 *
 */
public class TaskHandler {
	

	public static TaskHandler instance = new TaskHandler();
	private Queue<String> notExecuted = new LinkedList<String>(); // Pending changes
	private HashMap<String, Task> taskList = new HashMap<String, Task>();
	private Cal cal;
	private CalSerializer ser;

	private TaskHandler(){
		//unserialize the calendar of events
		ser = new CalSerializer();
		try {
			cal = ser.deserialize();


			for(Task task : cal.tasks){
				//add tasks to the map of tasks
				 taskList.put(task.id, task);
			}
		} catch (FileNotFoundException | JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(cal.tasks.size() +" Tasks read from XML file.");
	}
	/**
	 * adds a task to the notExecuted queue
	 * @param
	 */
	public void submitTask(String taskid){
		notExecuted.offer(taskid);
	}
	
	public boolean roleMatches(String taskId, String role){
		Task task = taskList.get(taskId);		
		ArrayList<String> roles = task.roles; //roles is null
		for(String r : roles)
		{
			System.out.println(r);
		}
		if (task.roles.contains(role)) return true;
		else return false;
	}
	/**
	 * singleton method
	 * @return
	 */
	public static TaskHandler getInstance(){
		return instance;
	}
	/**
	 * Class that executes all jobs. This runs in a separate thread so it wont block the TaskHandler thread.
	 * This makes it possible to continuously add jobs via the submit jobs method while executing jobs in the loop. 
	 */
	public void startExecuteService(){
		Thread t = new TaskExecuter();
		t.setDaemon(true);
		t.start();
	}
	
	private synchronized void executeTask(Task task){
		task.status = "executed";
		
		taskList.put(task.id, task);
		//save state
		cal.tasks = taskList.values();
		try {
			ser.serialize(cal);
		} catch (JAXBException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	

	public class TaskExecuter extends Thread {
				
		public TaskExecuter(){
			super();	
		}
		
		@Override
		public void start(){
			
		
			while(true){
				//if there are jobs in the queue
				if(!notExecuted.isEmpty()){
					//pop the task from the queue
                    Task task = taskList.get(notExecuted.remove());
					//if it has conditions, check whether they're fullfilled.
                    //System.out.println(task.conditions+" "+task.conditions.size()+" "+task.conditions.get(0).equals(""));
                    
                    if(!task.conditions.isEmpty() && !task.conditions.get(0).equalsIgnoreCase("")){ // Hack
                    	String[] taskConditions = task.conditions.get(0).split(", "); // Our serialization tool can't handle strings, we fix it by manually splitting
                    	for(String condition : taskConditions){
							Task key = taskList.get(condition);
							System.out.println(condition+" "+key);
                            if(key == null || key.status.equals("not-executed")){
                                submitTask(task.id); //submit task in the back of the queue and pop another :-)
                                System.out.println("Postponing "+task.id);
								continue;
							}else{
								executeTask(task);
							}
								
						}
						//if there are no conditions before execution
					} else {
						executeTask(task);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
			}
			
		}
	}
}