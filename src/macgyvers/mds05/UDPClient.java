package macgyvers.mds05;

import java.io.*;
import java.net.*;
import java.util.*;

public class UDPClient 
{    
	public static void main(String[] args)
	{
		boolean running = true;

		System.out.println("Please enter your ITU username and password separated by one space: ");
		while(running)
		{
			try (DatagramSocket socket = new DatagramSocket()){
				// Part 1 : authenticate credentials with ITS's ssh service

				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String login = br.readLine();
				String[] inputs = login.split(" ");

				if (inputs.length != 2) {
					System.out.println("Please enter your ITU username and password separated by one space: ");
					//socket.close();
					/// return;
				}

				String credentials = inputs[0]+" "+inputs[1];
				String encryptedCredentials = EncryptionService.encrypt(credentials);
				TokenService tokenService = TokenService.getInstance();
				String token = tokenService.getToken(encryptedCredentials);
				
				if(!token.isEmpty()) {

					// Part 2: send request to server

					// decrypt token
					String tokenString = EncryptionService.decrypt(token);
					
					// get task
					System.out.println("Please enter the id of the task you wish to execute: ");
					String task = br.readLine();


					byte[] byteArr = (tokenString+" "+task).getBytes();
					// send request
					byte[] buf = new byte[256];
					InetAddress address = InetAddress.getByName("localhost");
					DatagramPacket packet = new DatagramPacket(byteArr, byteArr.length, address, 4445);
					socket.send(packet);

					// get response
					packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);

					// display response
					String received = new String(packet.getData(), 0, packet.getLength());
					System.out.println("From server: " + received);

				}
			} catch (SocketException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

