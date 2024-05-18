package com.mygdx.game.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

public class EnemyHandler {
    public SpriteBatch spriteBatch;
    private ArrayList<Enemy> enemies;
    private Random random;

    private long lastSpawnTime;
    private static final long SPAWN_INTERVAL = 3000; // 3 seconds in milliseconds

    // ZOMBIE TEXTURES
    private Texture[] zombieTextures;

    public EnemyHandler() {
        spriteBatch = new SpriteBatch();
        random = new Random();
        enemies = new ArrayList<>();
        lastSpawnTime = TimeUtils.millis();

        // Initialize zombie textures
        zombieTextures = new Texture[] {
                new Texture(Gdx.files.internal("assets/enemies/Zombie 1/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 2/Idle/0_Zombie_Villager_Idle_000.png")),
                new Texture(Gdx.files.internal("assets/enemies/Zombie 3/Idle/0_Zombie_Villager_Idle_000.png"))
        };
    }

    public void handleWave(OrthographicCamera camera) {
        spriteBatch.begin();
        if (TimeUtils.timeSinceMillis(lastSpawnTime) >= SPAWN_INTERVAL) {
            spawnEnemies();
            lastSpawnTime = TimeUtils.millis();
        }

        spriteBatch.setProjectionMatrix(camera.combined);
        for (Enemy enemy : enemies) {
            spriteBatch.draw(enemy.enemy_texture, enemy.position.x, enemy.position.y, 45, 45);
        }
        spriteBatch.end();
    }

    public void spawnEnemies() {
        for (int i = 0; i < 3; i++) {
            Texture randomTexture = zombieTextures[random.nextInt(zombieTextures.length)];
            enemies.add(new Enemy(randomTexture, rand()));
        }
    }

    public Vector2 rand() {
        Vector2 random_position = new Vector2();
        int random_x = random.nextInt(450 - 50) + 50;
        int random_y = random.nextInt(300 - 50) + 50;

        random_position.set(random_x, random_y);
        return random_position;
    }
}
