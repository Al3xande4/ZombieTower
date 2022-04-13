package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.graalvm.compiler.replacements.Log;

public class MyGdxGame extends ApplicationAdapter  {

	static final int WORLD_WIDTH = 70;
	static final int WORLD_HEIGHT = 70;


	OrthographicCamera camera;
	Touchpad touchpad;

	SpriteBatch batch;
	Sprite sprite;

	Stage stage;
	Skin touchpadSkin;

	Touchpad.TouchpadStyle touchpadStyle;
	float cameraMovementSpeed = .3f;

	private Drawable touchBackground;
	private Drawable touchKnob;

	Texture blockTexture;
	float blockSpeed;
	Sprite blockSprite;

	
	@Override
	public void create () {

		sprite = new Sprite(new Texture(Gdx.files.internal("Level1.png")));
		sprite.setPosition(0, 0);
		sprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(30, 30 * (h / w));
		camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);
		camera.update();

		batch = new SpriteBatch();
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		touchpadSkin = new Skin();
		//Set background image
		touchpadSkin.add("touchBackground", new Texture("trans.png"));
		//Set knob image
		touchpadSkin.add("touchKnob", new Texture("img_2.png"));
		//Create TouchPad Style
		touchpadStyle = new Touchpad.TouchpadStyle();
		//Create Drawable's from TouchPad skin
		touchBackground = touchpadSkin.getDrawable("touchBackground");
		touchKnob = touchpadSkin.getDrawable("touchKnob");
		//Apply the Drawables to the TouchPad Style
		touchpadStyle.background = touchBackground;
		touchpadStyle.knob = touchKnob;
		//Create new TouchPad with the created style
		touchpad = new Touchpad(10, touchpadStyle);
		//setBounds(x,y,width,height)
		touchpad.setBounds(15, 15, 200, 200);
		//Create a Stage and add TouchPad
		stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
		stage.addActor(touchpad);
		Gdx.input.setInputProcessor(stage);

		//Create block sprite
		blockTexture = new Texture(Gdx.files.internal("img_3.png"));
		blockSprite = new Sprite(blockTexture);
		blockSprite.setSize(2, 2);
		//Set position to centre of the screen
		blockSprite.setPosition(camera.position.x, camera.position.y);

		blockSpeed = .3f;
	}

	@Override
	public void render () {
		handleInput();
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		blockSprite.setX(blockSprite.getX() + touchpad.getKnobPercentX() * blockSpeed);
		blockSprite.setY(blockSprite.getY() + touchpad.getKnobPercentY() * blockSpeed);

		batch.begin();
		sprite.draw(batch);
		blockSprite.draw(batch);
		batch.end();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			camera.zoom += 0.02;
			//If the A Key is pressed, add 0.02 to the Camera's Zoom
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
			camera.zoom -= 0.02;
			//If the Q Key is pressed, subtract 0.02 from the Camera's Zoom
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			camera.translate(-.5f * camera.zoom, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			camera.translate(.5f * camera.zoom, 0, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			camera.translate(0, -.5f * camera.zoom, 0);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			camera.translate(0, .5f * camera.zoom, 0);
		}
		camera.translate(touchpad.getKnobPercentX() * cameraMovementSpeed * camera.zoom, touchpad.getKnobPercentY() * camera.zoom * cameraMovementSpeed, 0);
		camera.zoom = MathUtils.clamp(camera.zoom, .5f, 1f);

		float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 50f - effectiveViewportWidth / 2f);
		camera.position.y = MathUtils.clamp(camera.position.y, 30f, 45f);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = 30f;
		camera.viewportHeight = 30f * height/width;
		camera.update();
	}

	@Override
	public void dispose () {
		sprite.getTexture().dispose();
		batch.dispose();
	}
}
