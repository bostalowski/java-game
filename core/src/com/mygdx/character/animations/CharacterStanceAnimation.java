package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.character.Tengu;

public class CharacterStanceAnimation extends CharacterAnimation implements ApplicationListener
{
    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 1;
    private static final String SPRITE_FILENAME = "spritesheet_tengu_stance.png";

    public CharacterStanceAnimation(Tengu tengu)
    {
        super(175, 175, 0.7f, tengu);
    }

    @Override
    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void update() {}

    @Override
    public void render()
    {
        super.render(true);
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
        elapsedTime = 0;
    }

    @Override
    public boolean isAnimationFinished() {
        return false;
    }
}
