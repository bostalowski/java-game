package com.japaleno.animations.character;

import com.japaleno.character.Tengu;


public class CharacterRunAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_run.png";
    public static final float ANIMATION_DURATION = 0.5f;

    public CharacterRunAnimation(Tengu tengu)
    {
        super(128, 133, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION, true);
    }
}
