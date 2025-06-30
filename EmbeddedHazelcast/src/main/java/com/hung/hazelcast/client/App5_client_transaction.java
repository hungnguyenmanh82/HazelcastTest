package com.hung.hazelcast.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.core.EntryView;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionalMap;
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
public class App5_client_transaction {

	public static void main(String[] args) throws IOException {
		log.debug("=============================== start {}", App5_client_transaction.class.getSimpleName());
		// Xây dựng cấu hình client bằng cách đọc từ file XML.
		ClientConfig clientConfig = new XmlClientConfigBuilder("hazelcast-client.xml").build();

		/**
		 * client sẽ tạo socket non-blocking tới tất cả các Server Node
		 */
		HazelcastInstance hzClient 	  = HazelcastClient.newHazelcastClient(clientConfig);

		/**
		 * với IMap các command write (put, remove) từ client sẽ ko gán transactionId
		 * chỉ khi ta tạo transaction thì mới có
		 * -----
		 * transaction dùng  để rollback
		 * ----
		 * transactionId này là UUID tạo ở client (ko phải ở server như DB or Zookeeper)
		 */
		TransactionOptions options = new TransactionOptions()
				.setTransactionType(TransactionOptions.TransactionType.TWO_PHASE);

		TransactionContext context = hzClient.newTransactionContext(options);
		context.beginTransaction();
		try {
			TransactionalMap<String, String> transactionMap = context.getMap("transactionMap");
			transactionMap.put("key", "value");

//			if (someConditionFails) {
//				context.rollbackTransaction(); //  rollback thủ công
//				return;
//			}

			context.commitTransaction();
		}catch (Exception e) {
			context.rollbackTransaction(); // 👈 rollback nếu có exception
		}


        // Shutdown this Hazelcast client
//        hz.shutdown();
	}

}
