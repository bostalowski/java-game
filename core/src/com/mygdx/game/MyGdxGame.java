package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.character.Tengu;

public class MyGdxGame extends ApplicationAdapter {
	World world;
	Tengu tengu;

	private static float PIXELS_TO_METERS_SCALE = 100f;

	Body bodyEdgeScreen;
	Box2DDebugRenderer debugRenderer;
	Matrix4 debugMatrix;
	OrthographicCamera camera;
	BitmapFont font;

	@Override
	public void create ()
	{
		//set a world gravity
		world = new World(new Vector2(0, -10f), true);

		tengu = new Tengu(world);
		tengu.create();

		BodyDef bodyDef2 = new BodyDef();
		bodyDef2.type = BodyDef.BodyType.StaticBody;
		float w = Gdx.graphics.getWidth();
		bodyDef2.position.set(0,0);
		FixtureDef fixtureDef2 = new FixtureDef();

		EdgeShape edgeShape = new EdgeShape();
		edgeShape.set(0,0,w,0);
		fixtureDef2.shape = edgeShape;

		bodyEdgeScreen = world.createBody(bodyDef2);
		bodyEdgeScreen.createFixture(fixtureDef2);
		edgeShape.dispose();

		debugRenderer = new Box2DDebugRenderer();
		font = new BitmapFont();
		font.setColor(Color.BLACK);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.
				getHeight());
	}

	public void update()
	{
		camera.update();
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		tengu.update();

		debugMatrix = tengu.getSpriteBatch().getProjectionMatrix().cpy().scale(100f, 100f, 0);
	}


	@Override
	public void render () {
		update();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		tengu.render();

		debugRenderer.render(world, debugMatrix);
	}

	@Override
	public void dispose() {
		tengu.dispose();
		world.dispose();
	}

	public static float PixelsToMeters(float pixels)
	{
		return pixels / PIXELS_TO_METERS_SCALE;
	}

	public static float MetersToPixels(float meters)
	{
		return meters * PIXELS_TO_METERS_SCALE;
	}
}
