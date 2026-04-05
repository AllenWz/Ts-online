package com.tsonline.app.common.util;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	String uploadFileName(MultipartFile file) throws IOException;
	
	String getPresignedUrl(String fileName);
	
	void deleteFile(String fileName);
}
