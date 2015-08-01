package com.mygdx.character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
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

    public static final int RUN_SPEED = 200;
    public static final int JUMP_SPEED = 200;
    public static final int FALL_SPEED = 200;

    private int currentAction;

    private Vector2 position;
    private Vector2 speed;
    private Vector2 direction;

    private CharacterAnimation currentAnimation;
    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    boolean isOnBlockedAnimation = false;
    boolean isJumping = false;
    boolean isFalling = false;
    boolean isOnAir = false;

    public Tengu()
    {
        this.currentAction = ACTION_STANCE;
        this.position = new Vector2(0, 0);//Vector2.Zero;
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
        isOnAir = position.y > 0;

        if(isOnBlockedAnimation && currentAnimation.isAnimationFinished()) {
            if(isJumping)
            {
                isJumping = false;
                if(isOnAir) {
                    changeAnimation(ACTION_FALL);
                }
            }

            if(isFalling && position.y <= 0)
            {
                isFalling = false;
                changeAnimation(ACTION_STANCE);
            }
        }

        this.handleKeyboard();
        currentAnimation.update();

        position = new Vector2(position.x + (direction.x * speed.x) * Gdx.graphics.getDeltaTime(), position.y + (direction.y * speed.y) * Gdx.graphics.getDeltaTime());
    }

    @Override
    public void render()
    {
        update();

        if(isOnAir && isFalling) {
            currentAnimation.renderFrame(3);
        } else {
            currentAnimation.render();
        }

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
            //currentAction = ACTION_JUMP;
            //noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            //currentAction = ACTION_STANCE;
            //noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }
            direction = new Vector2(1, direction.y);
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }
            direction = new Vector2(-1, direction.y);
            noKeyPressed = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isOnBlockedAnimation)
        {
            changeAnimation(ACTION_JUMP);
            characterJumpAnimation.start();
        }

        if(noKeyPressed && !isOnBlockedAnimation)
        {
            changeAnimation(ACTION_STANCE);
        }
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Tengu setPosition(Vector2 position)
    {
        this.position = position;
        return this;
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

    public Vector2 getSpeed()
    {
        return speed;
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
            speed = Vector2.Zero;
        } else if (animation == ACTION_RUN)
        {
            isOnBlockedAnimation = false;
            currentAction = ACTION_RUN;
            currentAnimation = characterRunAnimation;
            speed = new Vector2(RUN_SPEED, speed.y);
        } else if (animation == ACTION_JUMP)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_JUMP;
            currentAnimation = characterJumpAnimation;
            isJumping = true;
            speed = new Vector2(speed.x, JUMP_SPEED);
        } else if (animation == ACTION_FALL)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_FALL;
            currentAnimation = characterJumpAnimation;
            isFalling = true;
            speed = new Vector2(speed.x, -FALL_SPEED);
        }
    }
}
