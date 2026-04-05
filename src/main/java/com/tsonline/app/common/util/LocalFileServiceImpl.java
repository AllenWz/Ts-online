package com.tsonline.app.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileServiceImpl implements FileService{
	
	@Value("${project.image}")
	private String path;

	@Override
	public String uploadFileName(MultipartFile file) throws IOException {
		String originalFileName = file.getOriginalFilename();
		System.out.println("original file name -> " + originalFileName);
        String randomId = UUID.randomUUID().toString();
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        System.out.println("formatted file name ->" + fileName);
        String filePath = path + File.separator + fileName;
        System.out.println("file path ->" + filePath);
        
		File folder = new File(path);
		if(!folder.exists()) {
			folder.mkdir();
		}
		Files.copy(file.getInputStream(), Paths.get(filePath));
		return fileName;
	}

	@Override
	public String getPresignedUrl(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteFile(String fileName) {
		// TODO Auto-generated method stub
		
	}

}
