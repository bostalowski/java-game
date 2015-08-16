package com.mygdx.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.character.animations.CharacterAnimation;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;
import com.mygdx.tools.Physics;

public class Tengu extends Physics
{
    private static final int ACTION_STANCE = 0;
    private static final int ACTION_RUN = 1;
    private static final int ACTION_JUMP = 2;

    public static final float SPEED_RUN_MAX_VELOCITY = 10f;
    public static final float SPEED_RUN_ACCELERATION = 0.2f;
    public static final float SPEED_JUMP_ACCELERATION = 20f;

    private static final float DIRECTION_LEFT = -1;
    private static final float DIRECTION_RIGHT = 1;
    private float direction;

    private int currentAction;
    private CharacterAnimation currentAnimation;
    private CharacterAnimation characterStanceAnimation;
    private CharacterAnimation characterRunAnimation;
    private CharacterAnimation characterJumpAnimation;

    private boolean isJumping;

    public Tengu(Vector2 position, float speed, float direction)
    {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.velocity.setLength(speed);
        this.velocity.setAngle(direction);
        this.friction = 0.1f;
        this.direction = DIRECTION_RIGHT;
        this.isJumping = false;

        this.characterStanceAnimation = new CharacterStanceAnimation(this);
        this.characterRunAnimation = new CharacterRunAnimation(this);
        this.characterJumpAnimation = new CharacterJumpAnimation(this);

        this.currentAction = ACTION_STANCE;
        this.currentAnimation = this.characterStanceAnimation;
    }

    public void update(float deltaTime)
    {
        if(this.position.y == 0) {
            this.friction = 0.1f;
            isJumping = false;
        }

        this.handleKeys();

        //add deltatime to position to not depend on FPS
        //this.velocity.x = this.velocity.x * deltaTime;
        //this.velocity.y = this.velocity.y * deltaTime;

        this.position.add(this.velocity);

        if(position.x > Gdx.graphics.getWidth()) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = Gdx.graphics.getWidth();
        }

        this.currentAnimation.update();
    }

    public void render()
    {
        this.currentAnimation.render();
    }

    private void handleKeys()
    {
        boolean isAnyKeyPressed = false;

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(this.velocity.x > -SPEED_RUN_MAX_VELOCITY) {
                Vector2 force = new Vector2(-SPEED_RUN_ACCELERATION, 0);

                if(this.velocity.x > 0) {
                    force.x = force.x + (-this.velocity.x * this.friction);
                }
                this.applyForce(force);
            }
            isAnyKeyPressed = true;
            this.direction = DIRECTION_LEFT;
            if(!isJumping) {
                this.changeAnimation(ACTION_RUN);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(this.velocity.x < SPEED_RUN_MAX_VELOCITY) {
                Vector2 force = new Vector2(SPEED_RUN_ACCELERATION, 0);

                if(this.velocity.x < 0) {
                    force.x = force.x + (-this.velocity.x * this.friction);
                }
                this.applyForce(force);
            }
            isAnyKeyPressed = true;
            this.direction = DIRECTION_RIGHT;
            if(!isJumping) {
                this.changeAnimation(ACTION_RUN);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            this.isJumping = true;
            this.friction = 0;
            Vector2 force = new Vector2(0, SPEED_JUMP_ACCELERATION);
            this.applyForce(force);
            this.changeAnimation(ACTION_JUMP);
        }

        if(!isAnyKeyPressed && !isJumping) {
            if(Math.round(this.velocity.x * 100) == 0) {
                this.velocity.x = 0;
                changeAnimation(ACTION_STANCE);
            } else {
                float inertieForceX = -this.velocity.x * this.friction;
                this.applyForce(new Vector2(inertieForceX, 0));
            }
        }
    }

    private void changeAnimation(int action)
    {
        if(this.currentAction != action) {
            this.currentAction = action;
            switch (action) {
                case ACTION_STANCE:
                    this.currentAnimation = this.characterStanceAnimation;
                    break;
                case ACTION_RUN:
                    this.currentAnimation = this.characterRunAnimation;
                    break;
                case ACTION_JUMP:
                    this.currentAnimation = this.characterJumpAnimation;
                    break;
                default:
                    this.currentAnimation = this.characterStanceAnimation;
            }
            this.currentAnimation.reset();
        }
    }

    /**
     * Used to draw sprite
     * @return sprite direction (scale)
     */
    public float getScale()
    {
        return this.direction;
    }
}
