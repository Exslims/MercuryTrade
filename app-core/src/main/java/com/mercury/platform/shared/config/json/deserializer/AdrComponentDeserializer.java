package com.mercury.platform.shared.config.json.deserializer;

import com.google.gson.*;
import com.mercury.platform.shared.config.descriptor.adr.*;

import java.lang.reflect.Type;


public class AdrComponentDeserializer implements JsonDeserializer<AdrComponentWrapper> {
    @Override
    public AdrComponentWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonObj = jsonElement.getAsJsonObject().getAsJsonPrimitive("type");
        Gson gson = new Gson();
        AdrComponentWrapper wrapper = new AdrComponentWrapper();
        wrapper.setType(AdrComponentType.valueOf(jsonObj.getAsString()));
        switch (wrapper.getType()){
            case ICON_GROUP:{
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrIconGroupDescriptor.class));
                break;
            }
            case PROGRESS_BAR_GROUP: {
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrProgressBarGroupDescriptor.class));
                break;
            }
            case ICON: {
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrIconDescriptor.class));
                break;
            }
            case PROGRESS_BAR: {
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrProgressBarDescriptor.class));
                break;
            }
        }
        return wrapper;
    }
}
