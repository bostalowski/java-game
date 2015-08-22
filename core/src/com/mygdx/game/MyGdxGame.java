package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.character.Tengu;
import com.mygdx.tools.Gravity;


public class MyGdxGame extends ApplicationAdapter {

    private Gravity gravity;
	private Tengu tengu;

	private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject () {
			return new Rectangle();
		}
	};

	ShapeRenderer shapeRenderer;

	@Override
	public void create ()
	{
        this.gravity = new Gravity();

		this.tengu = new Tengu(new Vector2(0, 0), 0, 0);
        this.gravity.addGravityAffectedElement(this.tengu);

		this.shapeRenderer = new ShapeRenderer();
	}

	public void update(float deltaTime)
	{
		Vector2 oldTenguPosition = new Vector2(this.tengu.getPosition().x, this.tengu.getPosition().y);

		//apply gravity
		this.gravity.applyGravity();

		this.tengu.handleKeys();
		this.tengu.update(deltaTime);

		Rectangle plateform = this.rectPool.obtain()
				.set(300, 200, 200, 100);

		Rectangle tenguRectangle = this.rectPool.obtain()
				.set(this.tengu.getPosition().x, this.tengu.getPosition().y, this.tengu.getWidth(), this.tengu.getHeight());

		if(tenguRectangle.overlaps(plateform)) {
			boolean resetX = false,
					resetY = false;

			boolean isTenguOverPlateform = plateform.getY() + plateform.getHeight() < oldTenguPosition.y;
			boolean isTenguUnderPlateform = plateform.getY() > oldTenguPosition.y + tenguRectangle.getHeight();
			boolean isTenguOnLeftOrRight = plateform.getX() > oldTenguPosition.x + tenguRectangle.getWidth() ||
					plateform.getX() + plateform.getWidth() < oldTenguPosition.x;

			if(this.tengu.getVelocity().x > 0 && !isTenguOverPlateform && isTenguOnLeftOrRight) {
				float velocityX = plateform.getX() - (tenguRectangle.getX() + tenguRectangle.getWidth() + 1) - this.tengu.getVelocity().x;
				this.tengu.applyForce(new Vector2(velocityX, 0));
				resetX = true;
			} else if(this.tengu.getVelocity().x < 0 && !isTenguOverPlateform && isTenguOnLeftOrRight) {
				float velocityX = (plateform.getX() + plateform.getWidth() + 1) - tenguRectangle.getX() - this.tengu.getVelocity().x;
				this.tengu.applyForce(new Vector2(velocityX, 0));
				resetX = true;
			}

			if(this.tengu.getVelocity().y > 0 && isTenguUnderPlateform) {
				float velocityY = plateform.getY() - (tenguRectangle.getY() + tenguRectangle.getHeight() + 1) - this.tengu.getVelocity().y;
				this.tengu.applyForce(new Vector2(0, velocityY));
				resetY = true;
			} else if(this.tengu.getVelocity().y < 0 && isTenguOverPlateform) {
				float velocityY = (plateform.getY() + plateform.getHeight() + 1) - tenguRectangle.getY() - this.tengu.getVelocity().y;
				this.tengu.applyForce(new Vector2(0, velocityY));
				resetY = true;
				this.tengu.isOnGround(true);
			}

			this.tengu.getPosition().add(this.tengu.getVelocity());
			this.tengu.setVelocity(new Vector2(resetX ? 0 : this.tengu.getVelocity().x, resetY ? 0 : this.tengu.getVelocity().y));
		}

		this.rectPool.free(plateform);
		this.rectPool.free(tenguRectangle);

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
		this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		this.shapeRenderer.setColor(Color.RED);
		this.shapeRenderer.rect(300, 200, 200, 100);
		this.shapeRenderer.end();
	}

	@Override
	public void dispose() {}
}
