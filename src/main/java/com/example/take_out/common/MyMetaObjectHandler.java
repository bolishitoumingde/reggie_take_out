package com.example.take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 * 公共字段自动填充
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert:" + metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        // metaObject.setValue("createUser", LocalDateTime.now());
        // metaObject.setValue("updateUser", LocalDateTime.now());

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update:" + metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        // metaObject.setValue("updateUser", LocalDateTime.now());
    }
}
