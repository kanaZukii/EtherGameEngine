package dev.kanazukii.banana.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

public class Shader {

    private int shaderProgramID;

    private String vertexSrc;

    private String fragmentSrc;

    private String filepath;

    private boolean inUse = false;

    public Shader(String filepath){

        this.filepath = filepath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));  // Absolute file

            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            int index = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\n", index); // "\n for linux or mac  (\r\n) Win"
            String firstTag = source.substring(index, endOfLine).trim();

            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\n", index); // "\n for linux or mac  (\r\n) Win"
            String secondTag = source.substring(index,endOfLine).trim();

            if(firstTag.equals("vertex")){
                vertexSrc = splitString[1];
            } else if(firstTag.equals("fragment")){
                fragmentSrc = splitString[1];
            } else {
                throw new IOException("Unexpected Token '" + firstTag + "'");
            }

            if(secondTag.equals("vertex")){
                vertexSrc = splitString[2];
            } else if(secondTag.equals("fragment")){
                fragmentSrc = splitString[2];
            } else {
                throw new IOException("Unexpected Token '" + secondTag + "'");
            }


        } catch (Exception e) {
            e.printStackTrace();
            assert false : "Error cannot open file for shader '" + filepath + "'";
        }

        //System.out.println(vertexSrc);
        //System.out.println(fragmentSrc);

    }

    private String loadSourceFromMemory(String filepath) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(filepath); // From resource folder (Bundled to memory)
            if (is == null) {
                throw new IOException("Shader file not found in resources: " + filepath);
            }

        String source =  new String(is.readAllBytes());

        return source;
    }


    public void compile(){
        System.out.println( "Creating a shader");
        //Compile Shaders
        int vertexID, fragmentID;
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //Pass source code to be compiled
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);

        // Check errors

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int strlen = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '"+filepath+"' Vertex compilation failed!");
            System.out.println(glGetShaderInfoLog(vertexID, strlen));
            assert false: "";
        }

        // Link and Compile Fragment Shaders

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //Pass source code to be compiled
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);

        // Check errors

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if(success == GL_FALSE){
            int strlen = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '"+filepath+"' Fragment compilation failed!");
            System.out.println(glGetShaderInfoLog(fragmentID, strlen));
            assert false: "";
        }

        link(vertexID, fragmentID);
    }

    private void link(int vertexID, int fragmentID){
        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Errors
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int strlen = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("Error: '"+filepath+"' Linking failed!");
            System.out.println(glGetProgramInfoLog(shaderProgramID, strlen));
            assert false: "";
        }

    }

    public void use(){
        // Bind shader program
        if(!inUse){
            glUseProgram(shaderProgramID);
            inUse = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMat4f(String varname, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matrixBuffer);
        glUniformMatrix4fv(varLocation, false, matrixBuffer);
    }

    public void uploadMat3f(String varname, Matrix3f mat3){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matrixBuffer);
        glUniformMatrix3fv(varLocation, false, matrixBuffer);
    }
    

    public void uploadVec2f(String varname, Vector2f vec2f){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadVec3f(String varname, Vector3f vec3f){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        glUniform3f(varLocation, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec4f(String varname, Vector4f vec4f){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadFloat(String varname, float value){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varname, int value){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use(); // Make sure it is in use when uploading to shader
        glUniform1i(varLocation, value);
    }

    public void uploadTexture(String varname, int slot){
        int varLocation = glGetUniformLocation(shaderProgramID, varname);
        use();
        glUniform1i(varLocation, slot);
    }

}
