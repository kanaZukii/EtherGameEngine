#type vertex
#version 330 core

layout(location=0) in vec3 aPos;
layout(location=1) in vec4 aColor;
layout(location=2) in vec2 aTexCoords;
layout(location=3) in float aTexID;
layout(location=4) in float aGameObjID;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexID;
out float fGameObjID;

void main(){
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexID = aTexID;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
    fGameObjID = aGameObjID;
}

#type fragment
#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexID;
in float fGameObjID;

uniform sampler2D uTextures[8];

out vec3 color;

void main(){
    int id = int(fTexID);
    vec4 texColor = vec4(1.0);
    vec4 outColor = vec4(1.0);
    if(id > 0){
        switch(id){
        case 0:
            texColor = texture(uTextures[0], fTexCoords);
            break;
        case 1:
            texColor = texture(uTextures[1], fTexCoords);
            break;
        case 2:
            texColor = texture(uTextures[2], fTexCoords);
            break;
        case 3:
            texColor = texture(uTextures[3], fTexCoords);
            break;
        case 4:
            texColor = texture(uTextures[4], fTexCoords);
            break;
        case 5:
            texColor = texture(uTextures[5], fTexCoords);
            break;
        case 6:
            texColor = texture(uTextures[6], fTexCoords);
            break;
        case 7:
            texColor = texture(uTextures[7], fTexCoords);
            break;
        default:
            texColor =  vec4(1.0);
        }

        
        outColor = fColor * texColor; 
    }else{
        outColor = fColor;
    }

    if(outColor.a < 0.5){ discard;}
    
    color = vec3(fGameObjID, fGameObjID, fGameObjID);

}

