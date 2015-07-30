package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.character.Tengu;
import com.mygdx.character.animations.CharacterJumpAnimation;
import com.mygdx.character.animations.CharacterRunAnimation;
import com.mygdx.character.animations.CharacterStanceAnimation;

import java.awt.geom.Point2D;

public class MyGdxGame extends ApplicationAdapter {
	Tengu tengu;

	@Override
	public void create ()
	{
		tengu = new Tengu();
		tengu.create();
	}

	@Override
	public void render ()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		tengu.render();
	}
}
