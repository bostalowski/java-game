package com.japaleno.character;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.japaleno.animations.character.*;
import com.japaleno.tools.EffectHandler;
import com.japaleno.tools.Physics;

public class Tengu extends Physics
{
    private static final int ACTION_STANCE = 0;
    private static final int ACTION_RUN = 1;
    private static final int ACTION_JUMP = 2;
    private static final int ACTION_DOUBLE_JUMP = 3;
    private static final int ACTION_FALL = 4;
    private static final int ACTION_SLIDE = 5;
    private static final int ACTION_WALK = 6;
    private static final int ACTION_LANDING = 7;

    public static final float SPEED_RUN_MAX_VELOCITY = 10f;
    public static final float SPEED_RUN_ACCELERATION = 2f;
    public static final float SPEED_JUMP_ACCELERATION = 20f;
    public static final float SPEED_DASH_ACCELERATION = 20f;
    public static final float SPEED_WALK_ACCELERATION = 1f;
    public static final float SPEED_WALK_MAX_VELOCITY = 5f;

    public static final float FRICTION_ON_AIR = 0.4f;
    public static final float FRICTION_ON_GROUND = 0.8f;
    public static final float FRICTION_ON_PARACHUTE = 1f;

    private static final float DIRECTION_LEFT = -1;
    private static final float DIRECTION_RIGHT = 1;
    private float direction;

    private int currentAction;
    private AbstractCharacterAnimation currentAnimation;
    private AbstractCharacterAnimation characterStanceAnimation;
    private AbstractCharacterAnimation characterRunAnimation;
    private AbstractCharacterAnimation characterJumpAnimation;
    private AbstractCharacterAnimation characterDoubleJumpAnimation;
    private AbstractCharacterAnimation characterFallAnimation;
    private AbstractCharacterAnimation characterSlideAnimation;
    private AbstractCharacterAnimation characterWalkAnimation;
    private AbstractCharacterAnimation characterLandingAnimation;

    public boolean isOnGround,
            isJumping,
            hasDoubleJumped,
            isDashingLeft,
            isDashingRight,
            isLanding,
            isAnyKeyPressed;

    public static final float DASH_MAX_DISTANCE = 300f;
    private float elapsedDashDistance;

    private Vector2 oldPosition,
            oldVelocity;

    public Tengu(Vector2 position, float speed, float direction)
    {
        this.position = position;
        this.oldPosition = new Vector2(position.x, position.y);
        this.velocity = new Vector2(0, 0);
        this.velocity.setLength(speed);
        this.velocity.setAngle(direction);
        this.oldVelocity = new Vector2(this.velocity.x, this.velocity.y);
        this.friction = new Vector2(0, 0);
        this.direction = DIRECTION_RIGHT;
        this.isOnGround = true;
        this.isJumping = false;
        this.hasDoubleJumped = false;
        this.isDashingLeft = false;
        this.isDashingRight = false;
        this.isLanding = false;
        this.elapsedDashDistance = 0;
        this.isAnyKeyPressed = false;

        this.characterStanceAnimation = new CharacterStanceAnimation(this);
        this.characterRunAnimation = new CharacterRunAnimation(this);
        this.characterJumpAnimation = new CharacterJumpAnimation(this);
        this.characterDoubleJumpAnimation = new CharacterDoubleJumpAnimation(this);
        this.characterFallAnimation = new CharacterFallAnimation(this);
        this.characterSlideAnimation = new CharacterSlideAnimation(this);
        this.characterWalkAnimation = new CharacterWalkAnimation(this);
        this.characterLandingAnimation = new CharacterLandingAnimation(this);

        this.currentAction = ACTION_STANCE;
        this.currentAnimation = this.characterStanceAnimation;
    }

    public void update()
    {
        if(this.isOnGround) {
            this.isJumping = false;
            this.hasDoubleJumped = false;
            this.friction = new Vector2(FRICTION_ON_GROUND, 0);

            if(this.isLanding && this.currentAction != ACTION_LANDING) {
                changeAnimation(ACTION_LANDING);
            }

            if(this.currentAction == ACTION_LANDING && this.currentAnimation.isAnimationFinished()) {
                this.isLanding = false;
            }
        } else {
            this.friction = new Vector2(FRICTION_ON_AIR, 0);
            //is falling ?
            if(this.velocity.y < 0) {
                changeAnimation(ACTION_FALL);
            }
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

        //round velocity
        this.velocity = new Vector2(Math.round(this.velocity.x * 100)/100f, Math.round(this.velocity.y * 100)/100f);
        this.position.add(this.velocity);

        if(!this.isAnyKeyPressed && !this.isJumping && this.isOnGround && !this.isLanding) {
            if(Math.abs(this.velocity.x) > 2) {
                changeAnimation(ACTION_SLIDE);
            }

            if(Math.round(this.velocity.x * 100) == 0) {
                this.velocity = new Vector2(0, this.velocity.y);
                changeAnimation(ACTION_STANCE);
            }
        }

        this.currentAnimation.update();
        this.isAnyKeyPressed = false;

        if(this.currentAction == ACTION_RUN && this.currentAnimation.getKeyFrameIndex() == 3 && this.currentAnimation.getPreviousKeyFrameIndex() != this.currentAnimation.getKeyFrameIndex()) {
            EffectHandler.AddRunEffect(this.getScale(), new Vector2(this.getScale() < 0 ? this.position.x + this.getWidth() : this.position.x, this.position.y));
        }
    }

    public void render(SpriteBatch spriteBatch)
    {
        this.currentAnimation.render(spriteBatch);
    }

    public void jump()
    {
        if(this.isOnGround && !this.isJumping && !this.hasDoubleJumped) {
            this.isJumping = true;
            changeAnimation(ACTION_JUMP);
        } else if((!this.isOnGround || this.isJumping) && !this.hasDoubleJumped) {
            this.isJumping = true;
            this.hasDoubleJumped = true;
            changeAnimation(ACTION_DOUBLE_JUMP);
        } else {
            return;
        }

        this.isAnyKeyPressed = true;
        this.isOnGround = false;
        Vector2 force = new Vector2(0, SPEED_JUMP_ACCELERATION - this.velocity.y);
        this.applyForce(force);

        //add smoke jump effect
        EffectHandler.AddJumpEffect(this.getScale(), new Vector2(this.getScale() < 0 ? this.position.x + this.getWidth() : this.position.x, this.position.y));
    }

    public void moveLeft()
    {
        int action;

        if(this.velocity.x > -SPEED_WALK_MAX_VELOCITY || (this.currentAction == ACTION_WALK && !this.currentAnimation.isAnimationFinished())) {
            action = ACTION_WALK;

            Vector2 force = new Vector2(-SPEED_WALK_ACCELERATION, 0);
            this.applyForce(force);
        } else {
            action = ACTION_RUN;

            if(this.velocity.x > -SPEED_RUN_MAX_VELOCITY) {
                if(this.velocity.x - SPEED_RUN_ACCELERATION < -SPEED_RUN_MAX_VELOCITY) {
                    Vector2 force = new Vector2(-(SPEED_RUN_MAX_VELOCITY + this.velocity.x), 0);
                    this.applyForce(force);
                } else {
                    Vector2 force = new Vector2(-SPEED_RUN_ACCELERATION, 0);
                    this.applyForce(force);
                }
            }
        }

        this.isAnyKeyPressed = true;
        this.direction = DIRECTION_LEFT;
        if(!this.isJumping && !this.isLanding) {
            changeAnimation(action);
        }
    }

    public void moveRight()
    {
        int action;

        if(this.velocity.x < SPEED_WALK_MAX_VELOCITY || (this.currentAction == ACTION_WALK && !this.currentAnimation.isAnimationFinished())) {
            action = ACTION_WALK;

            Vector2 force = new Vector2(SPEED_WALK_ACCELERATION, 0);
            this.applyForce(force);
        } else {
            action = ACTION_RUN;

            if(this.velocity.x < SPEED_RUN_MAX_VELOCITY) {
                if(this.velocity.x + SPEED_RUN_ACCELERATION > SPEED_RUN_MAX_VELOCITY) {
                    Vector2 force = new Vector2(SPEED_RUN_MAX_VELOCITY - this.velocity.x, 0);
                    this.applyForce(force);
                } else {
                    Vector2 force = new Vector2(SPEED_RUN_ACCELERATION, 0);
                    this.applyForce(force);
                }
            }
        }

        this.isAnyKeyPressed = true;
        this.direction = DIRECTION_RIGHT;
        if(!this.isJumping && !this.isLanding) {
            this.changeAnimation(action);
        }
    }

    public void deployParachute()
    {
        if(Math.signum(this.velocity.y) == -1) {
            this.friction = new Vector2(FRICTION_ON_PARACHUTE, 0);
            this.velocity = new Vector2(this.velocity.x, -2.5f);
        }
    }

    public void dashLeft()
    {
        if(!(this.isDashingLeft || this.isDashingRight)) {
            this.isDashingLeft = true;
            this.elapsedDashDistance = 0f;
        }
    }

    public void dashRight()
    {
        if(!(this.isDashingLeft || this.isDashingRight)) {
            this.isDashingRight = true;
            this.elapsedDashDistance = 0f;
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
                case ACTION_DOUBLE_JUMP:
                    this.currentAnimation = this.characterDoubleJumpAnimation;
                    break;
                case ACTION_FALL:
                    this.currentAnimation = this.characterFallAnimation;
                    break;
                case ACTION_SLIDE:
                    this.currentAnimation = this.characterSlideAnimation;
                    break;
                case ACTION_WALK:
                    this.currentAnimation = this.characterWalkAnimation;
                    break;
                case ACTION_LANDING:
                    this.currentAnimation = this.characterLandingAnimation;
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
        return 130;
    }

    public float getHeight()
    {
        //return this.currentAnimation.getHeight();
        return 130;
    }

    public Tengu setIsOnGround(boolean isOnGround)
    {
        this.isOnGround = isOnGround;

        if(isOnGround && (this.isJumping || this.currentAction == ACTION_FALL)) {
            this.isLanding = true;
        }

        return this;
    }

    public Rectangle getRectangle(Rectangle rectangle)
    {
        return rectangle.setX(this.position.x).setY(this.position.y).setWidth(this.getWidth()).setHeight(this.getHeight());
    }

    public Vector2 getOldPosition()
    {
        return this.oldPosition;
    }

    public Tengu setOldPosition(Vector2 oldPosition)
    {
        this.oldPosition = new Vector2(oldPosition.x, oldPosition.y);
        return this;
    }

    public Vector2 getOldVelocity()
    {
        return this.oldVelocity;
    }

    public Tengu setOldVelocity(Vector2 oldVelocity)
    {
        this.oldVelocity = new Vector2(oldVelocity.x, oldVelocity.y);
        return this;
    }
}
