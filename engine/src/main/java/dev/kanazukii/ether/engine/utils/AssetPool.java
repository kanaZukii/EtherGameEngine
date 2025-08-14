package dev.kanazukii.ether.engine.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import dev.kanazukii.ether.engine.components.SpriteSheetDeserializer;
import dev.kanazukii.ether.engine.components.Spritesheet;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.components.TextureDeserializer;
import dev.kanazukii.ether.engine.renderer.Shader;

public class AssetPool {
    
    // Hashmaps that contains all the resources that the game will use Shaders, Textures, Spritesheet Object
    // Absolute File location is used as key for indentifying the resource
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    //Loads resource from classpath (used when inside JAR or src/main/resources)
    public static InputStream getFileFromMemory(String resourcePath) {
        InputStream in = AssetPool.class.getClassLoader().getResourceAsStream(resourcePath);

        if (in == null) {
            throw new RuntimeException("Resource not found in memory: " + resourcePath);
        }

        return in;
    }

    // Retrieves and compiles a shader from disk
    public static Shader getShader(String filepath) {
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        
        if (shaders.containsKey(absolutePath)) {
            return shaders.get(absolutePath);
        } else {
            Shader shader = new Shader(filepath);
            shader.compile();
            shaders.put(absolutePath, shader);
            return shader;
        }
    }

    // Retrieves and compiles a shader that is compiled into memory
    public static Shader getShaderFromMemory(String filepath) {
        if (shaders.containsKey(filepath)) {
            return shaders.get(filepath);
        } else {
            Shader shader = new Shader(filepath);
            shader.compile();
            shaders.put(filepath, shader);
            return shader;
        }
    }

    // Retrieves a texture from disk
    public static Texture getTexture(String filepath) {
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();

        if (textures.containsKey(absolutePath)) {
            return textures.get(absolutePath);
        } else {
            Texture texture = new Texture();
            texture.init(filepath);
            textures.put(absolutePath, texture);
            return texture;
        }
    }

    // Retrieves a texture that is compiled into memory
    public static Texture getTextureFromMemory(String filepath) {
        if (textures.containsKey(filepath)) {
            return textures.get(filepath);
        } else {
            Texture texture = new Texture();
            texture.init(filepath);
            textures.put(filepath, texture);
            return texture;
        }
    }

    // Adds a created sprite sheet object with its texture's filepath as the key
    public static void addSpriteSheet(String filepath, Spritesheet spritesheet){
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        if(!spritesheets.containsKey(absolutePath)){
            spritesheets.put(absolutePath, spritesheet);
        }
    }

    // Checks if it holds the sprite sheet object and retrieves it
    public static Spritesheet getSpriteSheet(String filepath){
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        if(!spritesheets.containsKey(absolutePath)){
            System.err.println( "Error: Resource: '" + filepath + "' this sprite sheet has not been added yet.");
            assert false: "Error: Resource: '" + filepath + "' this sprite sheet has not been added yet.";
        }

        return spritesheets.getOrDefault(absolutePath, null); // TODO: Create reate a missing texture sprite 
    }

    public static void removeSpriteSheet(String filepath){
        File file = new File(filepath);
        String absolutePath = file.getAbsolutePath();
        if(spritesheets.containsKey(absolutePath)){
            spritesheets.remove(absolutePath);
        }
    }

    public static void saveAssets(){
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .registerTypeAdapter(Spritesheet.class, new SpriteSheetDeserializer())
                        .create();
        try {
            FileWriter writer = new FileWriter("assets.txt");
            Collection<Spritesheet> sheets = spritesheets.values();
            writer.write(gson.toJson(sheets));
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadAssets(){
        spritesheets.clear();
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .registerTypeAdapter(Spritesheet.class, new SpriteSheetDeserializer())
                        .create();

        try {
            String savedAssets = new String(Files.readAllBytes(Paths.get("assets.txt")));
            if (!savedAssets.isEmpty()) {
                Type listType = new TypeToken<List<Spritesheet>>(){}.getType();
                List<Spritesheet> sheets = gson.fromJson(savedAssets, listType);

                for (Spritesheet sheet : sheets) {
                    String key = sheet.getTexture().getFilePath();
                    AssetPool.addSpriteSheet(key, sheet);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Spritesheet> getLoadedSpriteSheets(){
        return new ArrayList<>(spritesheets.values());
    }

    public static List<Texture> getLoadedTextures(){
        return new ArrayList<>(textures.values());
    }


}
