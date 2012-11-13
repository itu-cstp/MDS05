/**
 * It authenticates clients’s credentials (ITU username and password)  
 * by contacting and log into one of the ITU service  such as LDAP 
 * directory service or Secure Shell (ssh). 
 * Furthermore, the token service maintains authorization of tasks 
 * by having role mappings for a given ITU user account that matches 
 * to the roles assigned to the tasks and if the client credentials 
 * are validated correctly, then token service provides the client 
 * a server token containing the role corresponding to the clients 
 * username and a timestamp indicating the validity of the token.
 * @author Ellen
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.AuthenticationException;

import com.jcraft.jsch.*;

public class TaskTokenService {

	static HashMap<String, String> roleMappings = new HashMap<String, String>();

	public TaskTokenService()
	{
		roleMappings.put("eeng", "student");
		roleMappings.put("lynd", "student");
		roleMappings.put("mrof", "student");
		roleMappings.put("cstp", "student");
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{		
	
		boolean running = true;
		System.out.println("Please enter your ITU username and password separated by one space: ");
		while(running){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			String line = br.readLine();
			String[] inputs = line.split(" ");

			String user = inputs[0];
			String pword = inputs[1];

			System.out.println(getToken(user, pword));
			running = false;
		}

	}

	public static String getToken(String user, String pword) 
	{
		String result = "";
		if(authenticate(user, pword))
		{			
			// return an encrypted server token containing the 
			// role corresponding to the clients username and 
			// a timestamp indicating the validity of the token.
			if(roleMappings.get(user) != null) 
			{
				result = "You have not been assigned a role. Please write yourself into the hashtable in TaskTokenService's constructor.";
			}
			else {
				String role = roleMappings.get(user);
				long timestamp = System.currentTimeMillis();
				
				String token = "Role: " + role + " time " + timestamp;
				try {
					result = EncryptionService.encrypt(token);
				} catch (Exception e) {
					result = "There was a problem with the encryption.";
					e.printStackTrace();
				}
			}
		}
		else {
			result = "You were not authenticated. The username or password was incorrect.";
		}
		return result;
	}

	private static boolean authenticate(String user, String pword)
	{
		try {
			JSch j = new JSch();
			String host = "ssh.itu.dk";			
			Session session=j.getSession(user, host, 22);
			session.setPassword(pword);	
			session.setConfig("StrictHostKeyChecking", "no");		
			session.connect(3000);			
		}
		catch(JSchException je){
			return false;
		}
		return true;
	}

}
