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
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.graalvm.compiler.replacements.Log;

public class MyGdxGame extends ApplicationAdapter  {

	static final int WORLD_WIDTH = 70;
	static final int WORLD_HEIGHT = 70;


	OrthographicCamera camera;

	SpriteBatch batch;
	Sprite sprite;

	
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
	}

	@Override
	public void render () {
		handleInput();
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		sprite.draw(batch);
		batch.end();
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
