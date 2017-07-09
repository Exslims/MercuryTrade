package com.mercury.platform.shared.config.json.serializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrComponentWrapper;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconGroupDescriptor;

import java.lang.reflect.Type;

public class AdrComponentSerializer implements JsonSerializer<AdrComponentDescriptor> {
    @Override
    public JsonElement serialize(AdrComponentDescriptor component, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();
        AdrComponentWrapper wrapper = new AdrComponentWrapper();
        wrapper.setComponentDescriptor(component);
        if(component instanceof AdrIconGroupDescriptor){
            wrapper.setType(AdrComponentType.ICON_GROUP);
        }
        if(component instanceof AdrIconDescriptor){
            wrapper.setType(AdrComponentType.ICON);
        }
        return gson.toJsonTree(wrapper);
    }
}
