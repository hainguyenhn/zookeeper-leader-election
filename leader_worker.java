import java.net.*;
import java.util.ArrayList;
import java.util.Random;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import java.io.*;

public class leader_worker extends Thread
{
	private ServerSocket serverSocket;
	Socket server;
	private Socket client;
	boolean leader = false;
	String name;
	final int port = 9090;
	ArrayList<String> workers;

	public leader_worker(boolean leader, String name) 
	{
		this.name = name;
		this.leader = leader;
		this.workers = new ArrayList<String>();
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
			leader_run(this.name);
		}
		else{
			worker_run(this.name);
		}
	}

	public void worker_run(String name)
	{
		while(!Thread.interrupted())
		{
			try
			{
				System.out.println(name + " : Waiting for leader's order: " +
						serverSocket.getLocalPort() + "...");
				server = serverSocket.accept();
				System.out.println(name + " : Just connected to "
						+ server.getRemoteSocketAddress());
				DataInputStream in =
						new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out =
						new DataOutputStream(server.getOutputStream());
				out.writeUTF(name + " : Thank you for connecting to "
						+ server.getLocalSocketAddress() + "\nGoodbye!");

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
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void leader_run(String name){
		System.out.println(name + " : Just connected to worker:  "
				+ this.client.getRemoteSocketAddress());
		OutputStream outToServer;
		while(true){
			try {
				outToServer = client.getOutputStream();

				DataOutputStream out =
						new DataOutputStream(outToServer);

				out.writeUTF(" Hello from "
						+ name);
				InputStream inFromServer = client.getInputStream();
				DataInputStream in =
						new DataInputStream(inFromServer);
				System.out.println(name + " : Worker replied " + in.readUTF());
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				this.interrupt();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				success = false;
			}	
		}
		else{
			success = false;
		}
		return success;
	}

	public void update_workers(ArrayList<String> worker_list){
		/*
		 * This method updates list of workers  
		 */
		this.workers = worker_list;
	}
	
	/* May not need to use for now
	class worker{
		String name;
		String ip;
		String port;

		public worker(String name, String ip, String port){
			this.name = name;
			this.ip = ip;
			this.port = port;
		}
		
		public String getname(){
			return this.name;
		}
		
		public String getip(){
			return this.name;
		}
		
		public String getport(){
			return this.name;
		}
	}
	*/
	
	public static void main(String[] args){
		leader_worker worker = new leader_worker(false, "hai");
		leader_worker leader = new leader_worker(true, "hai");
		Thread t1 = new Thread(worker);
		Thread t2 = new Thread(leader);
		t1.start();
		t2.start();


	}
}