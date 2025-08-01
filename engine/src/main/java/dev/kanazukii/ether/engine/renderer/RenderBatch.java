package dev.kanazukii.ether.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Texture;

public class RenderBatch implements Comparable<RenderBatch> {
    /* Vertex  Layout of the vertex data that is being sent to the gpu to draw
     *  --------------
     *  Position        Color                        Tex Coords     Tex ID   GameOBj ID
     *  float, float    float, float, float, float   float, float   float    float
     */

    // Constants for specifying the length of the each component in the vertex data
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int GameOBj_ID_SIZE = 1;

    // Constants for specifiying the offset (Where the string of component data begin) in float bytes
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
     private final int GameOBj_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;

    // Specify the length of a the data array within a singular vertex 
    private final int VERTEX_SIZE = 10;
    private final int VER_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    // list of sprites that is to be rendered
    private SpriteRenderer[] sprites;
    private int numSprite = 0;
    private boolean spriteFull = false;

    private float[] vertices;

    // List of textures (A whole image / A Sprite sheet ) that a single render batch can draw '8' (index 0 is reserved for no texture)
    private List<Texture> textures = new ArrayList<>();
    private int texSize = 8;
    private int[] texSlots = {0,1,2,3,4,5,6,7};

    private int vaoID, vboID; // Vertex Array Object & Vertex Buffer Object
    private int maxBatchSize;   // Maximum Object that it can render per draw call
    private int zIndex;     // Z index for blending

    public RenderBatch(int maxBatchSize, int zIndex) {
        System.out.println("Creating a Render Batch");

        sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;
        this.zIndex = zIndex;

        // Vertices Quads (Square)
        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
    }

    public void start(){
        // Generate and bind vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and Upload indices Buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute parameters
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VER_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VER_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VER_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VER_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, GameOBj_ID_SIZE, GL_FLOAT, false, VER_SIZE_BYTES, GameOBj_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    private int[] generateIndices(){
        // 6 indper quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i ++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    public int zIndex(){
        return zIndex;
    }

    public boolean hasRoom(){
        return !spriteFull;
    }

    public boolean hasRoomForTexture(){
        return textures.size() < texSize;
    }

    public boolean hasTexture(Texture tex){
        return textures.contains(tex);
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrIndex = 6 * index;
        int offset = 4 * index;
        
        // 3, 2, 0, 0, 2, 1     7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrIndex] = offset + 3;
        elements[offsetArrIndex + 1] = offset + 2;
        elements[offsetArrIndex + 2] = offset;

        // Triangle 2
        elements[offsetArrIndex + 3] = offset;
        elements[offsetArrIndex + 4] = offset + 2;
        elements[offsetArrIndex + 5] = offset + 1;
    }

    // Loads vertex properties Positin, color, texture coords, texture
    private void loadVertexProperties(int index){
        SpriteRenderer sprite = sprites[index];

        // Find the offset wihin array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texID = 0;  // 0 is reserved for sprite with no texture
        boolean found = false;
        // [0, texture, texture]
        if(sprite.getTexture() != null){
            for(int i = 0; i < textures.size(); i++){
                if(textures.get(i).getFilePath().equals(sprite.getTexture().getFilePath()) ){
                    texID = i + 1;
                    found = true;
                    break;
                } 
            }
            if(!found){
                System.err.println( sprite.gameObject.getName() + ": sprite texture Exist but cannot be found!");
            }
        }

        // Add vertices with the properties
        float xStep = 1.0f;
        float yStep = 1.0f;
        
        float rotation = sprite.gameObject.transform.rotation;
        Matrix4f transformMatrix = new Matrix4f().identity();
        if(rotation != 0.0f){
            transformMatrix.translate(sprite.gameObject.transform.position.x, sprite.gameObject.transform.position.y, 0.0f);
            transformMatrix.rotate((float)Math.toRadians(rotation), 0,0, 1);
            transformMatrix.scale(sprite.gameObject.transform.scale.x, sprite.gameObject.transform.scale.y, 1);
        }

        for(int i = 0; i < 4; i++){
            if(i == 1){
                yStep = 0.0f;
            } else if (i == 2){
                xStep = 0.0f;
            } else if (i == 3){
                yStep = 1.0f;
            }

            // Load position
            float posX = sprite.gameObject.transform.position.x;
            float posY = sprite.gameObject.transform.position.y;
            float scaleX = sprite.gameObject.transform.scale.x;
            float scaleY = sprite.gameObject.transform.scale.y;

            Vector4f currentPosition = new Vector4f(posX + (xStep * scaleX),  posY + (yStep * scaleY), 0, 1);
            if(rotation != 0.0f){
                currentPosition = new Vector4f(xStep, yStep, 0,1).mul(transformMatrix);
            }

            vertices[offset] = currentPosition.x;
            vertices[offset+1] = currentPosition.y;

            // Load Color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            // Load texture Coordinates
            vertices[offset+6] = texCoords[i].x;
            vertices[offset+7] = texCoords[i].y;

            //Load texture ID
            vertices[offset+8] = texID;

            //Load GameObj ID
            vertices[offset+9] = sprite.gameObject.getUID();

            offset += VERTEX_SIZE;
        }
    }

    public void addSprite(SpriteRenderer sprite){
        int index = numSprite;
        sprites[index] = sprite;
        numSprite++;

        if (sprite.getTexture() != null){
            if(!textures.contains(sprite.getTexture())){
                textures.add(sprite.getTexture());
            }
        }

        // Add properties to vertex array
        loadVertexProperties(index);

        if(numSprite >= maxBatchSize){
            spriteFull = true;
        }
    }

    // Draw call
    public void render(){

        boolean rebufferData = false;
        // Loop through sprites if something has changed
        for(int i = 0; i < numSprite; i++){
            SpriteRenderer sprite = sprites[i];
            if(sprite.isDirty()){
                loadVertexProperties(i);
                sprite.setClean();
                rebufferData = true;
                //System.out.println("Updated: " + i);
            }
        }
        // Rebuffer data for the frame if a sprite is dirty (modified)
        if(rebufferData){
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // Use shader
        Shader shader = Renderer.getBoundShader();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        for(int i = 0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", texSlots);
        
        // Draw VOA
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprite * 6, GL_UNSIGNED_INT, 0);

        // Unbinding current attributes for the next draw call
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        
        for(int i = 0; i < textures.size(); i++){
                textures.get(i).unBind();
            }

        shader.detach();
    }

    public boolean destroyIfExist(GameObject gameObject){
        SpriteRenderer sprRenderer = gameObject.getComponent(SpriteRenderer.class);
        for(int i = 0; i < numSprite; i++){
            if(sprites[i] == sprRenderer){
                for(int j = i; i < numSprite - 1; j++){
                    sprites[j] = sprites[j+1];
                    sprites[j].setDirty();
                }
                numSprite--;
                return true;
            }
        }

        return false;
    }

    @Override
    public int compareTo(RenderBatch arg0) {
        return Integer.compare(this.zIndex, arg0.zIndex);
    }

}
