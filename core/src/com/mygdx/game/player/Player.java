package com.mygdx.game.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.main.Map;
import com.mygdx.game.utilities.Animator;

public class Player{
    //Player Attributes
    private float PLAYER_WIDTH = 98, PLAYER_HEIGHT = 98;

    private float centerX = Gdx.graphics.getWidth() / 2f;
    private float centerY = Gdx.graphics.getHeight() / 2f;

    // Calculate the position to draw the player sprite
    private float playerDrawX = centerX - PLAYER_WIDTH / 2f;
    private float playerDrawY = centerY - PLAYER_HEIGHT / 2f;

    //Player Prerequisites
    public SpriteBatch spriteBatch;
    public Sprite character;
    private Animator animator;
    private float stateTime;
    private int speed;
    private int prev_movement;

    //Player Collision Attributes
    private MapObjects collision_objects;
    private Rectangle player_bounds;
    private float previous_x;
    private float previous_y;

    //Player preloaded textures
    private Texture idle;
    private Texture run;

    private ShapeRenderer shapeRenderer;

    public Player(){
        character = new Sprite(new Texture("assets/Full body animated characters/Char 4/no hands/idle_0.png"));
        character.setScale(-2f);
        spriteBatch = new SpriteBatch();
        animator = new Animator();
        stateTime = 0f;
        speed = 55 * 2;
        prev_movement = 0;

        collision_objects = new Map().getCollissionObjects();
        player_bounds = new Rectangle(playerDrawX, playerDrawY, PLAYER_WIDTH, PLAYER_HEIGHT);
        previous_x = 0;
        previous_y = 0;
        shapeRenderer = new ShapeRenderer();

        idle = new Texture(Gdx.files.internal("animations/idle.png"));
        run = new Texture(Gdx.files.internal("animations/run.png"));
    }

    public void handleMovement(OrthographicCamera camera){
        stateTime += Gdx.graphics.getDeltaTime() * 0.30f;
        TextureRegion currentFrame = null;


          //debugging
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED); // Adjust color as needed
//        shapeRenderer.rect(player_bounds.x, player_bounds.y, player_bounds.width, player_bounds.height);
//        shapeRenderer.end();

        for (MapObject object : collision_objects) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                // Check for collision
                if (Intersector.overlaps(player_bounds, rectangle)) {
                    // Determine the direction of collision (left, right, top, bottom)
                    float overlapX = Math.max(0, Math.min(player_bounds.x + player_bounds.width, rectangle.x + rectangle.width) - Math.max(player_bounds.x, rectangle.x));
                    float overlapY = Math.max(0, Math.min(player_bounds.y + player_bounds.height, rectangle.y + rectangle.height) - Math.max(player_bounds.y, rectangle.y));

                    // Adjust player's position based on the collision direction
                    if (overlapX < overlapY) {
                        // Horizontal collision
                        if (player_bounds.x < rectangle.x) {
                            // Collided from the left
                            character.setX(rectangle.x - player_bounds.width);
                        } else {
                            // Collided from the right
                            character.setX(rectangle.x + rectangle.width);
                        }
                    } else {
                        // Vertical collision
                        if (player_bounds.y < rectangle.y) {
                            // Collided from the bottom
                            character.setY(rectangle.y - player_bounds.height);
                        } else {
                            // Collided from the top
                            character.setY(rectangle.y + rectangle.height);
                        }
                    }
                }
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            previous_x = character.getX();
            character.setX(previous_x -= Gdx.graphics.getDeltaTime() * speed);
            prev_movement = 0;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            currentFrame = animator.animateRun(run).getKeyFrame(stateTime, true);
            previous_x = character.getX();
            character.setX(previous_x += Gdx.graphics.getDeltaTime() * speed);
            prev_movement = 1;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            previous_y = character.getY();
            character.setY(previous_y += Gdx.graphics.getDeltaTime() * speed);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            previous_y = character.getY();
            character.setY(previous_y -= Gdx.graphics.getDeltaTime() * speed);
        }



        spriteBatch.begin();
        if(currentFrame != null){
            spriteBatch.draw(currentFrame, playerDrawX, playerDrawY, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        else{
            TextureRegion idles = animator.animateIdle(idle).getKeyFrame(stateTime, true);
            spriteBatch.draw(idles, playerDrawX, playerDrawY, PLAYER_WIDTH, PLAYER_HEIGHT);
        }
        player_bounds = new Rectangle(character.getX(), character.getY(), 5, 5);
        spriteBatch.end();
    }


}