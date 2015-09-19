package com.japaleno.game;

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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.japaleno.character.Tengu;
import com.japaleno.environment.Platform;
import com.japaleno.tools.EffectHandler;
import com.japaleno.tools.Gravity;
import com.japaleno.tools.XBox360Pad;

public class JapalenoGame extends ApplicationAdapter implements ControllerListener, InputProcessor {

    private Controller controller;
    private OrthographicCamera camera;
    private Tengu tengu;
    private Gravity gravity;
    private SpriteBatch spriteBatch;

    private Pool<Rectangle> rectanglePool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject() {
            return new Rectangle();
        }
    };

    private Array platformList;

    @Override
    public void create() {
        // set resolution to default and set full-screen to true
        //Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, true);

        // Listen to all controllers, not just one
        Controllers.addListener(this);
        Gdx.input.setInputProcessor(this);

        if (Controllers.getControllers().size > 0) {
            this.controller = Controllers.getControllers().first();
        }

        this.camera = new OrthographicCamera(1920, 1080);
        this.camera.setToOrtho(false, 1920, 1080);
        this.camera.position.set(0, 1080f / 2f, 0);
        this.camera.update();

        this.tengu = new Tengu(new Vector2(0, 20), 0, 0);
        this.gravity = new Gravity();
        this.gravity.addGravityAffectedElement(this.tengu);

        this.spriteBatch = new SpriteBatch();

        this.platformList = new Array();
        this.platformList.add(new Platform(0, -100, Gdx.graphics.getWidth() * 100, 110));
        for (int i = 0; i < 50; i++) {
            this.platformList.add(new Platform((i + 1) * 800, (float) Math.round(Math.random() * (500 - 100)), (float) Math.round(Math.random() * (500 - 100)), (float) Math.round(Math.random() * (500 - 100))));
        }
    }

    public void update(float deltaTime) {
        //apply gravity
        this.gravity.applyGravity();

        //TODO Check if on keyboard or controller
        this.handleKeyboard();
        this.handleController();

        this.tengu.update();

        this.handleCollisions();

        this.tengu.setOldPosition(this.tengu.getPosition()).setOldVelocity(this.tengu.getVelocity());

        //remove finished animations
        EffectHandler.RemoveFinishedEffects(deltaTime);


        //add delta time to position ?
        //System.out.println((oldTenguPosition.x - this.tengu.getPosition().x) * deltaTime * 10);
    }


    @Override
    public void render() {
        float deltaTime = Gdx.graphics.getDeltaTime() * 10;
        update(deltaTime);

        GL20 gl20 = Gdx.graphics.getGL20();
        gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gl20.glClearColor(1, 1, 1, 1);

        this.camera.position.set(this.tengu.getPosition().x, this.camera.position.y, 0);
        this.camera.update();
        this.spriteBatch.setProjectionMatrix(this.camera.combined);

        //platforms
        for (int i = 0; i < this.platformList.size; i++) {
            Platform platform = (Platform) this.platformList.get(i);
            platform.setProjectionMatrix(this.camera.combined);
            platform.render();
        }

        this.spriteBatch.begin();

        //display background effects
        EffectHandler.RenderBackgroundEffects(this.spriteBatch);

        this.tengu.render(this.spriteBatch);

        //display foreground effects
        EffectHandler.RenderForegroundEffects(this.spriteBatch);

        this.spriteBatch.end();
    }

    @Override
    public void dispose() {
    }

    public void handleCollisions() {
        Vector2 oldTenguPosition = this.tengu.getOldPosition().sub(this.tengu.getOldVelocity());

        Rectangle tenguRectangle = this.tengu.getRectangle(this.rectanglePool.obtain());

        boolean isTenguOnGround = false;

        boolean resetX = false,
                resetY = false;

        for (int i = 0; i < this.platformList.size; i++) {
            Platform platform = (Platform) this.platformList.get(i);
            Rectangle plateformRectangle = platform.getRectangle(this.rectanglePool.obtain());

            if (
                    ((tenguRectangle.getX() + tenguRectangle.getWidth() >= plateformRectangle.getX()) && (tenguRectangle.getX() <= plateformRectangle.getX() + plateformRectangle.getWidth()))
                            &&
                            ((oldTenguPosition.y >= plateformRectangle.getY() + plateformRectangle.getHeight()) && (tenguRectangle.getY() <= plateformRectangle.getY() + plateformRectangle.getHeight()))
                    ) {
                isTenguOnGround = true;
            }

            if (tenguRectangle.overlaps(plateformRectangle)) {
                boolean isTenguOverPlateform = plateformRectangle.getY() + plateformRectangle.getHeight() <= oldTenguPosition.y;
                boolean isTenguUnderPlateform = plateformRectangle.getY() >= oldTenguPosition.y + tenguRectangle.getHeight();
                boolean isTenguOnLeft = plateformRectangle.getX() >= oldTenguPosition.x + tenguRectangle.getWidth();
                boolean isTenguOnRight = plateformRectangle.getX() + plateformRectangle.getWidth() <= oldTenguPosition.x;

                if (this.tengu.getVelocity().x >= 0 && !isTenguOverPlateform && isTenguOnLeft) {
                    float velocityX = (plateformRectangle.getX() - 0.1f) - (tenguRectangle.getX() + tenguRectangle.getWidth() + this.tengu.getVelocity().x);
                    this.tengu.applyForce(new Vector2(velocityX, 0));
                    resetX = true;
                } else if (this.tengu.getVelocity().x <= 0 && !isTenguOverPlateform && isTenguOnRight) {
                    float velocityX = (plateformRectangle.getX() + plateformRectangle.getWidth() + 0.1f) - (tenguRectangle.getX() + this.tengu.getVelocity().x);
                    this.tengu.applyForce(new Vector2(velocityX, 0));
                    resetX = true;
                }

                if (this.tengu.getVelocity().y > 0 && isTenguUnderPlateform) {
                    float velocityY = (plateformRectangle.getY() - 0.1f) - (tenguRectangle.getY() + tenguRectangle.getHeight()) - this.tengu.getVelocity().y;
                    this.tengu.applyForce(new Vector2(0, velocityY));
                    resetY = true;
                } else if (this.tengu.getVelocity().y < 0 && isTenguOverPlateform) {
                    //add 0.1f to avoid float to be reversed
                    float velocityY = (plateformRectangle.getY() + plateformRectangle.getHeight() + 0.1f) - tenguRectangle.getY() - this.tengu.getVelocity().y;
                    this.tengu.applyForce(new Vector2(0, velocityY));
                    resetY = true;
                    isTenguOnGround = true;
                }
            }

            this.rectanglePool.free(plateformRectangle);
        }

        this.tengu.getPosition().add(this.tengu.getVelocity());
        this.tengu.setVelocity(new Vector2(resetX ? 0 : this.tengu.getVelocity().x, resetY ? 0 : this.tengu.getVelocity().y));
        this.tengu.setIsOnGround(isTenguOnGround);

        this.rectanglePool.free(tenguRectangle);
    }

    public void handleKeyboard() {
        //MOVE LEFT
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            this.tengu.moveLeft();
        }

        //MOVE RIGHT
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            this.tengu.moveRight();
        }

        //PARACHUTE
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            this.tengu.deployParachute();
        }

        //DEBUG
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            int debug = 1;
        }
    }

    public void handleController() {
        if (this.controller != null) {
            //MOVE LEFT
            if (this.controller.getAxis(XBox360Pad.AXIS_LEFT_X) < -0.2f) {
                this.tengu.moveLeft();
            }

            //MOVE RIGHT
            if (this.controller.getAxis(XBox360Pad.AXIS_LEFT_X) > 0.2f) {
                this.tengu.moveRight();
            }

            //PARACHUTE
            if (this.controller.getButton(XBox360Pad.BUTTON_X)) {
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
        if (buttonCode == XBox360Pad.BUTTON_A) {
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
        if (axisCode == XBox360Pad.AXIS_LEFT_TRIGGER && value > 0.2f) {
            this.tengu.dashLeft();
        }

        //DASH RIGHT
        if (axisCode == XBox360Pad.AXIS_RIGHT_TRIGGER && value < -0.2f) {
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
        if (keycode == Input.Keys.SPACE) {
            this.tengu.jump();
        }

        //DASH LEFT
        if (keycode == Input.Keys.A) {
            this.tengu.dashLeft();
        }

        //DASH RIGHT
        if (keycode == Input.Keys.E) {
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
