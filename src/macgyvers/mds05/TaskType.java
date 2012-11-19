package macgyvers.mds05;

import java.util.ArrayList;

// Enum class made to make it easier to keep track of dependencies.
/**
 * This class represents a type of task. Each
 * task has a list of conditions needed to be executed before
 * itself. 
 * @author Morten
 *
 */
enum TaskType{
	unknown, handin, review(new String[] {"handin"}), reject(new String[]{"handin", "review"}), approve(new String[]{"handin", "review"}), qualify(new String[]{"handin", "review", "approve"});
	
	public ArrayList<String> conditions = new ArrayList<String>();
	
	private TaskType(String[] conditions){
		for(int i=0; i<conditions.length; i++) 
			this.conditions.add(conditions[i]);
	}
	// overload
	private TaskType(){}
	
	
	/**
	 * this method returns the correct enum based on the name parsed from an id-string. 
	 * @param name
	 * @return
	 */
	public static TaskType getType(String name){
		switch(name){
		case "handin": return TaskType.handin;
		case "review": return TaskType.review;
		case "reject": return TaskType.reject;
		case "approve": return TaskType.approve;
		case "qualify": return TaskType.qualify;
		}
		return TaskType.unknown;
		
	}
}