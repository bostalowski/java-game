package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.mygdx.character.Tengu;


public class CharacterRunAnimation extends CharacterAnimation implements ApplicationListener
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_run.png";
    public static final float ANIMATION_DURATION = 0.7f;

    public CharacterRunAnimation(Tengu tengu)
    {
        super(175, 175, tengu);
    }

    @Override
    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void update()
    {
        super.update(true);
    }

    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    @Override
    public void reset()
    {
        elapsedTime = 0f;
    }

    @Override
    public boolean isAnimationFinished() {
        return false;
    }
}