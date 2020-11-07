package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Tank {
    private final Texture texture;
    private final Texture textureWeapon;
    private float x;
    private float y;
    private final float speed;
    private float angle;
    private float angleWeapon;
    private final Projectile projectile;
    private final float scale;

//    private final float X_REAR_LEFT_CORNER = x - tankHeight * MathUtils.sin(angle);
//    private final float Y_REAR_LEFT_CORNER = y + tankHeight * MathUtils.cos(angle);
//    private final float X_REAR_RIGHT_CORNER = x;
//    private final float Y_REAR_RIGHT_CORNER = y;
//    private final float X_FRONT_LEFT_CORNER = x + tankLength * MathUtils.cos(angle);
//    private final float Y_FRONT_LEFT_CORNER = y + tankLength * MathUtils.sin(angle);
//    private final float X_FRONT_RIGHT_CORNER = x + tankLength * MathUtils.cos(angle) -
//            tankHeight * MathUtils.sin(angle);
//    private final float Y_FRONT_RIGHT_CORNER = y + tankLength * MathUtils.sin(angle) +
//            tankHeight * MathUtils.cos(angle);
//
//    private final float[] xCornerCoordinates = {X_REAR_LEFT_CORNER, X_REAR_RIGHT_CORNER,
//            X_FRONT_LEFT_CORNER, X_FRONT_RIGHT_CORNER};
//    private final float[] yCornerCoordinates = {Y_REAR_LEFT_CORNER, Y_REAR_RIGHT_CORNER,
//            Y_FRONT_LEFT_CORNER, Y_FRONT_RIGHT_CORNER};

    public Tank() {
        this.texture = new Texture("tank.png");
        this.textureWeapon = new Texture("weapon.png");
        this.projectile = new Projectile();
        this.x = 100.0f;
        this.y = 100.0f;
        this.speed = 240.0f;
        this.scale = 2.0f;
    }

    public void update(float dt) {
        // 1. Не дайте танку уехать за пределы экрана
        // Если какой-нибудь из углов танка упёрся в какую-нибудь стенку - остановить танк.
        // Простейшая реализация (без физики).
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 90.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 90.0f * dt;
        }
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
        batch.draw(texture, x - 20, y - 20, 20, 20, 40, 40, scale, scale, angle,
                0, 0, 40, 40, false, false);
        batch.draw(textureWeapon, x - 20, y - 20, 20, 20, 40, 40, scale, scale, angle + angleWeapon,
                0, 0, 40, 40, false, false);
        if (projectile.isActive()) {
            projectile.render(batch);
        }
    }

    public void dispose() {
        texture.dispose();
        projectile.dispose();
    }
}
