package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.character.Tengu;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;

import java.awt.geom.Point2D;

public class MyGdxGame extends ApplicationAdapter {
	World world;
	Tengu tengu;

	Box2DDebugRenderer debugRenderer;
	OrthographicCamera camera;

	@Override
	public void create ()
	{
		//set a world gravity
		world = new World(new Vector2(0, -10), true);

		tengu = new Tengu();
		tengu.create();
		tengu.createBody(world);

		debugRenderer = new Box2DDebugRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}

	public void update()
	{
		camera.update();
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		tengu.update();
	}


	@Override
	public void render ()
	{
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		tengu.render();
	}

	@Override
	public void dispose() {
		tengu.dispose();
		world.dispose();
	}
}
