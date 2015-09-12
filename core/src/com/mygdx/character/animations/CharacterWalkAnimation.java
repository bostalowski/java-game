package com.mygdx.character.animations;

import com.mygdx.character.Tengu;

public class CharacterWalkAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_walk.png";
    public static final float ANIMATION_DURATION = 0.5f;

    public CharacterWalkAnimation(Tengu tengu)
    {
        super(118, 133, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION);
    }

    @Override
    public void update()
    {
        this.update(false);
    }

    @Override
    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }
}
