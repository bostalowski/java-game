package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.character.Tengu;
import com.mygdx.environment.Plateform;
import com.mygdx.tools.Gravity;
import com.mygdx.tools.XBox360Pad;

import java.util.ArrayList;


public class MyGdxGame extends ApplicationAdapter implements ControllerListener, InputProcessor {

	private Controller controller;
	private OrthographicCamera camera;
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
		// Listen to all controllers, not just one
		Controllers.addListener(this);
		Gdx.input.setInputProcessor(this);

		if(Controllers.getControllers().size > 0) {
			this.controller = Controllers.getControllers().first();
		}

		this.camera = new OrthographicCamera(1280, 720);
		this.camera.setToOrtho(false, 1280, 720);
		this.camera.position.set(0, 720f / 2f, 0);
		this.camera.update();

		this.gravity = new Gravity();

		this.tengu = new Tengu(new Vector2(0, 20), 0, 0);
        this.gravity.addGravityAffectedElement(this.tengu);

		this.plateformList = new ArrayList();
		plateformList.add(new Plateform(0, -100, Gdx.graphics.getWidth()*10, 110));
		for(int i=0; i<20; i++) {
			plateformList.add(new Plateform(i*800, (float)(Math.random() * ( 500 - 100 )), (float)(Math.random() * ( 500 - 100 )), (float)(Math.random() * ( 500 - 100 ))));
		}
	}

	public void update(float deltaTime)
	{
		Vector2 oldTenguPosition = new Vector2(this.tengu.getPosition().x, this.tengu.getPosition().y);

		//apply gravity
		this.gravity.applyGravity();

		//TODO Check if on keyboard or controller
		this.handleKeyboard();
		this.handleController();

		//this.tengu.handleKeys();
		this.tengu.update(deltaTime);

		this.handleCollisions(oldTenguPosition);

		//add delta time to position ?
		//System.out.println((oldTenguPosition.x - this.tengu.getPosition().x) * deltaTime * 10);
	}


	@Override
	public void render () {
		float deltaTime = Gdx.graphics.getDeltaTime() * 10;
		update(deltaTime);

		//this.camera.position.set(this.tengu.getPosition().x, this.tengu.getPosition().y, 0);
		//this.tengu.getSpriteBatch().setProjectionMatrix(this.camera.combined);

		GL20 gl20 = Gdx.graphics.getGL20();
		gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gl20.glClearColor(1, 1, 1, 1);
//		gl20.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		this.camera.position.set(this.tengu.getPosition().x, this.camera.position.y, 0);
		this.camera.update();
		this.tengu.getSpriteBatch().setProjectionMatrix(this.camera.combined);

		//TODO set projection matrix on rectangle ?

		//plateforms
		for(int i=0; i<this.plateformList.size(); i++) {
			plateformList.get(i).setProjectionMatrix(this.camera.combined);
			plateformList.get(i).render();
		}
		//this.plateformList.forEach(com.mygdx.environment.Plateform::render);

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

			if(
				tenguRectangle.getY() == plateformRectangle.getY() + plateformRectangle.getHeight()
				/*&&
					(tenguRectangle.getX() >= plateformRectangle.getX()
					||
					tenguRectangle.getX() + tenguRectangle.getWidth() <= plateformRectangle.getX() + plateformRectangle.getWidth())*/
			)
			{
				isTenguOnGround = true;
			}

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

		this.tengu.setIsOnGround(isTenguOnGround);

		this.rectanglePool.free(tenguRectangle);
	}

	public void handleKeyboard()
	{
		//MOVE LEFT
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.tengu.moveLeft();
		}

		//MOVE RIGHT
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.tengu.moveRight();
		}

		//PARACHUTE
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.tengu.deployParachute();
		}
	}

	public void handleController()
	{
		if(this.controller != null) {
			//MOVE LEFT
			if(this.controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f) {
				this.tengu.moveLeft();
			}

			//MOVE RIGHT
			if(this.controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f ) {
				this.tengu.moveRight();
			}

			//PARACHUTE
			if(this.controller.getButton(XBox360Pad.BUTTON_X)) {
				this.tengu.deployParachute();
			}
		}
	}

	@Override
	public void connected(Controller controller) {

	}

	@Override
	public void disconnected(Controller controller) {

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		//JUMP
		if(buttonCode == XBox360Pad.BUTTON_A) {
			this.tengu.jump();
		}

		return true;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		//DASH LEFT
		if(axisCode == XBox360Pad.AXIS_LEFT_TRIGGER && value > 0.2f) {
			this.tengu.dashLeft();
		}

		//DASH RIGHT
		if(axisCode == XBox360Pad.AXIS_RIGHT_TRIGGER && value < -0.2f) {
			this.tengu.dashRight();
		}

		return true;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		//JUMP
		if(keycode == Input.Keys.SPACE) {
			this.tengu.jump();
		}

		//DASH LEFT
		if(keycode == Input.Keys.A) {
			this.tengu.dashLeft();
		}

		//DASH RIGHT
		if(keycode == Input.Keys.E) {
			this.tengu.dashRight();
		}

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
