package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.character.Tengu;
import com.mygdx.environment.Plateform;
import com.mygdx.tools.Gravity;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter {

    private Gravity gravity;
	private Tengu tengu;

	private Pool<Rectangle> rectanglePool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};

	ArrayList<Plateform> plateformList;

	@Override
	public void create ()
	{
        this.gravity = new Gravity();

		this.tengu = new Tengu(new Vector2(0, 20), 0, 0);
        this.gravity.addGravityAffectedElement(this.tengu);

		this.plateformList = new ArrayList<>();
		plateformList.add(new Plateform(0, -50, Gdx.graphics.getWidth()*10, 60));
		plateformList.add(new Plateform(300, 250, 200, 100));
	}

	public void update(float deltaTime)
	{
		Vector2 oldTenguPosition = new Vector2(this.tengu.getPosition().x, this.tengu.getPosition().y);

		//apply gravity
		this.gravity.applyGravity();

		this.tengu.handleKeys();
		this.tengu.update(deltaTime);

		this.handleCollisions(oldTenguPosition);
	}


	@Override
	public void render () {
        float deltaTime = Gdx.graphics.getDeltaTime() * 10;
        
		update(deltaTime);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		//plateforms
		this.plateformList.forEach(com.mygdx.environment.Plateform::render);

		this.tengu.render();
	}

	@Override
	public void dispose() {}

	public void handleCollisions(Vector2 oldTenguPosition)
	{
		Rectangle tenguRectangle = this.tengu.getRectangle(this.rectanglePool.obtain());

		boolean isTenguOnGround = false;

		for(Plateform plateform : this.plateformList) {
			Rectangle plateformRectangle = plateform.getRectangle(this.rectanglePool.obtain());

			if(tenguRectangle.overlaps(plateformRectangle)) {
				boolean resetX = false,
						resetY = false;

				boolean isTenguOverPlateform = plateformRectangle.getY() + plateformRectangle.getHeight() <= oldTenguPosition.y;
				boolean isTenguUnderPlateform = plateformRectangle.getY() >= oldTenguPosition.y + tenguRectangle.getHeight();
				boolean isTenguOnLeftOrRight = plateformRectangle.getX() >= oldTenguPosition.x + tenguRectangle.getWidth() ||
						plateformRectangle.getX() + plateformRectangle.getWidth() <= oldTenguPosition.x;

				if(this.tengu.getVelocity().x > 0 && !isTenguOverPlateform && isTenguOnLeftOrRight) {
					float velocityX = plateformRectangle.getX() - (tenguRectangle.getX() + tenguRectangle.getWidth()) - this.tengu.getVelocity().x;
					this.tengu.applyForce(new Vector2(velocityX, 0));
					resetX = true;
				} else if(this.tengu.getVelocity().x < 0 && !isTenguOverPlateform && isTenguOnLeftOrRight) {
					float velocityX = (plateformRectangle.getX() + plateformRectangle.getWidth()) - tenguRectangle.getX() - this.tengu.getVelocity().x;
					this.tengu.applyForce(new Vector2(velocityX, 0));
					resetX = true;
				}

				if(this.tengu.getVelocity().y > 0 && isTenguUnderPlateform) {
					float velocityY = plateformRectangle.getY() - (tenguRectangle.getY() + tenguRectangle.getHeight()) - this.tengu.getVelocity().y;
					this.tengu.applyForce(new Vector2(0, velocityY));
					resetY = true;
				} else if(this.tengu.getVelocity().y < 0 && isTenguOverPlateform) {
					float velocityY = (plateformRectangle.getY() + plateformRectangle.getHeight()) - tenguRectangle.getY() - this.tengu.getVelocity().y;
					this.tengu.applyForce(new Vector2(0, velocityY));
					resetY = true;
					isTenguOnGround = true;
				}

				this.tengu.getPosition().add(this.tengu.getVelocity());
				this.tengu.setVelocity(new Vector2(resetX ? 0 : this.tengu.getVelocity().x, resetY ? 0 : this.tengu.getVelocity().y));
			}

			this.rectanglePool.free(plateformRectangle);
		}

		this.tengu.isOnGround(isTenguOnGround);

		this.rectanglePool.free(tenguRectangle);
	}
}
