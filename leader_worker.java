import java.net.*;
import java.io.*;

public class leader_worker extends 	Thread
{
	private ServerSocket serverSocket;
	Socket server;
	private Socket client;
	boolean leader = false;
	String name;
	final int port = 9090;

	public leader_worker(boolean leader, String name) 
	{
		this.name = name;
		this.leader = leader;
		System.out.print(this.leader);
		if (!this.leader){
			try {
				serverSocket = new ServerSocket(this.port);
				serverSocket = new ServerSocket(9090, 0, InetAddress.getByName("localhost"));
				serverSocket.setSoTimeout(99999);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else{
			try
			{
				System.out.println("Connecting to " + "server"
						+ " on port " + this.port);
				this.client = new Socket("localhost", this.port);

			}catch(IOException e)
			{
				e.printStackTrace();
			}
		
		}
		
	}

	public void run(){
		if(this.leader){
			leader_run();
		}
		else{
			worker_run();
		}
	}

	public void worker_run()
	{
		while(true)
		{
			try
			{
				System.out.println("Worker: Waiting for leader's order: " +
						serverSocket.getLocalPort() + "...");
				server = serverSocket.accept();
				System.out.println("Worker: Just connected to "
						+ server.getRemoteSocketAddress());
				DataInputStream in =
						new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out =
						new DataOutputStream(server.getOutputStream());
				out.writeUTF("Worker: Thank you for connecting to "
						+ server.getLocalSocketAddress() + "\nGoodbye!");
				server.close();
			}catch(SocketTimeoutException s)
			{
				System.out.println("Socket timed out!");
				break;
			}catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}

	}

	public void leader_run(){
		System.out.println("Leader: Just connected to worker:  "
				+ this.client.getRemoteSocketAddress());
		OutputStream outToServer;
		try {
			outToServer = client.getOutputStream();

			DataOutputStream out =
					new DataOutputStream(outToServer);

			out.writeUTF("Leader: Hello from "
					+ client.getLocalSocketAddress());
			InputStream inFromServer = client.getInputStream();
			DataInputStream in =
					new DataInputStream(inFromServer);
			System.out.println("Leader: Worker replied " + in.readUTF());
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public boolean got_promoted(boolean promoted){
		/*
		 * Switch role
		 */
		boolean success = false;
		if (promoted && !this.leader){
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				success = false;
			}	
			leader_run();
		}
		else{
			success = false;
		}
		return success;
	}

	public static void main(String[] args){
		leader_worker worker = new leader_worker(false, "hai");
		leader_worker leader = new leader_worker(true, "hai");
		Thread t1 = new Thread(worker);
		Thread t2 = new Thread(leader);
		t1.start();
		t2.start();
		
		
	}
}