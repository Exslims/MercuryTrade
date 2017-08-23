package com.mercury.platform.shared.config.json.deserializer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.descriptor.adr.AdrIconDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrProgressBarDescriptor;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupContentType;
import com.mercury.platform.shared.config.descriptor.adr.AdrTrackerGroupDescriptor;

import java.lang.reflect.Type;
import java.util.List;

public class AdrTrackerGroupDeserializer implements JsonDeserializer<AdrTrackerGroupDescriptor> {
    @Override
    public AdrTrackerGroupDescriptor deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonPrimitive jsonObj = jsonElement.getAsJsonObject().getAsJsonPrimitive("contentType");
        JsonArray cellsObj = jsonElement.getAsJsonObject().getAsJsonArray("cells");
        Gson gson = new Gson();
        AdrTrackerGroupDescriptor descriptor = gson.fromJson(jsonElement.getAsJsonObject(), AdrTrackerGroupDescriptor.class);
        switch (AdrTrackerGroupContentType.valueOf(jsonObj.getAsString())) {
            case PROGRESS_BARS: {
                descriptor.setCells(gson.fromJson(cellsObj, new TypeToken<List<AdrProgressBarDescriptor>>() {
                }.getType()));
                break;
            }
            case ICONS: {
                descriptor.setCells(gson.fromJson(cellsObj, new TypeToken<List<AdrIconDescriptor>>() {
                }.getType()));
            }
        }
        return descriptor;
    }
}
