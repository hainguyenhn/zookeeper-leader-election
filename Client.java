import java.net.*;
import java.io.*;

public class Client
{
	private Socket client;

	public Client(String serverName, int port){
		try
		{	
			System.out.println("Connecting to " + serverName
					+ " on port " + port);
			this.client = new Socket(serverName, port);

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void run(){
		System.out.println("Just connected to "
				+ this.client.getRemoteSocketAddress());
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
	
	public static void main(String[] args){
		Client cl = new Client("localhost", 9090);
		cl.run();
	}
}