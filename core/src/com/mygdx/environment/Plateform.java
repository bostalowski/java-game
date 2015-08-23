package com.mygdx.environment;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Plateform
{
    private Vector2 position;
    private float width,
        height;

    private ShapeRenderer shapeRenderer;

    public Plateform(float x, float y, float width, float height)
    {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.shapeRenderer = new ShapeRenderer();
    }

    public void update(float deltatime)
    {

    }

    public void render()
    {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        this.shapeRenderer.setColor(Color.RED);
        this.shapeRenderer.rect(this.position.x, this.position.y, this.width, this.height);
        this.shapeRenderer.end();
    }

    public Rectangle getRectangle(Rectangle rectangle)
    {
        return rectangle.setX(this.position.x).setY(this.position.y).setWidth(this.width).setHeight(this.height);
    }
}
