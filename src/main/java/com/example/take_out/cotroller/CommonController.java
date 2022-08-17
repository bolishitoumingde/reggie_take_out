package com.example.take_out.cotroller;

import com.example.take_out.cotroller.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件上传下载
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${upload.path}")
    private String path;

    @PostMapping("/upload")
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        log.info("文件上传");
        String originalFilename = file.getOriginalFilename();
        // 获取后缀
        assert originalFilename != null;
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID() + substring;
        try {
            file.transferTo(new File(path + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
