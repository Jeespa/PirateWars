package com.mygdx.piratewars.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mygdx.piratewars.controller.PirateWarsController;

public class EndGameView implements Screen {
    private final PirateWarsController controller;
    private final Stage stage;
    private final UIBuilder uiBuilder;
    private TextButton mainMenuButton;
    private Sprite backgroundSprite;
    private SpriteBatch batch;

    public EndGameView(PirateWarsController controller) {
        this.controller = controller;
        this.stage = new Stage();
        this.uiBuilder = new UIBuilder(this.stage);
        batch = new SpriteBatch();
        new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        buildUI();
    }

    @Override
    public void show() {
        Texture background = new Texture(Gdx.files.internal("images/endGameBackground.png"));
        backgroundSprite = new Sprite(background);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage.setViewport(new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));

        Gdx.input.setInputProcessor(stage);
        render(0);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.6f, 0.8f, 1f, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.getViewport().apply();

        controller.gameModel.batch.begin();
        controller.gameModel.batch.end();

        batch.begin();
        backgroundSprite.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        backgroundSprite.setPosition((stage.getViewport().getWorldWidth() - backgroundSprite.getWidth()) / 2,
                (stage.getViewport().getWorldHeight() - backgroundSprite.getHeight()) / 2);
        backgroundSprite.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }

    @Override
    public void pause() {
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private void buildUI() {
        float mainMenuButtonWidth = 500;
        float mainMenuButtonHeight = 100f;

        float mainX = Gdx.graphics.getWidth() / 2f - mainMenuButtonWidth / 2f;
        float mainY = Gdx.graphics.getHeight()/ 2f - 2 * mainMenuButtonHeight;

        mainMenuButton = uiBuilder.buildButton("Return to Main Menu", mainMenuButtonWidth,
                mainMenuButtonHeight,
                mainX,
                mainY,
                "default");

        addActionsToUI();
    }

    private void addActionsToUI() {
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    controller.setScreen(new MainMenuView(controller));
                } catch (NullPointerException nullPointerException) {
                    System.out.println("No controller found");
                }

            }
        });
    }
}
