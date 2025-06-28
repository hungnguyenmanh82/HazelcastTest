package com.hung.hazelcast.client;

import com.hazelcast.map.EntryProcessor;
import com.hazelcast.map.impl.record.Record;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

@Slf4j
@Data
public class VersionEntryProcessor implements EntryProcessor<String, String, Boolean>, Serializable {
    @NonNull
    private final long expectedVersion;
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

        try {
            /**
             * phần implement entry có hàm getVersion() thật
             * Nhưng ta ko biết tên class nên Dùng Reflection để gọi phương thức getVersion() một cách an toàn
             * -----
             * hazelcast version mới đã thay đổi ko cho phép lấy version hiện tại
             *  {@link com.hazelcast.map.impl.LockAwareLazyMapEntry}
             */
            Method getVersionMethod = entry.getClass().getMethod("getversion");
            Record record = (Record) getVersionMethod.invoke(entry);

            //log lại để đối soát sau này
            log.debug("currentVersion = {}", record.getVersion());

            // so sánh version
            if (record.getVersion() == this.expectedVersion) {
                entry.setValue(this.newValue);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            // Xử lý lỗi nếu có bất kỳ vấn đề gì với Reflection
            // (gần như không bao giờ xảy ra trong môi trường Hazelcast)
            throw new RuntimeException("Không thể lấy version từ entry bằng reflection", e);
        }
    }
}
