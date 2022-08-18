package com.example.take_out.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.example.take_out.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 自定义元数据处理器
 * 公共字段自动填充
 */

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert:" + metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long id = (Long) (request.getSession().getAttribute("employee") != null ?
                request.getSession().getAttribute("employee") : request.getSession().getAttribute("user"));
        metaObject.setValue("createUser", id);
        metaObject.setValue("updateUser", id);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update:" + metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long id = (Long) (request.getSession().getAttribute("employee") != null ?
                request.getSession().getAttribute("employee") : request.getSession().getAttribute("user"));
        metaObject.setValue("updateUser", id);
    }
}
