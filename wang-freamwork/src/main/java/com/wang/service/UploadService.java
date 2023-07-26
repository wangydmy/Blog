package com.wang.service;

import com.wang.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    ResponseResult uploadImg(MultipartFile img)throws IOException;
}
