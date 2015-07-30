package com.mygdx.character.animations;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.awt.geom.Point2D;

public class CharacterJumpAnimation implements ApplicationListener
{
    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 1;

    private static final String SPRITE_FILENAME = "spritesheet_tengu_jump.png";

    private int frameWidth = 175;
    private int frameHeight = 175;

    private float animationSpeed = 0.7f;

    private SpriteBatch spriteBatch;
    private Texture texture;
    private Animation animation;
    private TextureRegion actualFrame;
    private TextureRegion frames[];

    private float temps;

    private Point2D position;

    @Override
    public void create()
    {
        // Initialisation
        spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal(SPRITE_FILENAME));

        //map sprites into one dimensional array
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/FRAME_COLS, texture.getHeight()/FRAME_ROWS);
        frames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;

        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                frames[index++] = tmp[i][j];
            }
        }

        //set animation speed
        animation = new Animation(animationSpeed/(FRAME_COLS * FRAME_ROWS), frames);
        temps = 0.0f;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void render()
    {
        temps += Gdx.graphics.getDeltaTime();
        actualFrame = animation.getKeyFrame(temps, true);
        actualFrame.setRegion(actualFrame, 0, 0, frameWidth, frameHeight);
        spriteBatch.begin();
        spriteBatch.draw(actualFrame, (float) position.getX(), (float) position.getY());
        spriteBatch.end();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {}

    public void setPosition(Point2D position)
    {
        this.position = position;
    }

    public Point2D getPosition()
    {
        return this.position;
    }
}
