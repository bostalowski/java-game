package com.japaleno.animations.character;

import com.japaleno.character.Tengu;

public class CharacterFallAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 4;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_fall.png";
    public static final float ANIMATION_DURATION = 0.3f;

    public CharacterFallAnimation(Tengu tengu)
    {
        super(130, 134, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION, true);
    }

    public boolean isAnimationFinished()
    {
        return false;
    }
}
