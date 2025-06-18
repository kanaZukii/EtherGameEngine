package dev.kanazukii.banana.engine;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*; // for VAOs (glGenVertexArrays, glBindVertexArray, etc.)

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import dev.kanazukii.banana.engine.components.SpriteRenderer;
import dev.kanazukii.banana.engine.renderer.Shader;
import dev.kanazukii.banana.engine.utils.Time;

public class LevelEditorScene extends Scene {

    private Shader defaultShader;
    private Texture test_texture;
    private GameObject testObj;

    boolean firstTime = false;

    private float[] vertexArr = {
        // Position             //Color                 //UV Coordinates for Texture
        150f, 50f, 0.0f,      1.0f, 1.0f, 1.0f, 1.0f,   1,1,// 0
        50f, 150f, 0.0f,      1.0f, 1.0f, 1.0f, 1.0f,   0,0,// 1
        150f, 150f, 0.0f,       1.0f, 1.0f, 1.0f, 1.0f, 1,0,// 2
        50f, 50f, 0.0f,     1.0f, 1.0f, 1.0f, 1.0f ,    0,1// 3
    
    };

    // Must be in counter clockwise order
    private int[] elementArr = {

        2,1,0,
        0,1,3
    };

    private int shaderProgram;

    private int vaoID, vboID, eboID;

    public LevelEditorScene(){
        System.out.println("Level Editor Scene");
    }

    @Override
    public void init() {
        testObj = new GameObject("Test Obj");
        testObj.addComponent(new SpriteRenderer());
        addGameObject(testObj);

        camera = new Camera(new Vector2f());
        //Shaders
        defaultShader = new Shader("shaders/default.glsl");
        defaultShader.compile();
        test_texture = new Texture("assets/Hero_down2.png");

        // Generate A VAO, VBO, EBO buffer objects and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArr.length);
        vertexBuffer.put(vertexArr).flip();

        // Create a VBO and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        //Create the index buffer and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArr.length);
        elementBuffer.put(elementArr).flip();

        // Create a EBO and upload element buffer
        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float deltaTime) {
        camera.getPosition().y -= deltaTime * 50.0f;

        // Bind shader program
        defaultShader.use();

        //Upload Texture
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        test_texture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getDeltaTime());
        //Bind the VAO
        glBindVertexArray(vaoID);
        //Enable the vertex Attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArr.length, GL_UNSIGNED_INT, 0);

        //Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

        for (GameObject gameObject: gameObjects){
            gameObject.update(deltaTime);
        }
    }

    
}