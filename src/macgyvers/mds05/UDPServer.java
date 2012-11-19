package macgyvers.mds05;

import java.io.*;
import java.net.*;
import java.util.*;

import macgyvers.mds05.xml.Task;
/**
 * This class listens to requests from a socket via UDP.
 * If a request is received in the correct syntax, a Task is
 * created and placed in the queue.
 * @author MacGyvers
 *
 */
public class UDPServer extends Thread {

	protected DatagramSocket socket = null;
	protected BufferedReader in = null;
	protected boolean looping = true;
	private TaskHandler handler = TaskHandler.getInstance();

	//constructor calls the constructor in the ancestor class. 
	public UDPServer() throws IOException {
		this("ServerThread");
	}
	// overload constructor with custom name
	public UDPServer(String name) throws IOException {
		super(name);
		socket = new DatagramSocket(4445);
	}
	//inherited method from thread
	public void run() {
		while (looping) {
			try {
				byte[] buf = new byte[256];

				String returnMessage = "";
				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				// if no data is received
				if (packet.getData() == null)
					returnMessage = "Usage: token task-id";
				// new string created from input
				String input = new String(packet.getData(), 0, packet.getLength());
				// splitting the input string to isolate commands
				String[] inputs = input.split(" ");

				//if all information is not entered from the client
				if(inputs.length < 2)
					returnMessage = "Usage: token task-id";
				else {
					// decrypt the token & split into component parts
					String token = inputs[0];
					String tokenString = EncryptionService.decrypt(token);
					String[] splitToken = tokenString.split(" ");
					String role = splitToken[0];
					long timestamp = Long.valueOf(splitToken[1]).longValue();

					// validate the timestamp
					long currentTime = System.currentTimeMillis();
					// if more than 15 minutes have passed since the token was created
					if (currentTime-timestamp > 900000) returnMessage = "Sorry, your login session has expired, please log in again.";
					else {		
						// get the task
						String taskId = inputs[1]; 

						// check that role matches associated task
						if(!handler.roleMatches(taskId, role)) returnMessage = "Sorry, you do not have permission to execute that task. Please contact your role administrator.";

						//if there are no errors so far (returnMessage not altered)
						if(returnMessage == "") 
						{
							// submit task
							handler.submitTask(taskId);
							returnMessage = "Task received, Id: "+taskId;
						}
					}
				}
				buf = returnMessage.getBytes();

				// send the response to the client at "address" and "port"
				InetAddress address = packet.getAddress();
				int port = packet.getPort();
				packet = new DatagramPacket(buf, buf.length, address, port);
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}
}
