package com.mercury.platform.shared.config.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;
import com.mercury.platform.shared.config.json.deserializer.AdrComponentJsonAdapter;
import com.mercury.platform.shared.config.json.deserializer.AdrTrackerGroupDeserializer;
import com.mercury.platform.shared.entity.message.MercuryError;
import com.mercury.platform.shared.store.MercuryStoreCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class JSONHelper {
    private Logger logger = LogManager.getLogger(JSONHelper.class.getSimpleName());
    private String dataSource;

    public JSONHelper(String dataSource) {
        this.dataSource = dataSource;
    }

    public JSONHelper() {
        this.dataSource = dataSource;
    }

    public static List<AdrComponentDescriptor> getJsonAsObject(String jsonStr) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            JsonParser jsonParser = new JsonParser();
            return gson.fromJson(
                    jsonParser.parse(jsonStr),
                    new TypeToken<List<AdrComponentDescriptor>>() {
                    }.getType());
        } catch (IllegalStateException | JsonSyntaxException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing string: " + jsonStr, e));
            return null;
        }
    }

    public <T> List<T> readArrayData(TypeToken<List<T>> typeToken) {
        try {

            Gson gson = new GsonBuilder()
                    .registerTypeHierarchyAdapter(AdrTrackerGroupDescriptor.class, new AdrTrackerGroupDeserializer())
                    .registerTypeHierarchyAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();

            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new FileReader(dataSource))) {
                return gson.fromJson(
                        jsonParser.parse(reader),
                        typeToken.getType());
            }
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
            logger.error(e);
            return null;
        }
    }

    public <T> T readMapData(String key, TypeToken<T> typeToken) {
        try {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new FileReader(dataSource))) {
                return gson.fromJson(
                        jsonParser.parse(reader)
                                .getAsJsonObject()
                                .get(key),
                        typeToken.getType());
            }
        } catch (IOException | IllegalStateException e) {
            logger.error(e);
            return null;
        }
    }

    public void writeMapObject(String key, Map<?, ?> object) {
        try {
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();

            try (JsonWriter writer = new JsonWriter(new FileWriter(dataSource))) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.add(key, gson.toJsonTree(object));
                gson.toJson(jsonObject, writer);
            }
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public <T> void writeListObject(List<?> object, TypeToken<List<T>> typeToken) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            try (JsonWriter writer = new JsonWriter(new FileWriter(dataSource))) {
                gson.toJson(object, typeToken.getType(), writer);
            }
        } catch (IOException e) {
            logger.error(e);
        }

    }

    public List<AdrComponentDescriptor> getJsonAsObjectFromFile(String filePath) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(AdrComponentDescriptor.class, new AdrComponentJsonAdapter())
                    .create();
            JsonParser jsonParser = new JsonParser();
            try (JsonReader reader = new JsonReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filePath)))) {
                return gson.fromJson(
                        jsonParser.parse(reader),
                        new TypeToken<List<AdrComponentDescriptor>>() {
                        }.getType());
            } catch (IOException e) {
                MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing from file:", e));
            }
        } catch (IllegalStateException | JsonSyntaxException e) {
            MercuryStoreCore.errorHandlerSubject.onNext(new MercuryError("Error while importing from file:", e));
            return null;
        }
        return null;
    }
}
