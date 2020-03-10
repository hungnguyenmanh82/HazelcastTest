package hung.com.hazelcast.servers;

import java.util.Map;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.flakeidgen.FlakeIdGenerator;

/**
By default, Hazelcast will try 100 ports to bind. start at port 5701.
 Hazelcast tries to find ports between 5701 and 5721.
 */
public class App1_serverNode1 {
	
	public static void main(String[] args){
		
		
		HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
		
		// crate map at Server Node
		Map<Long, String> map = hzInstance.getMap("data");
		FlakeIdGenerator idGenerator = hzInstance.getFlakeIdGenerator("newid");
		
		for (int i = 0; i < 10; i++) {
		    map.put(idGenerator.newId(), "message" + i);
		}
	}

}
