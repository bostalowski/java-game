package com.japaleno.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.japaleno.animations.effects.BaseEffectAnimation;

public class EffectHandler
{
    private static Array BackgroundEffects = new Array();
    private static Array ForegroundEffects = new Array();

    public static void RemoveFinishedEffects(float deltaTime)
    {
        RemoveBackgroundFinishedEffects(deltaTime);
        RemoveForeGroundFinishedEffects(deltaTime);
    }

    public static void RemoveBackgroundFinishedEffects(float deltaTime)
    {
        for(int i=0; i<BackgroundEffects.size; i++) {
            BaseEffectAnimation baseEffectAnimation = (BaseEffectAnimation) BackgroundEffects.get(i);
            if(baseEffectAnimation.isAnimationFinished(deltaTime)) {
                BackgroundEffects.removeIndex(i);
            }
        }
    }

    public static void RemoveForeGroundFinishedEffects(float deltaTime)
    {
        for(int i=0; i<ForegroundEffects.size; i++) {
            BaseEffectAnimation baseEffectAnimation = (BaseEffectAnimation) ForegroundEffects.get(i);
            if(baseEffectAnimation.isAnimationFinished(deltaTime)) {
                ForegroundEffects.removeIndex(i);
            }
        }
    }

    public static void RenderBackgroundEffects(SpriteBatch spriteBatch)
    {
        for(int i=0; i<BackgroundEffects.size; i++) {
            BaseEffectAnimation baseEffectAnimation = (BaseEffectAnimation) BackgroundEffects.get(i);
            baseEffectAnimation.render(spriteBatch);
        }
    }

    public static void RenderForegroundEffects(SpriteBatch spriteBatch)
    {
        for(int i=0; i<ForegroundEffects.size; i++) {
            BaseEffectAnimation baseEffectAnimation = (BaseEffectAnimation) ForegroundEffects.get(i);
            baseEffectAnimation.render(spriteBatch);
        }
    }

    public static void AddJumpEffect(float direction, Vector2 position)
    {
        BaseEffectAnimation baseEffectAnimation = new BaseEffectAnimation("sprites/effects/spritesheet_tengu_dust_run.png", 5, 1, 0.5f, false).setScale(direction);
        baseEffectAnimation.setPosition(new Vector2(baseEffectAnimation.getScale() < 0 ? position.x - baseEffectAnimation.getFrameWidth() : position.x, position.y));
        ForegroundEffects.add(baseEffectAnimation);
    }

    public static void AddRunEffect(float direction, Vector2 position)
    {
        BaseEffectAnimation baseEffectAnimation = new BaseEffectAnimation("sprites/effects/spritesheet_tengu_dust_run.png", 5, 1, 0.5f, false).setScale(direction).setPosition(position);
        ForegroundEffects.add(baseEffectAnimation);
    }
}
