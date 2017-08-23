package com.mercury.platform.shared.config.json.deserializer;

import com.google.gson.*;
import com.mercury.platform.shared.config.descriptor.adr.*;

import java.lang.reflect.Type;


public class AdrComponentJsonAdapter implements JsonDeserializer<AdrComponentDescriptor>, JsonSerializer<AdrComponentDescriptor> {
    @Override
    public AdrComponentDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonObj = jsonElement.getAsJsonObject().getAsJsonPrimitive("type");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(AdrTrackerGroupDescriptor.class, new AdrTrackerGroupDeserializer())
                .create();
        switch (AdrComponentType.valueOf(jsonObj.getAsString())) {
            case TRACKER_GROUP: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrTrackerGroupDescriptor.class);
            }
            case ICON: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrIconDescriptor.class);
            }
            case PROGRESS_BAR: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrProgressBarDescriptor.class);
            }
            case CAPTURE: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrCaptureDescriptor.class);
            }
        }
        return null;
    }

    @Override
    public JsonElement serialize(AdrComponentDescriptor descriptor, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();
        switch (descriptor.getType()) {
            case TRACKER_GROUP: {
                return gson.toJsonTree(descriptor, AdrTrackerGroupDescriptor.class);
            }
            case ICON: {
                return gson.toJsonTree(descriptor, AdrIconDescriptor.class);
            }
            case PROGRESS_BAR: {
                return gson.toJsonTree(descriptor, AdrProgressBarDescriptor.class);
            }
            case CAPTURE: {
                return gson.toJsonTree(descriptor, AdrCaptureDescriptor.class);
            }
        }
        return null;
    }
}
