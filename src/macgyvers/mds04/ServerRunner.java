package macgyvers.mds04;

import java.io.*;
 //a class to hold at run the server listening to the socket rdy to communicate via UDP.
public class ServerRunner {
    public static void main(String[] args) throws IOException {
    	new UDPServer().start();
    	System.out.println("Server Started.");
    	//this starts the thread that executes jobs that are in the queue.
    	TaskHandler.getInstance().startExecuteService();
    	System.out.println("Execute service started");
        
    }
}

