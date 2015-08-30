package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.mygdx.character.Tengu;

public class CharacterStanceAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_stance.png";
    public static final float ANIMATION_DURATION= 1f;

    public CharacterStanceAnimation(Tengu tengu)
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
        super.update(true);
    }

    public void render()
    {
        super.render();
    }

    public void reset()
    {
        elapsedTime = 0;
    }

    public boolean isAnimationFinished() {
        return false;
    }
}
