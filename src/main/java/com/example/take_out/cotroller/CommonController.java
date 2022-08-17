package com.example.take_out.cotroller;

import com.example.take_out.cotroller.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传下载
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info("文件上传");
        return null;
    }
}
