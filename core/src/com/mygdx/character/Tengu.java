package com.mygdx.character;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.character.animations.CharacterAnimation;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;
import com.mygdx.game.MyGdxGame;
import com.sun.javafx.scene.traversal.Direction;

import java.awt.geom.Point2D;

public class Tengu extends ApplicationAdapter
{
    public static final int ACTION_STANCE = 0;
    public static final int ACTION_JUMP = 1;
    public static final int ACTION_RUN = 2;
    public static final int ACTION_FALL = 3;

    public static final float SPEED_RUN = 5f;
    public static final float SPEED_JUMP = 15f;
    public static final float SPEED_FALL = 400;
    public static final float MAX_VELOCITY = 7f;

    private int currentAction;

    private Vector2 position;
    private Vector2 direction;

    private Body body;
    private Fixture fixture;

    private CharacterAnimation currentAnimation;
    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    boolean isJumping = false;
    boolean isOnGround = true;

    private World world;

    public Tengu(World world)
    {
        this.currentAction = ACTION_STANCE;
        this.position = new Vector2(400,0);
        this.direction = new Vector2(1,0);

        characterStanceAnimation = new CharacterStanceAnimation(this);
        characterRunAnimation = new CharacterRunAnimation(this);
        characterJumpAnimation = new CharacterJumpAnimation(this);

        this.currentAnimation = characterStanceAnimation;
        this.world = world;
    }

    public void create()
    {
        characterStanceAnimation.create();
        characterRunAnimation.create();
        characterJumpAnimation.create();
        createBody();
        setContactListener();
    }

    public void update()
    {
        //isOnGround = isOnGround();
        handleKeyboard();
        position = new Vector2(MyGdxGame.MetersToPixels(body.getPosition().x) - (getWidth()/2), MyGdxGame.MetersToPixels(body.getPosition().y) - (getHeight()/2));
        currentAnimation.update();
    }

    public void render()
    {
        update();
        currentAnimation.render();
    }

    public void dispose() {}

    public void handleKeyboard()
    {
        boolean noKeyPressed = true;
        Vector2 velocity = body.getLinearVelocity();
        Vector2 position = body.getPosition();

        // cap max velocity on x
        if(Math.abs(velocity.x) > SPEED_RUN) {
            velocity.x = Math.signum(velocity.x) * SPEED_RUN;
            body.setLinearVelocity(velocity.x, velocity.y);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) && velocity.x < SPEED_RUN )
        {
            if(isOnGround) {
                changeAnimation(ACTION_RUN);
            }
            body.applyLinearImpulse(SPEED_RUN, 0, position.x, position.y, true);
            direction = new Vector2(1, direction.y);
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) && velocity.x > -SPEED_RUN )
        {
            if(isOnGround) {
                changeAnimation(ACTION_RUN);
            }
            body.applyLinearImpulse(-SPEED_RUN, 0, position.x, position.y, true);
            direction = new Vector2(-1, direction.y);
            noKeyPressed = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isOnGround)
        {
            changeAnimation(ACTION_JUMP);
            isJumping = true;
            body.setLinearVelocity(velocity.x, 0);
            body.setTransform(position.x, position.y + 0.01f, 0);
            body.applyLinearImpulse(0, SPEED_JUMP, position.x, position.y, true);
            noKeyPressed = false;
        }

        if(noKeyPressed && Math.abs(velocity.x) <= 3 && isOnGround) {
            if(currentAnimation.isLastFrame()) {
                changeAnimation(ACTION_STANCE);
            }
        }
    }

    private void setContactListener()
    {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().equals(fixture) || contact.getFixtureB().equals(fixture)) {
                    isOnGround = true;
                }
            }

            @Override
            public void endContact(Contact contact) {
                if(contact.getFixtureA().equals(fixture) || contact.getFixtureB().equals(fixture)) {
                    isOnGround = false;
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
    }

    private void changeAnimation(int animation)
    {
        if(animation != currentAction)
        {
            currentAnimation.reset();
        }

        if(animation == ACTION_STANCE)
        {
            currentAction = ACTION_STANCE;
            currentAnimation = characterStanceAnimation;
        } else if (animation == ACTION_RUN)
        {
            currentAction = ACTION_RUN;
            currentAnimation = characterRunAnimation;
        } else if (animation == ACTION_JUMP)
        {
            currentAction = ACTION_JUMP;
            currentAnimation = characterJumpAnimation;
        } else if (animation == ACTION_FALL)
        {
            currentAction = ACTION_FALL;
            currentAnimation = characterJumpAnimation;
        }
    }

    public void createBody()
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(MyGdxGame.PixelsToMeters(position.x + getWidth()/2f), MyGdxGame.PixelsToMeters(position.y + getHeight()/2f));

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(MyGdxGame.PixelsToMeters(getWidth()/2f), MyGdxGame.PixelsToMeters(getHeight()/2f), new Vector2(0, 0), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 2f;

        fixture = body.createFixture(fixtureDef);
        shape.dispose();
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public Tengu setDirection(Vector2 direction)
    {
        this.direction = direction;
        return this;
    }

    public SpriteBatch getSpriteBatch()
    {
        return currentAnimation.getSpriteBatch();
    }

    private float getWidth()
    {
        return currentAnimation.getWidth();
    }

    private float getHeight()
    {
        return currentAnimation.getHeight();
    }
}
