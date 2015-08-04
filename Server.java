import java.net.*;
import java.io.*;

public class Server extends Thread
{
	private ServerSocket serverSocket;

	public Server() 
	{
		int port = 6000;
		try {
			serverSocket = new ServerSocket(port);
			serverSocket = new ServerSocket(9090, 0, InetAddress.getByName("localhost"));
			serverSocket.setSoTimeout(99999999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port " +
						serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("Just connected to "
						+ server.getRemoteSocketAddress());
				DataInputStream in =
						new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				DataOutputStream out =
						new DataOutputStream(server.getOutputStream());
				out.writeUTF("Thank you for connecting to "
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
	
	public static void main(String[] args){
		Server sv = new Server();
		sv.run();
	}
}