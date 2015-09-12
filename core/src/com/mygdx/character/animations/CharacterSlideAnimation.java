package com.mygdx.character.animations;

import com.mygdx.character.Tengu;

public class CharacterSlideAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 3;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_slide.png";
    public static final float ANIMATION_DURATION = 0.4f;

    public CharacterSlideAnimation(Tengu tengu)
    {
        super(124, 130, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION);
    }

    public void update()
    {
        super.update(false);
    }

    public void render()
    {
        super.render();
    }

    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }
}
