package com.mygdx.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Physics
{
    protected Vector2 position,
            velocity;

    protected Vector2 friction;

    public Vector2 getPosition()
    {
        return this.position;
    }

    public Physics setPosition(Vector2 position)
    {
        this.position = position;
        return this;
    }

    public Vector2 getVelocity()
    {
        return this.velocity;
    }

    public Physics setVelocity(Vector2 velocity)
    {
        this.velocity = velocity;
        return this;
    }

    public float getSpeed()
    {
        return this.velocity.len();
    }

    public Physics setSpeed(float speed)
    {
        this.velocity.setLength(speed);
        return this;
    }

    public float getDirection()
    {
        return this.velocity.angle();
    }

    public Physics setDirection(float direction)
    {
        this.velocity.setAngle(direction);
        return this;
    }

    public Vector2 getFriction()
    {
        return this.friction;
    }

    public Physics setFriction(Vector2 friction)
    {
        this.friction = friction;
        return this;
    }

    public void applyForce(Vector2 force)
    {
        this.velocity = this.velocity.add(force);
    }
}
