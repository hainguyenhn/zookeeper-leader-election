/**
 * ZooTestElectableClient
 *
 */
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.apache.zookeeper.KeeperException;

// See http://zookeeper.apache.org/doc/trunk/recipes.html#sc_leaderElection
public class ZooTestElectableClient extends ZooElectableClient {
	
	boolean isFirstRun = true;
	boolean wasLeader = false;
	leader_worker worker; 
	
	public ZooTestElectableClient(String ip) throws KeeperException, IOException, InterruptedException {
		super(ip);
	}
	
	// Override this function to determine what work should be done
	void performRole() {
		/*
		if ( isFirstRun || ( wasLeader != getCachedIsLeader() ) ) {
			System.out.println( "ZooTestElectableClient::performRole:: work performed (" + getElectionGUIDZNodePath() + ") with state (leader=" + getCachedIsLeader() 
					+ ", isFirstRun=" + isFirstRun + ", wasLeader=" + wasLeader + ")" );			
		}
		else {
			System.out.println( "ZooTestElectableClient::performRole:: work  was not performed (" + getElectionGUIDZNodePath() + ")" );
		}
	*/
		boolean leader;
		leader = getCachedIsLeader();
		Server sv = null;

		if(leader){
			if(worker != null){
				worker.interrupt();
	
			}
			
			worker = new leader_worker(true, "hai");
			Thread t = new Thread(worker);
			t.start();
			
			System.out.println("leader");
			List<String> ip = get_woker_ip();
			System.out.println(ip);
			
			
		}
		else{
			if(worker != null){
				worker.interrupt();
	
			}
			worker = new leader_worker(false, "hai");
			Thread t = new Thread(worker);
			t.start();
			System.out.println("worker");
			List<String> ip = get_woker_ip();
			System.out.println(ip);
		}
	
		
	}
	
	// Main entry point
    public static void main(String args[])
        throws KeeperException, IOException, InterruptedException {
    	InetAddress IP=InetAddress.getLocalHost();
    	System.out.println("IP of my system is := "+IP.getHostAddress());
		
    	String ip = IP.getHostAddress();
    
	
        ZooElectableClient zooClient = new ZooTestElectableClient(ip);
        zooClient.run();
        System.out.println( "ZooTestElectableClient::main:: client finished." );
        
    }
}