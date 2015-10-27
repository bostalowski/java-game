package com.japaleno.animations.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class BaseEffectAnimation
{
    protected Texture texture;
    protected Animation animation;
    protected TextureRegion actualFrame;
    protected TextureRegion frames[];
    protected float elapsedTime;
    protected Vector2 position;
    //Used for direction
    protected float scale;
    protected boolean loop;
    protected int frameWidth;
    protected int frameHeight;

    public BaseEffectAnimation(String fileName, int frameCols, int frameRows, float animationDuration, boolean loop)
    {
        // Initialisation
        this.texture = new Texture(Gdx.files.internal(fileName));

        this.frameWidth = this.texture.getWidth() / frameCols;
        this.frameHeight = this.texture.getHeight() / frameRows;

        //map sprites into one dimensional array
        TextureRegion[][] tmp = TextureRegion.split(this.texture, this.frameWidth, this.frameHeight);
        this.frames = new TextureRegion[frameCols * frameRows];
        int index = 0;

        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                this.frames[index++] = tmp[i][j];
            }
        }

        //set animation speed
        this.animation = new Animation(animationDuration/(frameCols * frameRows), this.frames);
        this.elapsedTime = 0.0f;
        this.position = new Vector2(0, 0);
        this.scale = 1;
        this.loop = loop;
    }

    public void render(SpriteBatch spriteBatch)
    {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        this.actualFrame = this.animation.getKeyFrame(this.elapsedTime, this.loop);
        this.actualFrame.setRegion(this.actualFrame, 0, 0, this.frameWidth, this.frameHeight);

        spriteBatch.draw(this.actualFrame,
                this.position.x,
                this.position.y,
                this.frameWidth / 2,
                this.frameHeight / 2,
                this.frameWidth,
                this.frameHeight,
                this.scale, //scaleX
                1, //scaleY
                0 ); //rotation
    }

    public boolean isAnimationFinished(float elapsedTime)
    {
        return this.animation.isAnimationFinished(this.elapsedTime + elapsedTime);
    }

    public BaseEffectAnimation setPosition(Vector2 position)
    {
        this.position = position.cpy();
        return this;
    }

    public BaseEffectAnimation setScale(float scale)
    {
        this.scale = scale;
        return this;
    }

    public float getScale()
    {
        return this.scale;
    }

    public float getFrameWidth()
    {
        return this.frameWidth;
    }

    public float getFrameHeight()
    {
        return this.frameHeight;
    }
}
