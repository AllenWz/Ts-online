package com.tsonline.app.common.util;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileUtil {
	String uploadFileName(MultipartFile file) throws IOException;
}
