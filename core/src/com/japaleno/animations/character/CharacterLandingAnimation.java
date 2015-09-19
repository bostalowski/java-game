package com.japaleno.animations.character;

import com.japaleno.character.Tengu;

public class CharacterLandingAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 2;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_land.png";
    public static final float ANIMATION_DURATION = 0.2f;

    public CharacterLandingAnimation(Tengu tengu)
    {
        super(127, 126, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION, false);
    }
}
