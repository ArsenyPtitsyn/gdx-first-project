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
    private final float tankSpeed;
    private final float reverseTankSpeed;
    private final float wallTankSpeed;
    private final float tankAngularSpeed;
    private final float wallTankAngularSpeed;
    private final float weaponAngularSpeed;
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
        this.tankSpeed = 180.0f;
        this.reverseTankSpeed = 0.2f * this.tankSpeed;
        this.wallTankSpeed = 0.0f;
        this.tankAngularSpeed = 90.0f;
        this.wallTankAngularSpeed = 0.0f;
        this.weaponAngularSpeed = 90.0f;
        this.scale = 3.0f;
    }

    public void update(float dt) {
        // 1. Не дайте танку выехать за пределы экрана.
        // 1) Найдём координаты краёв танка в следующий момент времени при нажатии на ту или иную кнопку
        // управления:
        // а) Координаты передних краёв при движении танка вперёд.
        float wXFrontLeftCornerCoordinate = x + tankSpeed * MathUtils.cosDeg(angle) * dt +
                tankLength / 2 * MathUtils.cosDeg(angle) - tankHeight / 2 * MathUtils.sinDeg(angle);
        float wYFrontLeftCornerCoordinate = y + tankSpeed * MathUtils.sinDeg(angle) * dt +
                tankLength / 2 * MathUtils.sinDeg(angle) + tankHeight / 2 * MathUtils.cosDeg(angle);
        float wXFrontRightCornerCoordinate = x + tankSpeed * MathUtils.cosDeg(angle) * dt +
                tankLength / 2 * MathUtils.cosDeg(angle) + tankHeight / 2 * MathUtils.sinDeg(angle);
        float wYFrontRightCornerCoordinate = y + tankSpeed * MathUtils.sinDeg(angle) * dt +
                tankLength / 2 * MathUtils.sinDeg(angle) - tankHeight / 2 * MathUtils.cosDeg(angle);
        // б) Координаты задних краёв при движении танка назад.
        float sXRearLeftCornerCoordinate = x - reverseTankSpeed * MathUtils.cosDeg(angle) * dt -
                tankHeight / 2 * MathUtils.sinDeg(angle) - tankLength / 2 * MathUtils.cosDeg(angle);
        float sYRearLeftCornerCoordinate = y - reverseTankSpeed * MathUtils.sinDeg(angle) * dt +
                tankHeight / 2 * MathUtils.cosDeg(angle) - tankLength / 2 * MathUtils.sinDeg(angle);
        float sXRearRightCornerCoordinate = x - reverseTankSpeed * MathUtils.cosDeg(angle) * dt -
                tankLength / 2 * MathUtils.cosDeg(angle) + tankHeight / 2 * MathUtils.sinDeg(angle);
        float sYRearRightCornerCoordinate = y - reverseTankSpeed * MathUtils.sinDeg(angle) * dt -
                tankLength / 2 * MathUtils.sinDeg(angle) - tankHeight / 2 * MathUtils.cosDeg(angle);
        // в) Координаты всех краёв при повороте танка по часовой стрелки.
        // вспомогательные величины.
        float stepAngle = tankAngularSpeed * dt; // угол поворота за время dt при обычной угловой скорости.
        float semi = (float) Math.sqrt((tankLength / 2) * (tankLength / 2) +
                (tankHeight / 2) * (tankHeight / 2)); // полудиагональ танка.
        float diagonalAngle = (float) Math.acos(tankLength / (2 * semi));
        float dXFrontLeftCornerCoordinate = x + semi *
                MathUtils.cosDeg(angle + diagonalAngle - stepAngle);
        float dYFrontLeftCornerCoordinate = y + semi *
                MathUtils.sinDeg(angle + diagonalAngle - stepAngle);
        float dXFrontRightCornerCoordinate = x + semi *
                MathUtils.cosDeg(angle - diagonalAngle - stepAngle);
        float dYFrontRightCornerCoordinate = y + semi *
                MathUtils.sinDeg(angle - diagonalAngle - stepAngle);
        float dXRearLeftCornerCoordinate = x - semi *
                MathUtils.cosDeg(angle - diagonalAngle - stepAngle);
        float dYRearLeftCornerCoordinate = y - semi *
                MathUtils.sinDeg(angle - diagonalAngle - stepAngle);
        float dXRearRightCornerCoordinate = x - semi *
                MathUtils.cosDeg(angle + diagonalAngle - stepAngle);
        float dYRearRightCornerCoordinate = y - semi *
                MathUtils.sinDeg(angle + diagonalAngle - stepAngle);
        // г) Координаты всех краёв при повороте танка против часовой стрелки.
        float aXFrontLeftCornerCoordinate = x + semi *
                MathUtils.cosDeg(angle + diagonalAngle + stepAngle);
        float aYFrontLeftCornerCoordinate = y + semi *
                MathUtils.sinDeg(angle + diagonalAngle + stepAngle);
        float aXFrontRightCornerCoordinate = x + semi *
                MathUtils.cosDeg(angle - diagonalAngle + stepAngle);
        float aYFrontRightCornerCoordinate = y + semi *
                MathUtils.sinDeg(angle - diagonalAngle + stepAngle);
        float aXRearLeftCornerCoordinate = x - semi *
                MathUtils.cosDeg(angle - diagonalAngle + stepAngle);
        float aYRearLeftCornerCoordinate = y - semi *
                MathUtils.sinDeg(angle - diagonalAngle + stepAngle);
        float aXRearRightCornerCoordinate = x - semi *
                MathUtils.cosDeg(angle + diagonalAngle + stepAngle);
        float aYRearRightCornerCoordinate = y - semi *
                MathUtils.sinDeg(angle + diagonalAngle + stepAngle);
        // 2) Если танк при следующей зарисовке выезжает за пределы экрана - останавливаем его, иначе
        // оставляем скорость первоначальной.
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (dXFrontLeftCornerCoordinate < 0 || dXFrontLeftCornerCoordinate > 1000 ||
                    dXFrontRightCornerCoordinate < 0 || dXFrontRightCornerCoordinate > 1000 ||
                    dXRearLeftCornerCoordinate < 0 || dXRearLeftCornerCoordinate > 1000 ||
                    dXRearRightCornerCoordinate < 0 || dXRearRightCornerCoordinate > 1000 ||
                    dYFrontLeftCornerCoordinate < 0 || dYFrontLeftCornerCoordinate > 500 ||
                    dYFrontRightCornerCoordinate < 0 || dYFrontRightCornerCoordinate > 500 ||
                    dYRearLeftCornerCoordinate < 0 || dYRearLeftCornerCoordinate > 500 ||
                    dYRearRightCornerCoordinate < 0 || dYRearRightCornerCoordinate > 500)
                angle -= wallTankAngularSpeed * dt;
            else
                angle -= tankAngularSpeed * dt;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (aXFrontLeftCornerCoordinate < 0 || aXFrontLeftCornerCoordinate > 1000 ||
                    aXFrontRightCornerCoordinate < 0 || aXFrontRightCornerCoordinate > 1000 ||
                    aXRearLeftCornerCoordinate < 0 || aXRearLeftCornerCoordinate > 1000 ||
                    aXRearRightCornerCoordinate < 0 || aXRearRightCornerCoordinate > 1000 ||
                    aYFrontLeftCornerCoordinate < 0 || aYFrontLeftCornerCoordinate > 500 ||
                    aYFrontRightCornerCoordinate < 0 || aYFrontRightCornerCoordinate > 500 ||
                    aYRearLeftCornerCoordinate < 0 || aYRearLeftCornerCoordinate > 500 ||
                    aYRearRightCornerCoordinate < 0 || aYRearRightCornerCoordinate > 500)
                angle += wallTankAngularSpeed * dt;
            else
                angle += tankAngularSpeed * dt;
        }

//        if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
//            angle -= 90.0f;
//        }
//        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
//            angle += 90.0f;
//        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (wXFrontLeftCornerCoordinate < 0 || wXFrontLeftCornerCoordinate > 1000 ||
                    wXFrontRightCornerCoordinate < 0 || wXFrontRightCornerCoordinate > 1000 ||
                    wYFrontLeftCornerCoordinate < 0 || wYFrontLeftCornerCoordinate > 500 ||
                    wYFrontRightCornerCoordinate < 0 || wYFrontRightCornerCoordinate > 500) {
                x += wallTankSpeed * MathUtils.cosDeg(angle) * dt;
                y += wallTankSpeed * MathUtils.sinDeg(angle) * dt;
            } else {
                x += tankSpeed * MathUtils.cosDeg(angle) * dt;
                y += tankSpeed * MathUtils.sinDeg(angle) * dt;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (sXRearLeftCornerCoordinate < 0 || sXRearLeftCornerCoordinate > 1000 ||
                    sYRearLeftCornerCoordinate < 0 || sYRearLeftCornerCoordinate > 500 ||
                    sXRearRightCornerCoordinate < 0 || sXRearRightCornerCoordinate > 1000 ||
                    sYRearRightCornerCoordinate < 0 || sYRearRightCornerCoordinate > 500) {
                x -= wallTankSpeed * MathUtils.cosDeg(angle) * dt;
                y -= wallTankSpeed * MathUtils.sinDeg(angle) * dt;
            } else {
                x -= reverseTankSpeed * MathUtils.cosDeg(angle) * dt;
                y -= reverseTankSpeed * MathUtils.sinDeg(angle) * dt;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            angleWeapon -= weaponAngularSpeed * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            angleWeapon += weaponAngularSpeed * dt;
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
