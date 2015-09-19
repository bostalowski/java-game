package com.japaleno.animations.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.japaleno.character.Tengu;

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

    protected int previousKeyFrameIndex;

    public AbstractCharacterAnimation(int frameWidth, int frameHeight, Tengu tengu)
    {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.tengu = tengu;
        this.previousKeyFrameIndex = -1;
    }

    public void create(String fileName, int frameCols, int frameRows, float animationDuration, boolean loop)
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
        if(loop) {
            this.animation.setPlayMode(Animation.PlayMode.LOOP);
        }
        this.elapsedTime = 0.0f;
    }

    public void update()
    {
        this.previousKeyFrameIndex = this.animation.getKeyFrameIndex(this.elapsedTime);

        this.elapsedTime += Gdx.graphics.getDeltaTime();

        this.actualFrame = this.animation.getKeyFrame(this.elapsedTime);
        this.actualFrame.setRegion(this.actualFrame, 0, 0, this.frameWidth, this.frameHeight);
    }

    public void render(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(this.actualFrame, this.tengu.getPosition().x, this.tengu.getPosition().y, this.frameWidth / 2, this.frameHeight / 2, this.frameWidth, this.frameHeight, this.tengu.getScale(), 1, 0/*, actualFrame.getRegionX(), actualFrame.getRegionY(), actualFrame.getRegionWidth(), actualFrame.getRegionHeight(), false, false*/);
    }

    public void reset()
    {
        this.elapsedTime = 0;
        this.previousKeyFrameIndex = -1;
    }

    public boolean isAnimationFinished()
    {
        return this.animation.isAnimationFinished(this.elapsedTime);
    }

    public float getElapsedTime()
    {
        return this.elapsedTime;
    }

    public float getWidth()
    {
        return this.frameWidth;
    }

    public float getHeight()
    {
        return this.frameHeight;
    }

    public int getKeyFrameIndex()
    {
        return this.animation.getKeyFrameIndex(this.elapsedTime);
    }

    public int getPreviousKeyFrameIndex()
    {
        return this.previousKeyFrameIndex;
    }

    public void setAnimationDuration(float animationDuration, int frameCols, int frameRows)
    {
        //System.out.println(animationDuration);
        //this.animation.setFrameDuration(animationDuration/(frameCols * frameRows));
    }
}
