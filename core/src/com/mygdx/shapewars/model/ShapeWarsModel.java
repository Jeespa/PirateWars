package com.mygdx.shapewars.model;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.model.system.InputSystem;
import com.mygdx.shapewars.model.system.MovementSystem;
import com.mygdx.shapewars.model.system.RicochetSystem;

import java.util.ArrayList;
import java.util.List;

public class ShapeWarsModel {

    public static final int TANK_WIDTH = 75;
    public static final int TANK_HEIGHT = 75;

    public static final int NUM_PLAYERS = 2;

    public SpriteBatch batch;
    public static Engine engine;
    public static MovementSystem movementSystem;
    public static RicochetSystem ricochetSystem;
    private TiledMap map;

    public InputSystem inputSystem;

    public ShapeWarsModel() {
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/thirdMap.tmx");
        batch = new SpriteBatch();
        engine = new Engine();



        TiledMapTileLayer spawnLayer = (TiledMapTileLayer) map.getLayers().get(2);

        List<Vector2> spawnCells = new ArrayList<>();
        for (int y = 0; y < spawnLayer.getHeight(); y++) {
            for (int x = 0; x < spawnLayer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = spawnLayer.getCell(x, y);
                if (cell != null) {
                    spawnCells.add(new Vector2(x, y));
                }
            }
        }

        for (int i = 0; i < NUM_PLAYERS; i++) {
            Entity tank = new Entity();
            Vector2 cell = spawnCells.get(i);
            tank.add(new PositionComponent(cell.x * spawnLayer.getTileWidth(), cell.y * spawnLayer.getTileHeight()));
            tank.add(new VelocityComponent(0, 0));
            tank.add(new SpriteComponent("tank_graphics.png", TANK_WIDTH, TANK_HEIGHT));
            tank.add(new HealthComponent(100));
            tank.add(new IdentityComponent(i));
            engine.addEntity(tank);
        }
        movementSystem = MovementSystem.getInstance(map);
        inputSystem = InputSystem.getInstance();
        ricochetSystem = RicochetSystem.getInstance(map);
        engine.addSystem(movementSystem);
        engine.addSystem(inputSystem);
        engine.addSystem(ricochetSystem);
    }

    public static void update() {
        engine.update(Gdx.graphics.getDeltaTime());
    }

    public static void addToEngine(Entity entity) {
      engine.addEntity(entity);
    }

    public static void removedFromEngine(Entity entity) {
        engine.removeEntity(entity);
      }

    public TiledMap getMap() {
        return map;
    }
}
