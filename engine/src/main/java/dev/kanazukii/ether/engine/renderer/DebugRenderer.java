package dev.kanazukii.ether.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
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
import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.utils.AssetPool;
import dev.kanazukii.ether.engine.utils.Maths;

public class DebugRenderer {
    
    // TODO: FIX When a line getting stuck on to the frame when a different instance of a line dies

    private static int MAX_LINES = 500;
    private static List<Line2D> lines = new ArrayList<>();
    // 6 floats per vertex and 2 vertices per line
    private static float[] vertexArr = new float[MAX_LINES * 6 * 2];
    private static Shader shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    public static void start(){
        // Generating the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Generate the vbo
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexArr.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Vertex Array Attributes
        glVertexAttribPointer(0,3,GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer( 1,3,GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        glLineWidth(2.0f);
    }

    public static void beginFrame(){
        if(!started){
            start();
            started = true;
        }

        // Remove dead lines
        for(int i = 0; i < lines.size(); i++){
            if(lines.get(i).beginFrame() < 0){
                lines.remove(i);
                i--;
            }
        }
    }

    public static void render(){
        
        if(lines.size() <= 0) return;

        int index = 0;
        
        for(Line2D line : lines){
            for(int i = 0; i < 2; i++){
                Vector2f postion = i == 0 ? line.getStart() : line.getEnd();
                Vector3f color  = line.getColor();

                //Load Position
                vertexArr[index] = postion.x;
                vertexArr[index+1] = postion.y;
                vertexArr[index+2] = -10.0f; // Depth z index

                // Load the color
                vertexArr[index+3] = color.x;
                vertexArr[index+4] = color.y;
                vertexArr[index+5] = color.z;

                index += 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        int lineVertexCount = lines.size() * 2;
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArr, 0, lineVertexCount*6));

        // Use the shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().getCamera().getViewMatrix());

        // Bind vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the batch
        glDrawArrays(GL_LINES, 0, lineVertexCount);
        
        // Disable Location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    // Add Line2D
    public static void addLine2D(Vector2f start, Vector2f end){

        // TODO: ADD CONSTANT COLORS
        addLine2D(start,end, new Vector3f(1,0,0), 1);
    }

    // Add Line2D
    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color){

        addLine2D(start,end, color, 1);
    }

    // Add Line2D
    public static void addLine2D(Vector2f start, Vector2f end, Vector3f color, int duration){
        if(lines.size() >= MAX_LINES) return;
        lines.add(new Line2D(start, end, color, duration));
    }

    public static void addRect2D(Vector2f center, Vector2f dimension, float rotation){
        addRect2D(center, dimension, rotation, new Vector3f(1,0,0), 1);
    }

    public static void addRect2D(Vector2f center, Vector2f dimension, float rotation, Vector3f color){
        addRect2D(center, dimension, rotation, color, 1);
    }

    public static void addRect2D(Vector2f center, Vector2f dimension, float rotation, Vector3f color, int duration){
        Vector2f min = new Vector2f(center).sub(new Vector2f(dimension).div(2.0f));
        Vector2f max = new Vector2f(center).add(new Vector2f(dimension).div(2.0f));

        Vector2f[] vertices = {
            new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
            new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)
        };

        if(rotation != 0.0f){
            for(Vector2f vector : vertices){
                Maths.rotate(vector, rotation, center);
            }
        }

        addLine2D(vertices[0], vertices[1], color, duration);
        addLine2D(vertices[1], vertices[2], color, duration);
        addLine2D(vertices[2], vertices[3], color, duration);
        addLine2D(vertices[3], vertices[0], color, duration);
    }

    public static void addCircle2D(Vector2f center, float radius){
        addCircle2D(center, radius, new Vector3f(1,0,0), 1);
    }

    public static void addCircle2D(Vector2f center, float radius, Vector3f color){
        addCircle2D(center, radius, color, 1);
    }

    public static void addCircle2D(Vector2f center, float radius, Vector3f color, int duration){
        Vector2f[] points = new Vector2f[24];
        float increment = 360 / points.length;
        float currentAngle = 0;

        for(int i = 0; i < points.length; i++){
            Vector2f tmp = new Vector2f(radius, 0);
            Maths.rotate(tmp, currentAngle, new Vector2f());
            points[i] = new Vector2f(tmp).add(center);

            if(i > 0){
                addLine2D(points[i-1], points[i], color, duration);
            }

            currentAngle += increment;
        }

        addLine2D(points[points.length-1], points[0], color, duration);
    }

}
