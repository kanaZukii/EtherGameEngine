package dev.kanazukii.ether.engine.components;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ComponentDeserializer implements JsonSerializer<Component>, JsonDeserializer<Component> {

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String type = jsonObj.get("type").getAsString();
        JsonElement element = jsonObj.get("properties");

        try {
            return context.deserialize(element, Class.forName(type));

        } catch (Exception e) {
            throw new JsonParseException("Unknown element type: " + type, e);
        }
    }

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        json.add("properties", context.serialize(src, src.getClass()));
        return json;
    }
    
}
