package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.character.Tengu;

public class CharacterJumpAnimation extends CharacterAnimation implements ApplicationListener
{
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 1;
    private static final String SPRITE_FILENAME = "spritesheet_tengu_jump.png";

    public CharacterJumpAnimation(Tengu tengu)
    {
        super(175, 175, 0.5f, tengu);
    }

    @Override
    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS);
    }

    @Override
    public void resize(int width, int height) {}

    public void update()
    {
        if(elapsedTime < animationSpeed/2)
        {
            tengu.setDirection(new Vector2(tengu.getDirection().x, 1));
        } else {
            tengu.setDirection(new Vector2(tengu.getDirection().x, -1));
        }
    }

    @Override
    public void render()
    {
        //System.out.println(tengu.getPosition().y);
        super.render(false);
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
