package com.mygdx.character.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.character.Tengu;

public abstract class CharacterAnimation
{
    protected int frameWidth = 0;
    protected int frameHeight = 0;

    protected float animationSpeed = 0f;

    protected SpriteBatch spriteBatch;
    protected Texture texture;
    protected Animation animation;
    protected TextureRegion actualFrame;
    protected TextureRegion frames[];

    protected float elapsedTime;

    protected Tengu tengu;

    public CharacterAnimation(int frameWidth, int frameHeight, float animationSpeed, Tengu tengu)
    {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.animationSpeed = animationSpeed;
        this.tengu = tengu;
    }

    public void create(String fileName, int frameCols, int frameRows)
    {
        // Initialisation
        spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal(fileName));

        //map sprites into one dimensional array
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/frameCols, texture.getHeight()/frameRows);
        frames = new TextureRegion[frameCols * frameRows];
        int index = 0;

        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameCols; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        //set animation speed
        animation = new Animation(animationSpeed/(frameCols * frameRows), frames);
        elapsedTime = 0.0f;
    }

    public abstract void update();

    public void update(boolean loop)
    {
        elapsedTime += Gdx.graphics.getDeltaTime();

        actualFrame = animation.getKeyFrame(elapsedTime, loop);
        actualFrame.setRegion(actualFrame, 0, 0, frameWidth, frameHeight);
    }

    public void render()
    {
        spriteBatch.begin();
        spriteBatch.draw(actualFrame, tengu.getPosition().x, tengu.getPosition().y, frameWidth/2, frameHeight/2, frameWidth, frameHeight, tengu.getDirection().x, 1, 0/*, actualFrame.getRegionX(), actualFrame.getRegionY(), actualFrame.getRegionWidth(), actualFrame.getRegionHeight(), false, false*/);
        spriteBatch.end();
    }

    public void renderFrame(int frameIndex)
    {
        TextureRegion[] tmp = animation.getKeyFrames();
        actualFrame = tmp[frameIndex];
        actualFrame.setRegion(actualFrame, 0, 0, frameWidth, frameHeight);

        spriteBatch.begin();
        spriteBatch.draw(actualFrame, tengu.getPosition().x, tengu.getPosition().y, frameWidth/2, frameHeight/2, frameWidth, frameHeight, tengu.getDirection().x, 1, 0/*, actualFrame.getRegionX(), actualFrame.getRegionY(), actualFrame.getRegionWidth(), actualFrame.getRegionHeight(), false, false*/);
        spriteBatch.end();
    }

    public abstract boolean isAnimationFinished();
    public abstract void reset();
}
