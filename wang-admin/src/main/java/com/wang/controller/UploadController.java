package com.wang.controller;

import com.wang.domain.ResponseResult;
import com.wang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;

    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img){
        try {
              return  uploadService.uploadImg(img);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件上传失败");
        }
    }
}
