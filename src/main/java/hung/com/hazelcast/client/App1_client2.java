package hung.com.hazelcast.client;

import java.util.Map;
import java.util.Map.Entry;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;


/**
 * step1: run 2 server Node. Thứ tự Start Server ko quan trọng => chúng tự động tạo cluster (=group).
 * step2: run 2 client. Thứ tự ko quan trọng. Client tự động tìm server trong giải config.
 */

/**
Cluster giống như Redis. Khác biệt đó là lưu trữ distributed storage. Redis dùng replicate Storage
Client có thể tạo Map, queue, pub/sub... và truy vấn dữ liệu bằng SQL
 */
public class App1_client2 {
	
	public static void main(String[] args){
		ClientConfig clientConfig = new ClientConfig();
		
		//========== config to connect to Server Node: xem file hazelcast.xml
		// hazelcast.xml: dùng cho server Node config
		/**
		     <group>
        		<name>MyCluster</name>
        		<password>my-password</password>
    		</group>
		 */
		GroupConfig groupConfig = clientConfig.getGroupConfig();
		groupConfig.setName("MyCluster");           // phải có GroupName thì client mới truy suat đc Cluster Nodes.
//		groupConfig.setPassword("my-password123");  //password chỉ dùng cho bản thương mại
				
		clientConfig.getNetworkConfig().addAddress("127.0.0.1");
		
		/**
		 * client sẽ tạo socket non-blocking tới tất cả các Server Node
		 */
		HazelcastInstance hzClient 	  = HazelcastClient.newHazelcastClient(clientConfig);
		
	    
	    //================================== create Map from client 
        // Get the Distributed Map from Cluster.
        IMap<String, String> map1 = hzClient.getMap("my-distributed-map");
        //Standard Put and Get.
		
	    for(Entry<String, String> entryClient: map1.entrySet() ) {
	        System.out.println("{" + entryClient.getKey() + "," + entryClient.getValue() + "}" );
	    }
	   
        
        
        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}
