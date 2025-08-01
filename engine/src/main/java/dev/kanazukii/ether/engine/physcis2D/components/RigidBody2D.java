package dev.kanazukii.ether.engine.physcis2D.components;

import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

import dev.kanazukii.ether.engine.components.Component;
import dev.kanazukii.ether.engine.physcis2D.enums.BodyType;

public class RigidBody2D extends Component{
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.0f;
    private float linearDamping = 0.0f;
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;

    private boolean fixedRotation = false;
    private boolean continuousCollision = true;

    private transient Body rawbody = null;

    public void setFixedRotation(boolean value){
        fixedRotation = value;
    }

    public void setContinousCollision(boolean value){
        continuousCollision = value;
    }

    public void setVelocity(Vector2f value){
        velocity = value;
    }

    public void setMass(float value){
        mass = value;
    }

    public void setAngularDamping(float value){
        angularDamping = value;
    }

    public void setBodyType(BodyType bodyType){
        this.bodyType = bodyType;
    }

    public void setRawBody(Body body){
        rawbody = body;
    }

    public void setLinearDamping(float value){
        linearDamping = value;
    }


    public boolean getFixedRotation(){
        return fixedRotation;
    }

    public boolean getContinousCollision(){
        return continuousCollision;
    }

    public Vector2f getVelocity(){
        return velocity;
    }

    public float getMass(){
        return mass;
    }

    public float getAngularDamping(){
        return angularDamping;
    }

    public BodyType getBodyType(){
        return bodyType;
    }

    public Body getRawBody(){
        return rawbody;
    }

    public float getLinearDamping(){
        return linearDamping;
    }

    @Override
    public void update(float deltaTime){
        if(rawbody != null){
            this.gameObject.transform.position.set(rawbody.getPosition().x, rawbody.getPosition().y);
            this.gameObject.transform.rotation =  (float)Math.toDegrees(rawbody.getAngle());
        }
    }
}
