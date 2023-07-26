package com.wang.controller;

import com.wang.domain.ResponseResult;
import com.wang.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    public ResponseResult uploadImg(MultipartFile img) throws Exception{
        return  uploadService.uploadImg(img);
    }

}
