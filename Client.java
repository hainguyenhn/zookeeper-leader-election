import java.net.*;
import java.io.*;

public class Client
{
	Socket client = null;
	public Client(String serverName, int port){
	      try
	      {
	         System.out.println("Connecting to " + serverName
	                             + " on port " + port);
	          client = new Socket(serverName, port);
	      
    }catch(IOException e)
    {
       e.printStackTrace();
    }
	}
	      public void run(){
	         System.out.println("Just connected to "
	                      + client.getRemoteSocketAddress());
	         OutputStream outToServer;
			try {
				outToServer = client.getOutputStream();
			
	         DataOutputStream out =
	                       new DataOutputStream(outToServer);

	         out.writeUTF("Hello from "
	                      + client.getLocalSocketAddress());
	         InputStream inFromServer = client.getInputStream();
	         DataInputStream in =
	                        new DataInputStream(inFromServer);
	         System.out.println("Server says " + in.readUTF());
	         client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
  
   
}