package com.mygdx.shapewars.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.mygdx.shapewars.model.ShapeWarsModel;
import com.mygdx.shapewars.model.components.ComponentMappers;
import com.mygdx.shapewars.model.components.PositionComponent;
import com.mygdx.shapewars.model.components.SpriteComponent;
import com.mygdx.shapewars.model.components.VelocityComponent;
import com.mygdx.shapewars.view.MainMenuView;
import com.mygdx.shapewars.view.ShapeWarsView;

public class ShapeWarsController {

    private final ShapeWarsModel model;
    private final ShapeWarsView shapeWarsView;
    private final MainMenuView mainMenuView;
    private final VelocityComponent velocityComponent;
    private final PositionComponent positionComponent;
    private final SpriteComponent spriteComponent;

    private Screen currentScreen;

    public ShapeWarsController(ShapeWarsModel model, ShapeWarsView view, MainMenuView mainMenuView) {
        this.model = model;
        this.shapeWarsView = view;
        this.mainMenuView = mainMenuView;
        this.currentScreen = mainMenuView;
        velocityComponent = ComponentMappers.velocity.get(model.tank);
        positionComponent = ComponentMappers.position.get(model.tank);
        spriteComponent = ComponentMappers.sprite.get(model.tank);
        currentScreen.show();
    }

    public void update() {
        if (currentScreen instanceof ShapeWarsView) {
            getDirectionAndVelocityInput();

            TiledMapTileLayer collisionLayer = shapeWarsView.getCollisionLayer();

            // calculate old and new position values
            float radians = MathUtils.degreesToRadians * velocityComponent.getDirection();

            float oldX = positionComponent.getPosition().x;
            float oldY = positionComponent.getPosition().y;

            float newX = positionComponent.getPosition().x + MathUtils.cos(radians) * velocityComponent.getSpeed();
            float newY = positionComponent.getPosition().y + MathUtils.sin(radians) * velocityComponent.getSpeed();

            // detect colliding corners and edges
            Polygon hitbox = spriteComponent.getHitbox();
            String collisionType = getCollisionType(newX, newY, hitbox, collisionLayer);


            // set direction only if no collision with walls
            if (collisionType.equals("none")) {
                System.out.println("none");
                // no collision rotate the tank as planned
                spriteComponent.setRotation(velocityComponent.getDirection());
                // No collision, update the position as planned
                positionComponent.addPosition(newX, newY);
                spriteComponent.getSprite().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
                spriteComponent.getHitbox().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
            } else {
                float goalX, goalY;
                velocityComponent.setSpeed(0);
                System.out.println(collisionType);
                switch (collisionType) {
                    case "left":
                        // movement is to the left
                        if (newX < oldX) {
                            goalX = oldX + 0.1f;
                            goalY = newY;
                        } else {
                            goalX = newX;
                            goalY = newY;
                        }
                        break;
                    case "right":
                        // movement is to the right
                        if (newX > oldX) {
                            goalX = oldX - 0.1f;
                            goalY = newY;
                        } else {
                            goalX = newX;
                            goalY = newY;
                        }
                        break;
                    case "top":
                        // movement is upwards
                        if (newY > oldY) {
                            goalX = newX;
                            goalY = oldY - 0.1f;
                        } else {
                            goalX = newX;
                            goalY = newY;
                        }
                        break;
                    case "bottom":
                        // movement is downwards
                        if (newY < oldY) {
                            goalX = newX;
                            goalY = oldY + 0.1f;
                            positionComponent.addPosition(goalX, goalY);
                        } else {
                            goalX = newX;
                            goalY = newY;
                        }
                        break;
                    case "topLeft":
                        if (newX < oldX) {
                            goalX = oldX + 0.1f;
                        } else {
                            goalX = newX;
                        }
                        if (newY > oldY) {
                            goalY = oldY - 0.1f;
                        } else {
                            goalY = newY;
                        }
                        break;
                    case "topRight":
                        if (newX > oldX) {
                            goalX = oldX - 0.1f;
                        } else {
                            goalX = newX;
                        }
                        if (newY > oldY) {
                            goalY = oldY - 0.1f;
                        } else {
                            goalY = newY;
                        }
                        break;
                    case "bottomLeft":
                        if (newX < oldX) {
                            goalX = oldX + 0.1f;
                        } else {
                            goalX = newX;
                        }
                        if (newY < oldY) {
                            goalY = oldY + 0.1f;
                        } else {
                            goalY = newY;
                        }
                        break;
                    case "bottomRight":
                        System.out.print("NewX: " + newX);
                        System.out.println(" | OldX: " + oldX);
                        System.out.print("NewY: " + newY);
                        System.out.println(" | OldY: " + oldY);
                        if (newX > oldX) {
                            goalX = oldX - 0.1f;
                        }
                        else {
                            goalX = newX;
                        }
                        if (newY < oldY) {
                            goalY = oldY + 0.1f;
                        } else {
                            goalY = newY;
                        }
                        break;
                    case "severalCollisions":
                        goalX = oldX;
                        goalY = oldY;
                        break;
                    default:
                        goalX = newX;
                        goalY = newY;
                        break;
                }
                positionComponent.addPosition(goalX, goalY);
                spriteComponent.getSprite().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
                spriteComponent.getHitbox().setPosition(positionComponent.getPosition().x, positionComponent.getPosition().y);
                spriteComponent.setRotation(velocityComponent.getDirection());
            }
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.F)) {
                currentScreen = shapeWarsView;
                currentScreen.show();
            }
        }
        currentScreen.render(0);
    }

    private void getDirectionAndVelocityInput() {
        // get direction
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            velocityComponent.addDirection(2);
        } else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            velocityComponent.addDirection(-2);
        }

        // get velocity
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocityComponent.setSpeed(5);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocityComponent.setSpeed(-5);
        } else {
            velocityComponent.setSpeed(0);
        }
    }

    public String getCollisionType(float x, float y, Polygon hitbox, TiledMapTileLayer collisionLayer) {
        // iterate over all cells in the collision layer that the hitbox touches
        int cellWidth = collisionLayer.getTileWidth();
        int cellHeight = collisionLayer.getTileHeight();
        int startX = (int) (x - hitbox.getBoundingRectangle().width / 2) / cellWidth;
        int startY = (int) (y - hitbox.getBoundingRectangle().height / 2) / cellHeight;
        int endX = (int) (x + hitbox.getBoundingRectangle().width / 2) / cellWidth;
        int endY = (int) (y + hitbox.getBoundingRectangle().height / 2) / cellHeight;

        boolean collidedTop = false;
        boolean collidedBottom = false;
        boolean collidedLeft = false;
        boolean collidedRight = false;

        // iterate over all cells in the collision layer that the hitbox touches
        for (int cellX = startX - 1; cellX <= endX + 1; cellX++) {
            for (int cellY = startY - 1; cellY <= endY + 1; cellY++) {
                TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX, cellY);
                if (cell != null && cell.getTile() != null) {
                    // calculate the bounds of the cell
                    float cellLeft = cellX * cellWidth;
                    float cellBottom = cellY * cellHeight;
                    float cellRight = (cellX + 1) * cellWidth;
                    float cellTop = (cellY + 1) * cellHeight;

                    // create a polygon for the cell
                    Polygon cellPolygon = new Polygon(new float[]{cellLeft, cellBottom, cellRight, cellBottom, cellRight, cellTop, cellLeft, cellTop});

                    // check for overlap with the hitbox polygon
                    if (Intersector.overlapConvexPolygons(hitbox, cellPolygon)) {
                        // find the intersection points between the hitbox and the cell polygon
                        float[] cellVertices = cellPolygon.getTransformedVertices();
                        for (int i = 0; i < cellVertices.length; i += 2) {
                            float cellX1 = cellVertices[i];
                            float cellY1 = cellVertices[i + 1];
                            float cellX2 = cellVertices[(i + 2) % cellVertices.length];
                            float cellY2 = cellVertices[(i + 3) % cellVertices.length];

                            float hitboxTop = hitbox.getBoundingRectangle().y + hitbox.getBoundingRectangle().height;
                            float hitboxBottom = hitbox.getBoundingRectangle().y;
                            float hitboxLeft = hitbox.getBoundingRectangle().x;
                            float hitboxRight = hitbox.getBoundingRectangle().x + hitbox.getBoundingRectangle().width;


                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxTop, hitboxRight, hitboxTop, null)) {
                                collidedTop = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxBottom, hitboxRight, hitboxBottom, null)) {
                                collidedBottom = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxLeft, hitboxTop, hitboxLeft, hitboxBottom, null)) {
                                collidedLeft = true;
                            }
                            if (Intersector.intersectSegments(cellX1, cellY1, cellX2, cellY2, hitboxRight, hitboxTop, hitboxRight, hitboxBottom, null)) {
                                collidedRight = true;
                            }

                        }
                    }
                }
            }
        }
        // determine the type of collision based on which sides were collided with
        if (collidedTop && !collidedBottom && !collidedLeft && !collidedRight) {
            return "top";
        } else if (collidedBottom && !collidedTop && !collidedLeft && !collidedRight) {
            return "bottom";
        } else if (collidedLeft && !collidedRight && !collidedTop && !collidedBottom) {
            return "left";
        } else if (collidedRight && !collidedLeft && !collidedTop && !collidedBottom) {
            return "right";
        } else if (collidedTop && collidedLeft && !collidedRight && !collidedBottom) {
            return "topLeft";
        } else if (collidedTop && collidedRight && !collidedLeft && !collidedBottom) {
            return "topRight";
        } else if (collidedBottom && collidedLeft && !collidedRight && !collidedTop) {
            return "bottomLeft";
        } else if (collidedBottom && collidedRight && !collidedLeft && !collidedTop) {
            return "bottomRight";
        }
        if (!collidedLeft && !collidedBottom) {
            return "none";
        }
        else {
            return "severalCollisions";
        }

        /*else if (collidedLeft && collidedRight && !collidedBottom && !collidedTop) {
            return "halfWaller";
        } else if (!collidedLeft && !collidedRight && collidedBottom && collidedTop) {
            return "halfWaller2";
        }
        else {
            return "none";
        }*/
    }

    public void dispose() {
        model.batch.dispose();
    }
}
