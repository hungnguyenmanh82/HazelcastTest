package com.hung.hazelcast.client;

import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.impl.record.Record;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Data
public class MyEntryProcessor implements EntryProcessor<String, String, Boolean>, Serializable {
    @NonNull
    private final String oldValue;
    @NonNull
    private final String newValue;

    /**
     * atomic function thiết kế giống LUA script trên Redis
     * code function này chạy trên server
     * ---------
     * cách 1: add *.jar chứa class này vào classPath của cả client và ServerNode
     * cách 2: chỉ *.jar chứa class này cho client và enable tính năng upload *.jar từ client lên server
     *         phải config ở cả client và server mới đc
     * ---------
     * sau khi sửa code cần restart lại server để update code mới
     */
    @Override
    public Boolean process(Map.Entry<String, String> entry) {
        // --- ĐÂY LÀ PHẦN LOGIC CHẠY TRÊN SERVER ---

        if (entry.getValue() == null) {
            return false;
        }

        //Mô phỏng CAS
        if(entry.getValue().equals(oldValue)){
            entry.setValue(newValue);
            return true;
        }
        return false;
    }
}
