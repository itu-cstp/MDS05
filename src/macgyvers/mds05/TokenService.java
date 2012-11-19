package macgyvers.mds05;
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
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.naming.AuthenticationException;

import com.jcraft.jsch.*;

public class TokenService {

	static HashMap<String, String> roleMappings = new HashMap<String, String>();

	static TokenService instance;
	
	public static TokenService getInstance(){
		
		if (instance == null) instance = new TokenService();
		
		return instance;
	}
	
	private TokenService()
	{
		roleMappings.put("eeng", "student");
		roleMappings.put("lynd", "ta");
		roleMappings.put("mrof", "teacher");
		roleMappings.put("cstp", "student");
	}
	
	public static String getToken(String encryptedCredentials) throws Exception 
	{
		// decrypt with K-CT
		String credentials = EncryptionService.decrypt(encryptedCredentials);
		String[] inputs = credentials.split(" ");
		String user = inputs[0];
		String pword = inputs[1];
		
		String result = "";
		if(authenticate(user, pword))
		{			
			// return an encrypted server token containing the 
			// role corresponding to the clients username and 
			// a timestamp indicating the validity of the token.
			if(roleMappings.get(user) == null) 
			{
				System.out.println("You have not been assigned a role. Please write yourself into the hashtable in TaskTokenService's constructor.");
			}
			else {
				String role = roleMappings.get(user);
				long timestamp = System.currentTimeMillis();
				
				String token = role+" "+timestamp;
				try {
					// encrypt with K-TS
					String encryptedToken = EncryptionService.encrypt(token);
					// encrypt with K-CT
					result = EncryptionService.encrypt(encryptedToken);
				} catch (Exception e) {
					System.out.println("There was a problem with the encryption. Please try again.");
					e.printStackTrace();
				}
			}
		}
		else {
			System.out.println("You were not authenticated. The username or password was incorrect.");
		}
		return result;
	}
	
	private static boolean authenticate(String user, String pword)
	{/*
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
		}*/
		return true;
	}

}
