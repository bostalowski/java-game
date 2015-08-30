package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.character.Tengu;

public class CharacterJumpAnimation extends CharacterAnimation
{
    public static final int FRAME_COLS = 6;
    public static final int FRAME_ROWS = 1;
    public static final String SPRITE_FILENAME = "spritesheet_tengu_jump.png";
    public static final float ANIMATION_DURATION = 0.8f;

    public CharacterJumpAnimation(Tengu tengu)
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
