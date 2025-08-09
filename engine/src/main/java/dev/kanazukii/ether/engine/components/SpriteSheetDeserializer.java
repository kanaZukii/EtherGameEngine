package dev.kanazukii.ether.engine.components;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import dev.kanazukii.ether.engine.utils.AssetPool;

public class SpriteSheetDeserializer implements JsonDeserializer<Spritesheet>{

    @Override
    public Spritesheet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObj = json.getAsJsonObject();
        
        String filename = jsonObj.getAsJsonObject("texture").get("filepath").getAsString();
        int spriteWidth = jsonObj.get("spriteWidth").getAsInt();
        int spriteHeight = jsonObj.get("spriteHeight").getAsInt();
        int numSprites = jsonObj.get("numSprites").getAsInt();
        int spacing = jsonObj.get("spacing").getAsInt();

        // Create the spritesheet
        Spritesheet sheet = new Spritesheet(
            AssetPool.getTexture(filename),
            spriteWidth,
            spriteHeight,
            numSprites,
            spacing
        );

        return sheet;
    }
    
    
}
