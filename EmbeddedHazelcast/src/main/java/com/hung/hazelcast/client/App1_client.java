package com.hung.hazelcast.client;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;

/**
 * step1: run 2 server Node. Thứ tự Start Server ko quan trọng => chúng tự động tạo cluster (=group).
 * step2: run 2 client. Thứ tự ko quan trọng. Client tự động tìm server trong giải config.
 */

/**
Cluster giống như Redis. Khác biệt đó là lưu trữ distributed storage. Redis dùng replicate Storage
Client có thể tạo Map, queue, pub/sub... và truy vấn dữ liệu bằng SQL
 */
@Slf4j
public class App1_client {
	
	public static void main(String[] args) throws IOException {
		log.debug("=============================== start {}", App1_client.class.getSimpleName());
		// Xây dựng cấu hình client bằng cách đọc từ file XML.
		ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();

		/**
		 * client sẽ tạo socket non-blocking tới tất cả các Server Node
		 */
		HazelcastInstance hzClient 	  = HazelcastClient.newHazelcastClient(clientConfig);
		
		// lấy thông tin từ Map tên là "data" đã tạo ở server Node
		Map<Long, String> map = hzClient.getMap("myMap");
		
	    for(Entry<Long, String> entry: map.entrySet() ) {
	        log.debug("{" + entry.getKey() + "," + entry.getValue() + "}" );
	    }
	    
	    //================================== create Map from client 
        // Map này đc khởi tạo trong hazelcast_server.xml ở ServerNode
        IMap<String, String> map1 = hzClient.getMap("Data");
        //Standard Put and Get.
        map1.put("key1", "value1");
        map1.put("key2", "value2");

		String key = "key1";
		log.debug("key = {}, value = {}", key, map1.get(key));
        
        //Concurrent Map methods, optimistic updating
        map1.putIfAbsent("somekey", "somevalue");
        map1.replace("key2", "value", "newvalue");
        
        
        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}
