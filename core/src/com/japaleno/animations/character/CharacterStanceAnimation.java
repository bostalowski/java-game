package com.japaleno.animations.character;

import com.japaleno.character.Tengu;

public class CharacterStanceAnimation extends AbstractCharacterAnimation
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "sprites/tengu-animations/spritesheet_tengu_stance.png";
    public static final float ANIMATION_DURATION= 1f;

    public CharacterStanceAnimation(Tengu tengu)
    {
        super(119, 133, tengu);
        this.create();
    }

    public void create()
    {
        super.create(SPRITE_FILENAME, FRAME_COLS, FRAME_ROWS, ANIMATION_DURATION, true);
    }
}
