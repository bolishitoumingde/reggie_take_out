package com.example.take_out.controller;

import com.example.take_out.controller.utils.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * 文件上传下载
 */

@Slf4j
@RestController
@RequestMapping("/common")
@Api("文件上传下载接口")
public class CommonController {

    @Value("${upload.path}")
    private String path;

    /**
     * 文件上传
     *
     * @param file 文件对象
     * @return 上传信息
     */
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
        return R.success(fileName);
    }


    /**
     * 文件下载
     *
     * @param name     文件名
     * @param response resp对象
     * @return 文件信息
     */
    @GetMapping("/download")
    public R<String> download(@RequestParam("name") String name, HttpServletResponse response) {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path + name));
             ServletOutputStream os = response.getOutputStream()) {
            response.setContentType("image/jpeg");
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success("下载成功");
    }
}
