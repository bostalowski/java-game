package com.mygdx.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
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
    public static final float SPEED_RUN_ACCELERATION = 5f;
    public static final float SPEED_JUMP_ACCELERATION = 20f;
    public static final float SPEED_DASH_ACCELERATION = 20f;

    public static final float FRICTION_ON_AIR = 0;
    public static final float FRICTION_ON_GROUND = 1f;
    public static final float FRICTION_ON_PARACHUTE = 1f;

    private static final float DIRECTION_LEFT = -1;
    private static final float DIRECTION_RIGHT = 1;
    private float direction;

    private int currentAction;
    private CharacterAnimation currentAnimation;
    private CharacterAnimation characterStanceAnimation;
    private CharacterAnimation characterRunAnimation;
    private CharacterAnimation characterJumpAnimation;

    private boolean isOnGround,
            isJumping,
            hasDoubleJumped,
            isDashingLeft,
            isDashingRight;

    public static final float DASH_MAX_DISTANCE = 300f;
    private float elapsedDashDistance;

    public Tengu(Vector2 position, float speed, float direction)
    {
        this.position = position;
        this.velocity = new Vector2(0, 0);
        this.velocity.setLength(speed);
        this.velocity.setAngle(direction);
        this.friction = new Vector2(0, 0);
        this.direction = DIRECTION_RIGHT;
        this.isOnGround = true;
        this.isJumping = false;
        this.hasDoubleJumped = false;
        this.isDashingLeft = false;
        this.isDashingRight = false;
        this.elapsedDashDistance = 0;

        this.characterStanceAnimation = new CharacterStanceAnimation(this);
        this.characterRunAnimation = new CharacterRunAnimation(this);
        this.characterJumpAnimation = new CharacterJumpAnimation(this);

        this.currentAction = ACTION_STANCE;
        this.currentAnimation = this.characterStanceAnimation;
    }

    public void update(float deltaTime)
    {
        if(isOnGround) {
            this.friction = new Vector2(FRICTION_ON_GROUND, 0);
            this.isJumping = false;
            this.hasDoubleJumped = false;
        } else {
            this.friction = new Vector2(FRICTION_ON_AIR, 0);
        }

        if(this.velocity.len() > this.friction.len()) {
            this.velocity.setLength(this.velocity.len() - this.friction.len());
        } else {
            this.velocity = new Vector2(0, 0);
        }

        if((this.isDashingLeft || this.isDashingRight) && this.elapsedDashDistance < DASH_MAX_DISTANCE) {
            float direction = isDashingLeft ? -1 : 1;
            if(this.elapsedDashDistance + SPEED_DASH_ACCELERATION > DASH_MAX_DISTANCE) {
                this.velocity = new Vector2((DASH_MAX_DISTANCE - this.elapsedDashDistance) * direction, 0);
            } else {
                this.velocity = new Vector2(SPEED_DASH_ACCELERATION * direction, 0);
            }
            this.elapsedDashDistance += Math.abs(this.velocity.x);
        }

        if(this.elapsedDashDistance >= DASH_MAX_DISTANCE && (this.isDashingLeft || this.isDashingRight)) {
            this.isDashingLeft = false;
            this.isDashingRight = false;
            this.velocity = new Vector2(0, 0);
        }

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

    public void handleKeys()
    {
        boolean isAnyKeyPressed = false;

        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            //enable debug
            boolean debug = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(this.velocity.x > -SPEED_RUN_MAX_VELOCITY) {
                if(this.velocity.x - SPEED_RUN_ACCELERATION < -SPEED_RUN_MAX_VELOCITY) {
                    Vector2 force = new Vector2(-(SPEED_RUN_MAX_VELOCITY + this.velocity.x), 0);
                    this.applyForce(force);
                } else {
                    Vector2 force = new Vector2(-SPEED_RUN_ACCELERATION, 0);
                    this.applyForce(force);
                }
            }
            isAnyKeyPressed = true;
            this.direction = DIRECTION_LEFT;
            if(!isJumping) {
                this.changeAnimation(ACTION_RUN);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(this.velocity.x < SPEED_RUN_MAX_VELOCITY) {
                if(this.velocity.x + SPEED_RUN_ACCELERATION > SPEED_RUN_MAX_VELOCITY) {
                    Vector2 force = new Vector2(SPEED_RUN_MAX_VELOCITY - this.velocity.x, 0);
                    this.applyForce(force);
                } else {
                    Vector2 force = new Vector2(SPEED_RUN_ACCELERATION, 0);
                    this.applyForce(force);
                }
            }
            isAnyKeyPressed = true;
            this.direction = DIRECTION_RIGHT;
            if(!isJumping) {
                this.changeAnimation(ACTION_RUN);
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && Math.signum(this.velocity.y) == -1) {
            this.friction = new Vector2(FRICTION_ON_PARACHUTE, 0);
            this.velocity = new Vector2(this.velocity.x, -2.5f);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !this.hasDoubleJumped) {
            if(this.isJumping) {
                this.hasDoubleJumped = true;
            }
            this.isJumping = true;
            this.isOnGround = false;
            //ajust force to avoid velocity
            Vector2 force = new Vector2(0, SPEED_JUMP_ACCELERATION - this.velocity.y);
            this.applyForce(force);
            this.changeAnimation(ACTION_JUMP);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.A) && !(this.isDashingLeft || this.isDashingRight)) {
            this.isDashingLeft = true;
            this.elapsedDashDistance = 0f;
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.E) && !(this.isDashingLeft || this.isDashingRight)) {
            this.isDashingRight = true;
            this.elapsedDashDistance = 0f;
        }

        if(!isAnyKeyPressed && !isJumping) {
            if(Math.round(this.velocity.x * 100) == 0) {
                this.velocity.x = 0;
                changeAnimation(ACTION_STANCE);
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

    public float getWidth()
    {
        return this.currentAnimation.getWidth();
    }

    public float getHeight()
    {
        return this.currentAnimation.getHeight();
    }

    public Tengu isOnGround(boolean isOnGround)
    {
        this.isOnGround = isOnGround;
        return this;
    }

    public Vector2 getFiction()
    {
        return this.friction;
    }

    public Rectangle getRectangle(Rectangle rectangle)
    {
        return rectangle.setX(this.position.x).setY(this.position.y).setWidth(this.getWidth()).setHeight(this.getHeight());
    }
}
