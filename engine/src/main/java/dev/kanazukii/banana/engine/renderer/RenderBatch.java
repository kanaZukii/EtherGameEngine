package dev.kanazukii.banana.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
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
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Vector4f;

import dev.kanazukii.banana.engine.Window;
import dev.kanazukii.banana.engine.components.SpriteRenderer;

public class RenderBatch {
    /* Vertex
     *  --------------
     *  Position        Color
     *  float, float    float, float, float, float
     */

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VER_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprite = 0;
    private boolean spriteFull = false;

    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        shader = new Shader("shaders/default.glsl");
        shader.compile();
        sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

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
    }

    private int[] generateIndices(){
        // 6 indper quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i ++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    public boolean hasRoom(){
        return !spriteFull;
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

    private void loadVertexProperties(int index){
        SpriteRenderer sprite = sprites[index];

        // Find the offset wihin array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the properties
        float xStep = 1.0f;
        float yStep = 1.0f;
        for(int i = 0; i < 4; i++){
            if(i == 1){
                yStep = 0.0f;
            } else if (i == 2){
                xStep = 0.0f;
            } else if (i == 3){
                yStep= 1.0f;
            }

            // Load position
            float posX = sprite.gameObject.transform.position.x;
            float posY = sprite.gameObject.transform.position.y;
            float scaleX = sprite.gameObject.transform.scale.x;
            float scaleY = sprite.gameObject.transform.scale.y;

            vertices[offset] = posX + (xStep * scaleX);
            vertices[offset+1] = posY + (yStep * scaleY);

            // Load Color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    public void addSprite(SpriteRenderer sprite){
        int index = numSprite;
        sprites[index] = sprite;
        numSprite++;

        // Add properties to vertex array
        loadVertexProperties(index);

        if(numSprite >= maxBatchSize){
            spriteFull = true;
        }
    }

    public void render(){
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());
        
        // Draw VOA
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glDrawElements(GL_TRIANGLES, numSprite * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

}
