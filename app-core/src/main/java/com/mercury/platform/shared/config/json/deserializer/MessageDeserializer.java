package com.mercury.platform.shared.config.json.deserializer;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mercury.platform.shared.entity.message.NotificationDescriptor;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<NotificationDescriptor> {
    @Override
    public NotificationDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
