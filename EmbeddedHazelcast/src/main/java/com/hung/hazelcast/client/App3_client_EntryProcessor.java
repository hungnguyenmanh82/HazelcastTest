package com.hung.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * step1: run 2 server Node. Thứ tự Start Server ko quan trọng => chúng tự động tạo cluster (=group).
 * step2: run 2 client. Thứ tự ko quan trọng. Client tự động tìm server trong giải config.
 */

/**
 + run and restart app để xem version thay đổi thế nào
 */
@Slf4j
public class App3_client_EntryProcessor {

	public static void main(String[] args) throws IOException {
		log.debug("=============================== start {}", App3_client_EntryProcessor.class.getSimpleName());
		// Xây dựng cấu hình client bằng cách đọc từ file XML.
		ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();

		/**
		 * client sẽ tạo socket non-blocking tới tất cả các Server Node
		 */
		HazelcastInstance hzClient 	  = HazelcastClient.newHazelcastClient(clientConfig);

		//================================== create Map from client
		// Map này đc khởi tạo trong hazelcast_server.xml ở ServerNode
		IMap<String, String> myMap = hzClient.getMap("myMap");

		myMap.put("key1_version", "value_test_version");
		/**
		 * STEP 1:
		 * lấy thong tin {key,value, MetaData}
		 * metaData gồm có version, createTime, modifiedTime, ...
		 */
		String oldValue =  myMap.get("key1_version");
		log.debug("oldValue = {}", oldValue);

		/**
		 * STEP 2*: đoạn code MyEntryProcessor sẽ chạy trên server
		 */
		boolean isOk = myMap.executeOnKey("key1_version",new MyEntryProcessor(oldValue,"new_value"));

		// xem Server trả về thành công hay thất bại
		log.debug("==================== isOk = {}", isOk);

		/**
		 * lấy giá trị về để kiểm tra
		 */
		String newValue =  myMap.get("key1_version");
		log.debug("newValue = {}", newValue);

        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}
