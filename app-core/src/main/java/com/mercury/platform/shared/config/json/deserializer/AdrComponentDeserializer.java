package com.mercury.platform.shared.config.json.deserializer;

import com.google.gson.*;
import com.mercury.platform.shared.config.descriptor.adr.*;

import java.lang.reflect.Type;


public class AdrComponentDeserializer implements JsonDeserializer<AdrComponentDescriptor> {
    @Override
    public AdrComponentDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonObj = jsonElement.getAsJsonObject().getAsJsonPrimitive("type");
        Gson gson = new Gson();
        switch (AdrComponentType.valueOf(jsonObj.getAsString())){
            case GROUP:{
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrGroupDescriptor.class);
            }
            case ICON: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrIconDescriptor.class);
            }
            case PROGRESS_BAR: {
                return gson.fromJson(jsonElement.getAsJsonObject(), AdrProgressBarDescriptor.class);
            }
        }
        return null;
    }
}
