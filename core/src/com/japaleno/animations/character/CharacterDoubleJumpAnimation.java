package com.japaleno.animations.character;

import com.japaleno.character.Tengu;

public class CharacterDoubleJumpAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 8;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_doublejump.png";
    public static final float ANIMATION_DURATION = 0.4f;

    public CharacterDoubleJumpAnimation(Tengu tengu)
    {
        super(136, 123, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION, false);
    }
}
