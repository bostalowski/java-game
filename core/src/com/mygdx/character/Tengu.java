package com.mygdx.character;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private float positionX;
    private float positionY;
    private float speed;
    private int currentAction;
    private Direction direction;

    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    public Tengu()
    {
        this.positionX = 0;
        this.positionY = 0;
        this.speed = 200f;
        this.currentAction = ACTION_STANCE;
        this.direction = Direction.RIGHT;

        characterStanceAnimation = new CharacterStanceAnimation();
        characterRunAnimation = new CharacterRunAnimation();
    }

    @Override
    public void create()
    {
        characterStanceAnimation.create();
        characterRunAnimation.create();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render()
    {
        this.handleKeyboard();

        if(currentAction == ACTION_STANCE) {
            characterStanceAnimation.setPosition(new Point2D.Float(positionX, positionY));
            characterStanceAnimation.render();
        } else if(currentAction == ACTION_RUN) {
            characterRunAnimation.setPosition(new Point2D.Float(positionX, positionY));
            characterRunAnimation.setDirection(direction);
            characterRunAnimation.render();
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
            currentAction = ACTION_JUMP;
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
            currentAction = ACTION_STANCE;
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
        {
            currentAction = ACTION_RUN;
            positionX = positionX + (Gdx.graphics.getDeltaTime() * speed);
            direction = Direction.RIGHT;
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
        {
            currentAction = ACTION_RUN;
            positionX = positionX - (Gdx.graphics.getDeltaTime() * speed);
            direction = Direction.LEFT;
            noKeyPressed = false;
        }

        if(noKeyPressed)
        {
            currentAction = ACTION_STANCE;
        }
    }
}
