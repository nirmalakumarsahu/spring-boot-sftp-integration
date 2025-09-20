package com.sahu.springboot.basics.service;

import java.util.Map;

public interface FileReaderService {
    Map<String, String> readAllFiles();

    String readFileByName(String fileName);

}
