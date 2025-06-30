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
public class App4_client_EntryProcessor_version_fail {

	public static void main(String[] args) throws IOException {
		log.debug("=============================== start {}", App4_client_EntryProcessor_version_fail.class.getSimpleName());
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
		EntryView<String, String> entry =  myMap.getEntryView("key1_version");
		log.debug("entryView = {}", entry);
		log.debug("entry version = {}", entry.getVersion());

		boolean isOk = myMap.executeOnKey("key1_version",new VersionEntryProcessor(entry.getVersion(),"new_value"));

		log.debug("==================== isOk = {}", isOk);

		entry =  myMap.getEntryView("key1_version");
		log.debug("entryView = {}", entry);
		log.debug("entry version = {}", entry.getVersion());

        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}
