package com.mercury.platform.shared.config.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mercury.platform.shared.config.ConfigurationSource;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentWrapper;
import com.mercury.platform.shared.config.json.deserializer.AdrComponentDeserializer;
import com.mercury.platform.shared.config.json.serializer.AdrComponentSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JSONHelper {
    private Logger logger = LogManager.getLogger(JSONHelper.class.getSimpleName());
    private ConfigurationSource dataSource;

    public JSONHelper(ConfigurationSource dataSource){
        this.dataSource = dataSource;
    }
    public <T> List<T> readArrayData(TypeToken<List<T>> typeToken){
        try {
            Gson gson = new GsonBuilder().registerTypeAdapter(AdrComponentWrapper.class,new AdrComponentDeserializer()).create();
            JsonParser jsonParser = new JsonParser();
            try(JsonReader reader = new JsonReader(new FileReader(dataSource.getConfigurationFilePath()))) {
                return gson.fromJson(
                        jsonParser.parse(reader),
                        typeToken.getType());
            }
        }catch (IOException e){
            logger.error(e);
            return null;
        }catch (IllegalStateException e1) {
            return null;
        }
    }
    public <T> T readMapData(String key,TypeToken<T> typeToken){
        try {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            try(JsonReader reader = new JsonReader(new FileReader(dataSource.getConfigurationFilePath()))) {
                return gson.fromJson(
                        jsonParser.parse(reader)
                                .getAsJsonObject()
                                .get(key),
                        typeToken.getType());
            }
        }catch (IOException e){
            logger.error(e);
            return null;
        }catch (IllegalStateException e1) {
            return null;
        }
    }
    public void writeMapObject(String key, Map<?,?> object){
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

            try(JsonWriter writer = new JsonWriter(new FileWriter(dataSource.getConfigurationFilePath()))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add(key,gson.toJsonTree(object));
                gson.toJson(jsonObject,writer);
            }
        }catch (IOException e){
            logger.error(e);
        }

    }
    public <T> void writeListObject(List<?> object, TypeToken<List<T>> typeToken){
        try {
            Gson gson = new Gson();
            try(JsonWriter writer = new JsonWriter(new FileWriter(dataSource.getConfigurationFilePath()))) {
                gson.toJson(object,typeToken.getType(),writer);
            }
        }catch (IOException e){
            logger.error(e);
        }

    }
}
