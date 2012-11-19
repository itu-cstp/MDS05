/**
 * 
 */
package macgyvers.mds04.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.*;
/**
 * @author Yndal
 *
 */
@XmlRootElement(name = "task")
public class Task implements Serializable {
	
	@XmlAttribute
	public String id;
	
	// not saved to xml
	public String idNumber;
	
	@XmlAttribute
	public String name;
	
	@XmlAttribute
	public String date;
	
	@XmlAttribute
	public String status;
	
	@XmlAttribute
	public Boolean required;
	
	@XmlElementWrapper(name = "attendants")
	@XmlElement(name = "user")
	public ArrayList<String> attendants;
	
	@XmlElement(name = "description")
	public String description;
	
	@XmlElement(name = "conditions")
	public ArrayList<String> conditions = new ArrayList<>();
	
	@XmlElement(name = "responses")
	public ArrayList<String> responses;	
	
	@XmlElement(name = "roles")
	public ArrayList<String> roles;	

    public Task(){}
    //this constructor is for creating new entries
    public Task(String id, String name, String date, String status){
        
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        
        this.attendants = new ArrayList<String>();
        this.conditions = new ArrayList<String>();
        this.responses = new ArrayList<String>();
        this.roles = new ArrayList<String>();
    }
    //overload this constructor is for changing state of existing entries
    public Task(String id, Date date, String idNumber){
    	this.date = date.toString();
    	this.id = id;
    	this.idNumber = idNumber;
    }
    
    @Override
    public String toString(){

        return "Job:	Id:"+id+ " Date: " +date+" Status: "+status+" conditions: "+conditions;

    }

}
