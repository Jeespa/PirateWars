package com.mygdx.shapewars.model.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.HealthComponent;
import com.mygdx.shapewars.model.components.IdentityComponent;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;

public class InputSystem extends EntitySystem {
  private ImmutableArray<Entity> entities;
  
  private static volatile InputSystem instance;

  private InputSystem() {};

  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(
        Family.all(PositionComponent.class, VelocityComponent.class, SpriteComponent.class, HealthComponent.class).get());
  }

  public void update(float deltaTime) {
    for (Entity entity : entities) {
      VelocityComponent velocityComponent = ComponentMappers.velocity.get(entity);

      IdentityComponent identityComponent = ComponentMappers.identity.get(entity);

      if (identityComponent.getId() == 0) {
          if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocityComponent.addDirection(2);
          } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocityComponent.addDirection(-2);
          }

          //Controls value of velocity
          if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocityComponent.setValue(5);
          } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocityComponent.setValue(-5);
          } else {
            velocityComponent.setValue(0);
          }
      } else if (identityComponent.getId() == 1) {
            //Controls direction of velocity

            //Controls direction of velocity
          if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocityComponent.addDirection(2);
          } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocityComponent.addDirection(-2);
          }

          //Controls value of velocity
          if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            velocityComponent.setValue(5);
          } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            velocityComponent.setValue(-5);
          } else {
            velocityComponent.setValue(0);
          }
        }
      }

  }

  public static InputSystem getInstance() {
		if (instance == null) {
			synchronized (InputSystem.class) {
				if (instance == null) {
					instance = new InputSystem();
				}
			}
		}
		return instance;
	}
}