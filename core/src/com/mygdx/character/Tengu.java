package com.mygdx.character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.character.animations.CharacterAnimation;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;
import com.sun.javafx.scene.traversal.Direction;

import java.awt.geom.Point2D;

public class Tengu implements ApplicationListener
{
    public static final int ACTION_STANCE = 0;
    public static final int ACTION_JUMP = 1;
    public static final int ACTION_RUN = 2;
    public static final int ACTION_FALL = 3;

    public static final int RUN_SPEED = 400;
    public static final int JUMP_SPEED = 400;
    public static final int FALL_SPEED = 400;

    private int currentAction;

    private Vector2 position;
    private Vector2 speed;
    private Vector2 direction;

    private Body body;

    private CharacterAnimation currentAnimation;
    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    boolean isOnBlockedAnimation = false;

    public Tengu()
    {
        this.currentAction = ACTION_STANCE;
        this.position = new Vector2(0,0);
        this.speed = Vector2.Zero;
        this.direction = new Vector2(1,0);

        characterStanceAnimation = new CharacterStanceAnimation(this);
        characterRunAnimation = new CharacterRunAnimation(this);
        characterJumpAnimation = new CharacterJumpAnimation(this);

        this.currentAnimation = characterStanceAnimation;
    }

    @Override
    public void create()
    {
        characterStanceAnimation.create();
        characterRunAnimation.create();
        characterJumpAnimation.create();
    }

    @Override
    public void resize(int width, int height) {}

    public void update()
    {
        position = body.getPosition();
        //handleKeyboard();
        currentAnimation.update();

        /*if(isOnBlockedAnimation && currentAnimation.isAnimationFinished()) {
            isOnBlockedAnimation = false;
        }

        //decrease jump speed
        if(currentAction == ACTION_JUMP) {
            float decreaseSpeed = (JUMP_SPEED / (CharacterJumpAnimation.ANIMATION_DURATION * 100)) * (currentAnimation.getElapsedTime() * 100);
            if(JUMP_SPEED - decreaseSpeed < 0) {
                decreaseSpeed = JUMP_SPEED;
            }
            speed = new Vector2(speed.x, JUMP_SPEED - decreaseSpeed);
        }

        //if on air and falling
        if(position.y > 0 && currentAction != ACTION_JUMP) {
            changeAnimation(ACTION_FALL);
            speed = new Vector2(speed.x, FALL_SPEED);
            direction = new Vector2(direction.x, -1);
        }

        Vector2 newPosition = new Vector2(position.x + (direction.x * speed.x) * Gdx.graphics.getDeltaTime(), position.y + (direction.y * speed.y) * Gdx.graphics.getDeltaTime());
        if(newPosition.y < 0) {
            newPosition.y = 0;
            changeAnimation(ACTION_STANCE);
        }

        position = newPosition;*/
    }

    @Override
    public void render()
    {
        currentAnimation.render();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    public void handleKeyboard()
    {
        boolean noKeyPressed = true;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }
            speed = new Vector2(RUN_SPEED, speed.y);
            direction = new Vector2(1, direction.y);
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }
            speed = new Vector2(RUN_SPEED, speed.y);
            direction = new Vector2(-1, direction.y);
            noKeyPressed = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isOnBlockedAnimation)
        {
            changeAnimation(ACTION_JUMP);
            speed = new Vector2(speed.x, JUMP_SPEED);
            direction = new Vector2(direction.x, 1);
            characterJumpAnimation.start();
        }

        if(noKeyPressed && !isOnBlockedAnimation)
        {
            if(currentAction != ACTION_FALL) {
                speed = Vector2.Zero;
            }
            changeAnimation(ACTION_STANCE);
        }
    }

    private void changeAnimation(int animation)
    {
        if(animation != currentAction)
        {
            currentAnimation.reset();
        }

        if(animation == ACTION_STANCE)
        {
            isOnBlockedAnimation = false;
            currentAction = ACTION_STANCE;
            currentAnimation = characterStanceAnimation;
        } else if (animation == ACTION_RUN)
        {
            isOnBlockedAnimation = false;
            currentAction = ACTION_RUN;
            currentAnimation = characterRunAnimation;
        } else if (animation == ACTION_JUMP)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_JUMP;
            currentAnimation = characterJumpAnimation;
        } else if (animation == ACTION_FALL)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_FALL;
            currentAnimation = characterJumpAnimation;
        }
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

    public void createBody(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // Set our body to the same position as our sprite
        bodyDef.position.set(position.x, position.y);

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(175f, 175f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }
}
