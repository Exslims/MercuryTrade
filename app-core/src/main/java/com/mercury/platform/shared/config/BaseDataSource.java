package com.mercury.platform.shared.config;

public abstract class BaseDataSource {
    protected String configurationFilePath;
    protected BaseDataSource(String filePath){
        this.configurationFilePath = filePath;
    }
}
