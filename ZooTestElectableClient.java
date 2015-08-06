/**
 * ZooTestElectableClient
 *
 */
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.KeeperException;

// See http://zookeeper.apache.org/doc/trunk/recipes.html#sc_leaderElection
public class ZooTestElectableClient extends ZooElectableClient {
	
	String name = "";
	boolean isFirstRun = true;
	boolean wasLeader = false;
	leader_worker worker; 
	Thread t;
	
	public ZooTestElectableClient(String name) throws KeeperException, IOException, InterruptedException {
		super(name);
		this.name = name;
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
			
			/*
			 * wait for worker to join
			 */
			int num_workers = get_woker_ip().size();
			while(num_workers < 2){
				System.out.println("Leader " + this.name + " is waiting for worker to join");
				num_workers = get_woker_ip().size();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			worker = new leader_worker(true, this.name);
			t = new Thread(worker);
			t.setDaemon(true);
			t.start();
			
			System.out.println("leader");
			List<String> ip = get_woker_ip();
			
		}
		else{
			if(worker != null){
				worker.interrupt();
			}
			worker = new leader_worker(false, this.name);
			t = new Thread(worker);
			t.setDaemon(true);
			t.start();
			System.out.println("worker");
			List<String> ip = get_woker_ip();
		}	
	}

	public static String get_name(){
		Random ran = new Random();
		int top = 3;
		char data = ' ';
		String dat = "";

		for (int i=0; i<=top; i++) {
		  data = (char)(ran.nextInt(25)+97);
		  dat = data + dat;
		}

		return dat;
	}
	
	// Main entry point
    public static void main(String args[])
        throws KeeperException, IOException, InterruptedException {
		
    	String name = get_name();
        ZooElectableClient zooClient = new ZooTestElectableClient(name);
        zooClient.run();
        System.out.println( "ZooTestElectableClient::main:: client finished." );
        
    }
}