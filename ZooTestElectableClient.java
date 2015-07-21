/**
 * ZooTestElectableClient
 *
 */
import java.io.IOException;

import org.apache.zookeeper.KeeperException;

// See http://zookeeper.apache.org/doc/trunk/recipes.html#sc_leaderElection
public class ZooTestElectableClient extends ZooElectableClient {
	
	boolean isFirstRun = true;
	boolean wasLeader = false;
	
	public ZooTestElectableClient() throws KeeperException, IOException, InterruptedException {
		super();
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
	
			sv = new Server();
	
		Client cl = new Client("ab",6000);
		if(leader){
			sv.run();
			System.out.println("leader");
		}
		else{
			System.out.println("worker");
		}
		
	}
	
	// Main entry point
    public static void main(String args[])
        throws KeeperException, IOException, InterruptedException {
        ZooElectableClient zooClient = new ZooTestElectableClient();
        zooClient.run();
        System.out.println( "ZooTestElectableClient::main:: client finished." );
    }
}