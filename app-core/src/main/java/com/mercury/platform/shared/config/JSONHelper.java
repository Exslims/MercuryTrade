package com.mercury.platform.shared.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mercury.platform.shared.entity.KeyData;
import com.mercury.platform.shared.entity.SoundDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.Observable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONHelper {
    private Logger logger = LogManager.getLogger(JSONHelper.class.getSimpleName());
    private DataSource dataSource;

    public JSONHelper(DataSource dataSource){
        this.dataSource = dataSource;
    }
    public <T> Observable<KeyData<List<T>>> readArrayKeyData(KeyData<List<T>> key, TypeToken<T> typeToken){
        return Observable.just(key)
                .map(item -> {
                    try {
                        Gson gson = new Gson();
                        try(JsonReader reader = new JsonReader(new FileReader(dataSource.getConfigurationFilePath()))) {
                            return gson.fromJson(reader,typeToken.getType());
                        }
                    }catch (IOException e){
                        logger.error(e);
                    }
                    return item;
                });
    }
    public void writeArrayObject(String key, List<?> object, TypeToken<?> typeToken){
        try {
            Gson gson = new Gson();
            try(JsonWriter reader = new JsonWriter(new FileWriter(dataSource.getConfigurationFilePath()))) {
                gson.toJson(new KeyData<>(key,object),typeToken.getType(),reader);
            }
        }catch (IOException e){
            logger.error(e);
        }

    }
}
