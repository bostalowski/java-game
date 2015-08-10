package com.mygdx.character;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.character.animations.CharacterAnimation;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;
import com.mygdx.game.MyGdxGame;
import com.sun.javafx.scene.traversal.Direction;

import java.awt.geom.Point2D;

public class Tengu extends ApplicationAdapter implements InputProcessor
{
    public static final int ACTION_STANCE = 0;
    public static final int ACTION_JUMP = 1;
    public static final int ACTION_RUN = 2;
    public static final int ACTION_FALL = 3;

    public static final float SPEED_RUN = 400f;
    public static final float SPEED_JUMP = 2f;
    public static final float SPEED_FALL = 400;

    private int currentAction;

    private Vector2 position;
    private Vector2 speed;
    private Vector2 direction;

    private Body body;

    private CharacterAnimation currentAnimation;
    private CharacterStanceAnimation characterStanceAnimation;
    private CharacterJumpAnimation characterJumpAnimation;
    private CharacterRunAnimation characterRunAnimation;

    boolean isOnBlockedAnimation = false;

    public Tengu()
    {
        this.currentAction = ACTION_STANCE;
        this.position = new Vector2(400,0);
        this.speed = Vector2.Zero;
        this.direction = new Vector2(1,0);

        characterStanceAnimation = new CharacterStanceAnimation(this);
        characterRunAnimation = new CharacterRunAnimation(this);
        characterJumpAnimation = new CharacterJumpAnimation(this);

        this.currentAnimation = characterStanceAnimation;
    }

    public void create(World world)
    {
        characterStanceAnimation.create();
        characterRunAnimation.create();
        characterJumpAnimation.create();
        createBody(world);

        Gdx.input.setInputProcessor(this);
    }

    public void update()
    {
        //handleKeyboard();
        position = new Vector2(MyGdxGame.MetersToPixels(body.getPosition().x) - (getWidth()/2), MyGdxGame.MetersToPixels(body.getPosition().y) - (getHeight()/2));
        currentAnimation.update();

        /*if(isOnBlockedAnimation && currentAnimation.isAnimationFinished()) {
            isOnBlockedAnimation = false;
        }

        //decrease jump speed
        if(currentAction == ACTION_JUMP) {
            float decreaseSpeed = (JUMP_SPEED / (CharacterJumpAnimation.ANIMATION_DURATION * 100)) * (currentAnimation.getElapsedTime() * 100);
            if(JUMP_SPEED - decreaseSpeed < 0) {
                decreaseSpeed = JUMP_SPEED;
            }
            speed = new Vector2(speed.x, JUMP_SPEED - decreaseSpeed);
        }

        //if on air and falling
        if(position.y > 0 && currentAction != ACTION_JUMP) {
            changeAnimation(ACTION_FALL);
            speed = new Vector2(speed.x, FALL_SPEED);
            direction = new Vector2(direction.x, -1);
        }

        Vector2 newPosition = new Vector2(position.x + (direction.x * speed.x) * Gdx.graphics.getDeltaTime(), position.y + (direction.y * speed.y) * Gdx.graphics.getDeltaTime());
        if(newPosition.y < 0) {
            newPosition.y = 0;
            changeAnimation(ACTION_STANCE);
        }

        position = newPosition;*/
    }

    public void render()
    {
        currentAnimation.render();
    }

    public void dispose() {}

    public void handleKeyboard()
    {
        boolean noKeyPressed = true;
        if(Gdx.input.isKeyPressed(Input.Keys.UP))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
        {
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }

            Vector2 force = new Vector2(SPEED_RUN, 0);
            Vector2 point = body.getWorldPoint(body.getWorldCenter());
            //body.applyForce(force ,point, true);
            body.applyLinearImpulse(force, point, true);
            //body.applyForceToCenter(force, true);

            //speed = new Vector2(RUN_SPEED, speed.y);
            direction = new Vector2(1, direction.y);
            noKeyPressed = false;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT) )
        {
            if(!isOnBlockedAnimation)
            {
                changeAnimation(ACTION_RUN);
            }
            speed = new Vector2(SPEED_RUN, speed.y);
            direction = new Vector2(-1, direction.y);
            noKeyPressed = false;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isOnBlockedAnimation)
        {
            changeAnimation(ACTION_JUMP);
            speed = new Vector2(speed.x, SPEED_JUMP);
            direction = new Vector2(direction.x, 1);
            characterJumpAnimation.start();
        }

        if(noKeyPressed && !isOnBlockedAnimation)
        {
            if(currentAction != ACTION_FALL) {
                speed = Vector2.Zero;
            }
            changeAnimation(ACTION_STANCE);
        }
    }

    private void changeAnimation(int animation)
    {
        if(animation != currentAction)
        {
            currentAnimation.reset();
        }

        if(animation == ACTION_STANCE)
        {
            isOnBlockedAnimation = false;
            currentAction = ACTION_STANCE;
            currentAnimation = characterStanceAnimation;
        } else if (animation == ACTION_RUN)
        {
            isOnBlockedAnimation = false;
            currentAction = ACTION_RUN;
            currentAnimation = characterRunAnimation;
        } else if (animation == ACTION_JUMP)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_JUMP;
            currentAnimation = characterJumpAnimation;
        } else if (animation == ACTION_FALL)
        {
            isOnBlockedAnimation = true;
            currentAction = ACTION_FALL;
            currentAnimation = characterJumpAnimation;
        }
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public Vector2 getDirection()
    {
        return direction;
    }

    public Tengu setDirection(Vector2 direction)
    {
        this.direction = direction;
        return this;
    }

    public void createBody(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(MyGdxGame.PixelsToMeters(position.x + getWidth()/2f), MyGdxGame.PixelsToMeters(position.y + getHeight()/2f));

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(MyGdxGame.PixelsToMeters(getWidth()/2f), MyGdxGame.PixelsToMeters(getHeight()/2f), new Vector2(0, 0), 0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public SpriteBatch getSpriteBatch()
    {
        return currentAnimation.getSpriteBatch();
    }

    private float getWidth()
    {
        return currentAnimation.getWidth();
    }

    private float getHeight()
    {
        return currentAnimation.getHeight();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            System.out.println("RIGHT DOWN");
            //body.applyLinearImpulse(force, point, true);
            body.applyForceToCenter(SPEED_RUN, 0, true);
            //body.setLinearVelocity(SPEED_RUN, 0f);
            direction = new Vector2(1, direction.y);
            changeAnimation(ACTION_RUN);
        }

        if(keycode == Input.Keys.LEFT) {
            System.out.println("LEFT DOWN");
            body.applyForceToCenter(-SPEED_RUN, 0, true);
            direction = new Vector2(-1, direction.y);
            changeAnimation(ACTION_RUN);
        }

        if(keycode == Input.Keys.UP) {
            //body.applyLinearImpulse(0, SPEED_JUMP, 175f/2f / 100f, 175f/2f / 100f, true);
            //body.applyForceToCenter(0f, SPEED_JUMP, true);
            
            Vector2 force  = new Vector2(0, SPEED_JUMP);
            float x = MyGdxGame.PixelsToMeters(getWidth() / 2);
            body.applyLinearImpulse(force.x, force.y, x, 0, true);
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.RIGHT) {
            System.out.println("RIGHT UP");
            body.setLinearVelocity(0f, 0f);
            changeAnimation(ACTION_STANCE);
        }

        if(keycode == Input.Keys.LEFT) {
            System.out.println("LEFT DOWN");
            body.setLinearVelocity(0f, 0f);
            changeAnimation(ACTION_STANCE);
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
