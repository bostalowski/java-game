package com.mygdx.character.animations;

import com.mygdx.character.Tengu;

public class CharacterDoubleJumpAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 8;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_doublejump.png";
    public static final float ANIMATION_DURATION = 0.6f;

    public CharacterDoubleJumpAnimation(Tengu tengu)
    {
        super(175, 175, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION);
    }

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
    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }
}
