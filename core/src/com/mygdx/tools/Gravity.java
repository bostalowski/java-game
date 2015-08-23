package com.mygdx.tools;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Gravity
{
    private static final float GRAVITY_MAX_VELOCITY = -20f;
    private static final float GRAVITY_ACCELERATION = -1f;

    private ArrayList<Physics> gravityAffectedElements;

    public Gravity()
    {
        this.gravityAffectedElements = new ArrayList<Physics>();
    }

    public Gravity addGravityAffectedElement(Physics element)
    {
        this.gravityAffectedElements.add(element);
        return this;
    }

    public Gravity removeGravityAffectedElement(Physics element)
    {
        this.gravityAffectedElements.remove(element);
        return this;
    }

    public ArrayList<Physics> getGravityAffectedElements()
    {
        return this.gravityAffectedElements;
    }

    public void applyGravity()
    {
        for(int i=0; i< this.gravityAffectedElements.size(); i++) {
            Physics physicsElement = this.gravityAffectedElements.get(i);
            if(physicsElement.getVelocity().y > GRAVITY_MAX_VELOCITY) {
                physicsElement.applyForce(new Vector2(0, GRAVITY_ACCELERATION));
            }
        }
    }
}
