package com.mygdx.character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
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

    public static final int RUN_SPEED = 200;
    public static final int JUMP_SPEED = 200;

    private int currentAction;


    private Vector2 position;
    private Vector2 speed;
    private Vector2 direction;
    private Vector2 lastDirection;

    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    boolean isJumping = false;

    public Tengu()
    {
        this.currentAction = ACTION_STANCE;
        this.position = Vector2.Zero;
        this.speed = Vector2.Zero;
        this.direction = new Vector2(1,0);
        this.lastDirection = direction;

        characterStanceAnimation = new CharacterStanceAnimation(this);
        characterRunAnimation = new CharacterRunAnimation(this);
        characterJumpAnimation = new CharacterJumpAnimation(this);
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

    @Override
    public void render()
    {
        if(isJumping && characterJumpAnimation.isAnimationFinished()) {
            isJumping = false;
        }
        this.handleKeyboard();

        if(currentAction == ACTION_STANCE) {
            characterStanceAnimation.render();
        } else if(currentAction == ACTION_RUN) {
            characterRunAnimation.render();
        } else if(currentAction == ACTION_JUMP) {
            characterJumpAnimation.render();
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
        speed = Vector2.Zero;
        lastDirection = direction;

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
            if(!isJumping)
            {
                currentAction = ACTION_RUN;
            }
            direction = new Vector2(1, direction.y);
            speed = new Vector2(RUN_SPEED, 0);
            position = new Vector2(position.x + (direction.x * speed.x) * Gdx.graphics.getDeltaTime(), position.y + (direction.y * speed.y) * Gdx.graphics.getDeltaTime());
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
        {
            if(!isJumping)
            {
                currentAction = ACTION_RUN;
            }
            direction = new Vector2(-1, direction.y);
            speed = new Vector2(RUN_SPEED, 0);
            position = new Vector2(position.x + (direction.x * speed.x) * Gdx.graphics.getDeltaTime(), position.y + (direction.y * speed.y) * Gdx.graphics.getDeltaTime());
            noKeyPressed = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping)
        {
            isJumping = true;
            currentAction = ACTION_JUMP;
            speed = new Vector2(RUN_SPEED, JUMP_SPEED);
            characterJumpAnimation.start();
        }

        if(noKeyPressed && !isJumping)
        {
            currentAction = ACTION_STANCE;
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
}
