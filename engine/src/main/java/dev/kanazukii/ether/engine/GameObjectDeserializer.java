package dev.kanazukii.ether.engine;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import dev.kanazukii.ether.engine.components.Component;
import dev.kanazukii.ether.engine.components.Transform;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        String name = jsonObj.get("name").getAsString();
        JsonArray components = jsonObj.getAsJsonArray("components");
        
        GameObject gameObject = new GameObject(name);
        for(JsonElement element: components){
            Component component = context.deserialize(element, Component.class);
            gameObject.addComponent(component);
        }
        gameObject.transform = gameObject.getComponent(Transform.class);

        return gameObject;
    }

}
