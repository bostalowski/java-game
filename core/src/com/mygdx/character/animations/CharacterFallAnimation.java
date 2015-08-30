package com.mygdx.character.animations;

import com.mygdx.character.Tengu;

public class CharacterFallAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 4;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_fall.png";
    public static final float ANIMATION_DURATION = 0.3f;

    public CharacterFallAnimation(Tengu tengu)
    {
        super(175, 175, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION);
    }

    public void update()
    {
        this.update(true);
    }

    public boolean isAnimationFinished()
    {
        return false;
    }
}
