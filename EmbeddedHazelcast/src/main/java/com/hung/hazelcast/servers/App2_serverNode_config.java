package com.hung.hazelcast.servers;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * step1: run 2 server Node. Thứ tự Start Server ko quan trọng => chúng tự động tạo cluster (=group).
 * step2: run 2 client. Thứ tự ko quan trọng. Client tự động tìm server trong giải config.
 * -------
 * có 2 cách config cả đối với Server và client:
 *   cách 1: qua file hazelcast.xml
 *   cách 2: config trong code (ko dùng file hazelcast_server.xml)
 * -------
 * server = member
 */

/**
By default, Hazelcast will try 100 ports to bind. start at port 5701.
 Hazelcast tries to find ports between 5701 and 5721.
 */
@Slf4j
public class App2_serverNode_config {

	public static void main(String[] args) throws FileNotFoundException {

		log.debug("step1: run {}", App2_serverNode_config.class.getSimpleName());

        /**
         * java -Dhazelcast.config=/path/to/hazelcast.xml -jar hazelcast-X.Y.Z.jar
		 * -----
		 */

		Config config = new XmlConfigBuilder(App1_serverNode1.class.getClassLoader().getResourceAsStream("hazelcast_server.xml")).build();
		HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(config);
		
		// crate map at Server Node
		Map<Long, String> map = hzInstance.getMap("myMap");

		/**
		 * tạo Id ko chùng lặp giữa các Node
		 */
		FlakeIdGenerator idGenerator = hzInstance.getFlakeIdGenerator("newid");
		
		for (int i = 0; i < 10; i++) {
		    map.put(idGenerator.newId(), "message" + i);
		}
	}

}
