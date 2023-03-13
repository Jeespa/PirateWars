package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private Screen currentScreen;

    private final VelocityComponent velocityComponent;


    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      velocityComponent = ComponentMappers.velocity.get(model.tank);
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
            // get direction
            if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velocityComponent.addDirection(2);
              } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velocityComponent.addDirection(-2);
            }

            // get velocity
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
                velocityComponent.setValue(5);
              } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
                velocityComponent.setValue(-5);
              } else {
                velocityComponent.setValue(0);
            }
            model.update();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    private Rectangle checkCollisionWithWalls(float x, float y, float width, float height, TiledMapTileLayer wallsLayer) {
        for (int col = 0; col < wallsLayer.getWidth(); col++) {
            for (int row = 0; row < wallsLayer.getHeight(); row++) {
                TiledMapTileLayer.Cell cell = wallsLayer.getCell(col, row);
                if (cell != null) {
                    Rectangle rect = new Rectangle(col * wallsLayer.getTileWidth(), row * wallsLayer.getTileHeight(),
                            wallsLayer.getTileWidth(), wallsLayer.getTileHeight());
                    if (rect.overlaps(new Rectangle(x, y, width, height))) {
                        return rect;
                    }
                }
            }
        }
        return null;
    }

    public void dispose() {
        model.batch.dispose();
    }
}
