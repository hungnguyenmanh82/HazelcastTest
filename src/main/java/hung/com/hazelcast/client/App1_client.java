package hung.com.hazelcast.client;

import java.util.Map;
import java.util.Map.Entry;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class App1_client {
	
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
		
		// lấy thông tin từ Map tên là "data" đã tạo ở server Node
		Map<Long, String> map = hzClient.getMap("data");
		
	    for(Entry<Long, String> entry: map.entrySet() ) {
	        System.out.println("{" + entry.getKey() + "," + entry.getValue() + "}" );
	    }
	    
	    //================================== create Map from client 
        // Get the Distributed Map from Cluster.
        IMap<String, String> map1 = hzClient.getMap("my-distributed-map");
        //Standard Put and Get.
        map1.put("key", "value");
        map1.get("key");
        //Concurrent Map methods, optimistic updating
        map1.putIfAbsent("somekey", "somevalue");
        map1.replace("key", "value", "newvalue");
        
        
        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}