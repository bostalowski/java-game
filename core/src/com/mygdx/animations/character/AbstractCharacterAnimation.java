package com.mygdx.animations.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.character.Tengu;

public abstract class AbstractCharacterAnimation
{
    protected int frameWidth = 0;
    protected int frameHeight = 0;

    protected Texture texture;
    protected Animation animation;
    protected TextureRegion actualFrame;
    protected TextureRegion frames[];

    protected float elapsedTime;

    protected Tengu tengu;

    public AbstractCharacterAnimation(int frameWidth, int frameHeight, Tengu tengu)
    {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.tengu = tengu;
    }

    public void create(String fileName, int frameCols, int frameRows, float animationDuration)
    {
        // Initialisation
        this.texture = new Texture(Gdx.files.internal(fileName));

        //map sprites into one dimensional array
        TextureRegion[][] tmp = TextureRegion.split(this.texture, this.texture.getWidth()/frameCols, this.texture.getHeight()/frameRows);
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
    }

    public abstract void update();

    public void update(boolean loop)
    {
        this.elapsedTime += Gdx.graphics.getDeltaTime();

        this.actualFrame = this.animation.getKeyFrame(this.elapsedTime, loop);
        this.actualFrame.setRegion(this.actualFrame, 0, 0, this.frameWidth, this.frameHeight);
    }

    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(this.actualFrame, this.tengu.getPosition().x, this.tengu.getPosition().y, this.frameWidth / 2, this.frameHeight / 2, this.frameWidth, this.frameHeight, this.tengu.getScale(), 1, 0/*, actualFrame.getRegionX(), actualFrame.getRegionY(), actualFrame.getRegionWidth(), actualFrame.getRegionHeight(), false, false*/);
    }

    public void reset()
    {
        this.elapsedTime = 0;
    }

    public abstract boolean isAnimationFinished();

    public float getElapsedTime()
    {
        return elapsedTime;
    }

    public float getWidth()
    {
        return frameWidth;
    }

    public float getHeight()
    {
        return frameHeight;
    }

    public boolean isLastFrame()
    {
        return animation.getKeyFrameIndex(elapsedTime) == frames.length - 1;
    }

    public void setAnimationDuration(float animationDuration, int frameCols, int frameRows)
    {
        //System.out.println(animationDuration);
        //this.animation.setFrameDuration(animationDuration/(frameCols * frameRows));
    }
}
