package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Tank {
    private final float tankLength = 40.0f;
    private final float tankHeight = 40.0f;

    private final Texture texture;
    private final Texture textureWeapon;
    private float x;
    private float y;
    private final float speed;
    private float angle;
    private float angleWeapon;
    private final Projectile projectile;
    private final float scale;

    public Tank() {
        this.texture = new Texture("tank.png");
        this.textureWeapon = new Texture("weapon.png");
        this.projectile = new Projectile();
        this.x = 100.0f;
        this.y = 100.0f;
        this.speed = 240.0f;
        this.scale = 3.0f;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 90.0f * dt;
        }

        float[] REAR_RIGHT_CORNER_COORDINATES = {x, y};
        float[] REAR_LEFT_CORNER_COORDINATES = {x - tankHeight * MathUtils.sinDeg(angle),
                y + tankLength * MathUtils.cosDeg(angle)};
        float[] FRONT_RIGHT_CORNER_COORDINATES = {x + tankLength * MathUtils.cosDeg(angle),
                y + tankLength * MathUtils.sinDeg(angle)};
        float[] FRONT_LEFT_CORNER_COORDINATES = {x + tankLength * MathUtils.cosDeg(angle) -
                tankHeight * MathUtils.sinDeg(angle), y + tankLength * MathUtils.sinDeg(angle) +
                tankLength * MathUtils.cosDeg(angle)};
//        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            angle -= 90.0f;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            angle += 90.0f;
//        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            x += speed * MathUtils.cosDeg(angle) * dt;
            y += speed * MathUtils.sinDeg(angle) * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            x -= speed * MathUtils.cosDeg(angle) * dt * 0.2f;
            y -= speed * MathUtils.sinDeg(angle) * dt * 0.2f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            angleWeapon -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            angleWeapon += 90.0f * dt;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !projectile.isActive()) {
            projectile.shoot(x + 16 * scale * MathUtils.cosDeg(angle + angleWeapon),
                    y + 16 * scale * MathUtils.sinDeg(angle + angleWeapon), angle + angleWeapon);
        }
        if (projectile.isActive()) {
            projectile.update(dt);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x - tankLength / 2, y - tankHeight / 2, tankLength / 2, tankHeight / 2,
                tankLength, tankHeight, scale, scale, angle, 0, 0, 40, 40, false, false);
        batch.draw(textureWeapon, x - tankLength / 2, y - tankHeight / 2, tankLength / 2, tankHeight / 2,
                tankLength, tankHeight, scale, scale, angle + angleWeapon, 0, 0, 40, 40, false, false);
        if (projectile.isActive()) {
            projectile.render(batch);
        }
    }

    public void dispose() {
        texture.dispose();
        projectile.dispose();
    }
}
