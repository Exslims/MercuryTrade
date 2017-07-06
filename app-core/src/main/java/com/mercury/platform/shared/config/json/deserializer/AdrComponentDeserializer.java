package com.mercury.platform.shared.config.json.deserializer;

import com.google.gson.*;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentWrapper;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.entity.adr.AdrGroupDescriptor;

import java.lang.reflect.Type;


public class AdrComponentDeserializer implements JsonDeserializer<AdrComponentWrapper> {
    @Override
    public AdrComponentWrapper deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonObj = jsonElement.getAsJsonObject().getAsJsonPrimitive("type");
        Gson gson = new Gson();
        AdrComponentWrapper wrapper = new AdrComponentWrapper();
        wrapper.setType(AdrComponentType.valueOf(jsonObj.getAsString()));
        switch (wrapper.getType()){
            case GROUP:{
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrGroupDescriptor.class));
                break;
            }
            case ICONIZED: {
                wrapper.setComponentDescriptor(gson.fromJson(jsonElement.getAsJsonObject().getAsJsonObject("componentDescriptor"), AdrIconDescriptor.class));
                break;
            }
            case PROGRESS_BAR: {
                return wrapper;
            }
        }
        return wrapper;
    }
}
