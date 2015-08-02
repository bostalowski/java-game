package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.character.Tengu;

public class CharacterJumpAnimation extends CharacterAnimation implements ApplicationListener
{
    public static final int FRAME_COLS = 4;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_jump.png";
    public static final float ANIMATION_DURATION = 0.5f;

    public CharacterJumpAnimation(Tengu tengu)
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

    public void update()
    {
        super.update(false);
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
    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }

    @Override
    public void reset()
    {
        elapsedTime = 0f;
        tengu.setDirection(new Vector2(tengu.getDirection().x, 0));
    }

    public void start()
    {
        elapsedTime = 0f;
    }
}
