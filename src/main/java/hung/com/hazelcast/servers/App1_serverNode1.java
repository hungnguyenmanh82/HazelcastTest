package hung.com.hazelcast.servers;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;

/**
 * step1: run 2 server Node. Thứ tự Start Server ko quan trọng => chúng tự động tạo cluster (=group).
 * step2: run 2 client. Thứ tự ko quan trọng. Client tự động tìm server trong giải config.
 */
/**
By default, Hazelcast will try 100 ports to bind. start at port 5701.
 Hazelcast tries to find ports between 5701 and 5721.
 */
public class App1_serverNode1 {
	
	public static void main(String[] args){
		
		System.out.println("step1: run App1_serverNode1 to create Node1 of Hazelcast cluster");
		System.out.println("step2: run App1_serverNode2 to create Node1 of Hazelcast cluster");
		System.out.println("step3: run App1_client to CRUD to Hazelcast cluster");
		System.out.println("step4: run App1_client2 to CRUD to Hazelcast cluster");
		
		
		HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
		
		// crate map at Server Node
		Map<Long, String> map = hzInstance.getMap("data");
		FlakeIdGenerator idGenerator = hzInstance.getFlakeIdGenerator("newid");
		
		for (int i = 0; i < 10; i++) {
		    map.put(idGenerator.newId(), "message" + i);
		}
	}

}
