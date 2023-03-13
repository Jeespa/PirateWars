package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private Screen currentScreen;
    
    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
      this.model = model;
      this.shapeWarsView = view;
      this.mainMenuView = mainMenuView;
      this.currentScreen = mainMenuView;
      currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
          model.update();
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }

        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    public void dispose() {
        model.batch.dispose();
    }
}
