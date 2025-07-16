package dev.kanazukii.ether.engine;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;
import static org.lwjgl.stb.STBImage.stbi_set_flip_vertically_on_load;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;

public class Texture {
    
    private String filepath;
    private transient int textureID;
    private transient int width, height;

    public Texture(){
        
    }

    public void init(String filepath){
        this.filepath = filepath;

        // Generate Texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        
        // Set Texture parameters
        // Repeat Image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); // Wrap X
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); // Wrap Y

        // When stretching, pixelate the texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // When shrinking, pixelate the texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);
        //ByteBuffer image = loadImageFromMemory(filepath, width, height, channels, 0);

        if(image != null){
            if(channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else{
                assert false : "Error in Texture loading: Unknown numbers of channels: " + channels.get(0);
            }

            this.width = width.get(0);
            this.height = height.get(0);
            System.out.println(filepath + " LOADED " + textureID);
        } else {
            assert false : "Error: Texture Could not load Image" + filepath + ""; 
        }

        stbi_image_free(image);
    }

    // From resource folder (Bundled to memory)
    private ByteBuffer loadImageFromMemory(String filepath, IntBuffer width, IntBuffer height, IntBuffer channels, int desiredChannels) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(filepath)){
            if(in == null) throw new RuntimeException("Texture Resource not found");

            // Read all bytes into a ByteBuffer
            byte[] dataInBytes = in.readAllBytes();
            ByteBuffer imageBuffer = MemoryUtil.memAlloc(dataInBytes.length);
            imageBuffer.put(dataInBytes).flip();

            // Load image through stbi
            ByteBuffer image = stbi_load_from_memory(imageBuffer, width, height, channels, desiredChannels);

            if(image == null){
                MemoryUtil.memFree(imageBuffer);
                throw new RuntimeException("Failed to load image: "+ STBImage.stbi_failure_reason());
            }
            MemoryUtil.memFree(imageBuffer);
            return image;
        } catch (IOException e) {
            System.err.println("Unable to load Texture");
            e.printStackTrace();
            return null;
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public int getID(){
        return textureID;
    }

    public String getFilePath(){
        return filepath;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unBind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
