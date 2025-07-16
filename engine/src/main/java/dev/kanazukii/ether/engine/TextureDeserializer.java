package dev.kanazukii.ether.engine;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import dev.kanazukii.ether.engine.utils.AssetPool;

public class TextureDeserializer implements JsonDeserializer<Texture>{

    @Override
    public Texture deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context){
        JsonObject jsonObj = json.getAsJsonObject();
        String filename = jsonObj.get("filepath").getAsString();

        Texture texture = AssetPool.getTexture(filename);
        
        return texture;
    }
    
}
