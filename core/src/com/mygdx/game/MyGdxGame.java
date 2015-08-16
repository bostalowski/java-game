package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.character.Tengu;
import com.mygdx.tools.Gravity;


public class MyGdxGame extends ApplicationAdapter {

    Gravity gravity;
	Tengu tengu;

	@Override
	public void create ()
	{
        this.gravity = new Gravity();

		this.tengu = new Tengu(new Vector2(Gdx.graphics.getWidth() / 2, 0), 0, 0);
        this.gravity.addGravityAffectedElement(this.tengu);
	}

	public void update(float deltaTime)
	{
        //apply gravity
        this.gravity.applyGravity();

		this.tengu.update(deltaTime);

        //if out of bound (y), replace it just on the edge of the screen
        this.gravity.outOfBounds();
	}


	@Override
	public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime() * 10;
        
		update(deltaTime);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		this.tengu.render();
	}

	@Override
	public void dispose() {}
}
