package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.editor.EtherImGUi;
import dev.kanazukii.ether.engine.utils.Configs;

public class Transform extends Component{
    
    public Vector2f position;
    public Vector2f scale;
    public int zIndex;
    public float rotation = 0.0f;

    public Transform(){
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position){
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale){
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale){
        this.position = position;
        this.scale = scale;
        zIndex = 0;
    }

    public Transform copy(){
        Transform copy = new Transform(new Vector2f(this.position), new Vector2f(this.scale));
        copy.rotation = this.rotation;
        copy.zIndex = this.zIndex;
        return copy;
    }

    public void copyTo(Transform target){
        target.position.set(this.position); 
        target.scale.set(this.scale);
        target.rotation = this.rotation;
        target.zIndex = this.zIndex;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Transform)) return false;

        Transform t = (Transform)o;
        return t.position.equals(this.position) && t.scale.equals(this.scale) 
            && t.rotation == this.rotation && t.zIndex == this.zIndex;
    }

    @Override
    public void ImGUI() {
        gameObject.name = EtherImGUi.inputText("Name: ", gameObject.name);
        EtherImGUi.dragVec2Controls("Position: ", position);
        EtherImGUi.dragVec2Controls("Scale: ", scale, Configs.GRID_WIDTH);
        rotation = EtherImGUi.dragFloatControls("Rotation: ", rotation);
        zIndex = EtherImGUi.dragIntControls("Z Index: ", zIndex);
    }
}
