package dev.kanazukii.ether.engine.physcis2D;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.components.Transform;
import dev.kanazukii.ether.engine.physcis2D.components.Box2DCollider;
import dev.kanazukii.ether.engine.physcis2D.components.CircleCollider;
import dev.kanazukii.ether.engine.physcis2D.components.RigidBody2D;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f/60.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void update(float deltaTime){
        physicsTime += deltaTime;
        if(physicsTime >= 0.0f){
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void add(GameObject gameObject){
        RigidBody2D rigidBod =  gameObject.getComponent(RigidBody2D.class);
        if(rigidBod != null && rigidBod.getRawBody() == null){
            Transform transform = gameObject.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rigidBod.getAngularDamping();
            bodyDef.linearDamping = rigidBod.getLinearDamping();
            bodyDef.fixedRotation = rigidBod.getFixedRotation();
            bodyDef.bullet = rigidBod.getContinousCollision();

            switch (rigidBod.getBodyType()) {
                case Kinematic:
                    bodyDef.type =  BodyType.KINEMATIC;
                    break;
                case Dynamic:
                    bodyDef.type =  BodyType.DYNAMIC;
                    break;
                case Static:
                    bodyDef.type =  BodyType.STATIC;
                    break;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider;
            Box2DCollider box2dCollider;

            if((circleCollider = gameObject.getComponent(CircleCollider.class))!= null){
                shape.setRadius(circleCollider.getRadius());
            } else if((box2dCollider = gameObject.getComponent(Box2DCollider.class))!= null){
                Vector2f halfsize = new Vector2f(box2dCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = box2dCollider.getOffset();
                Vector2f origin = new Vector2f(box2dCollider.getOrigin());
                shape.setAsBox(halfsize.x, halfsize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 position = bodyDef.position;
                float xPosition = position.x + offset.x;
                float yPosition = position.y + offset.y;

                bodyDef.position.set(xPosition, yPosition);
            }

            Body body = world.createBody(bodyDef);
            rigidBod.setRawBody(body);
            body.createFixture(shape, rigidBod.getMass());
        }
    }

    public void destroyGameObject(GameObject gameObject){
        RigidBody2D rigidBod = gameObject.getComponent(RigidBody2D.class);
        if(rigidBod == null) return;
        
        if(rigidBod.getRawBody() != null){
            world.destroyBody(rigidBod.getRawBody());
            rigidBod.setRawBody(null);
        }
    }
}
