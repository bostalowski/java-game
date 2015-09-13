package com.mygdx.animations.character;

import com.mygdx.character.Tengu;

public class CharacterSlideAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 3;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_slide.png";
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

    public boolean isAnimationFinished()
    {
        return animation.isAnimationFinished(elapsedTime);
    }
}
