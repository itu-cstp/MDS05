package macgyvers.mds05.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author rao
 */
@XmlRootElement(name = "cal")
@XmlSeeAlso(Task.class)
public class TaskList implements Serializable{
	
	@XmlElement(name="tasks")
    protected List<Task> tasks = new ArrayList<Task> ();
    
    public TaskList(){}

    public TaskList(List<Task> list) {
        this.tasks = list;
    }

    @XmlElement(name = "task")
    public List<Task> getList() {
        return tasks;
    }
}
