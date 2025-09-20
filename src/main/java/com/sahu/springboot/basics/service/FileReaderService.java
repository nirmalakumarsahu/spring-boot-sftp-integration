package com.sahu.springboot.basics.service;

import java.util.List;
import java.util.Map;

public interface FileReaderService {
    Map<String, List<String>> getAllFileNames();

    Map<String, String> readAllFileContents(String sftpConfigName);

    String readFileByName(String sftpConfigName, String fileName);
}
