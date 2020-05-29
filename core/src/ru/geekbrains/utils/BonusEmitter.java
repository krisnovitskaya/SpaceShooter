package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.sprite.Bonus;

public class BonusEmitter {
    private static final float GENERATE_INTERVAL = 8f;
    private Rect worldBounds;
    private float generateTimer;

    private static final float BONUS_HEIGHT = 0.05f;

    private final TextureRegion[] bonusHPRegions;
    private final TextureRegion[] bonusShieldRegions;
    private final TextureRegion[] bonusSpeedRegions;
    private final TextureRegion[] bonusShootRegions;
    private final BonusPool bonusPool;

    private Vector2 tempPos;
    private Vector2 tempV;


    public BonusEmitter(TextureAtlas atlas, BonusPool bonusPool) {
        TextureRegion bonusHP = atlas.findRegion("hpup");
        this.bonusHPRegions = Regions.split(bonusHP, 1, 2, 2);
        TextureRegion bonusShield = atlas.findRegion("shieldup");
        this.bonusShieldRegions = Regions.split(bonusShield, 1, 2, 2);
        TextureRegion bonusSpeed = atlas.findRegion("speedup");
        this.bonusSpeedRegions = Regions.split(bonusSpeed, 1, 2, 2);
        TextureRegion bonusShoot = atlas.findRegion("shootup");
        this.bonusShootRegions = Regions.split(bonusShoot, 1, 2, 2);
        this.bonusPool = bonusPool;
        tempPos = new Vector2();
        tempV = new Vector2();

    }

    public void generate(float delta) {
        generateTimer += delta;
        if (generateTimer >= GENERATE_INTERVAL) {
            generateTimer = 0f;
            Bonus bonus = bonusPool.obtain();
            float type = (float) Math.random();
            if (type < 0.25f) {
                bonus.set(
                        bonusHPRegions,
                        getNewPos(),
                        getNewV(),
                        BONUS_HEIGHT,
                        BonusType.HP
                );
            } else if (type < 0.5f) {
                bonus.set(
                        bonusShieldRegions,
                        getNewPos(),
                        getNewV(),
                        BONUS_HEIGHT,
                        BonusType.SHIELD
                );
            } else if (type < 0.75f) {
                bonus.set(
                        bonusSpeedRegions,
                        getNewPos(),
                        getNewV(),
                        BONUS_HEIGHT,
                        BonusType.SPEED
                );
            } else {
                bonus.set(
                        bonusShootRegions,
                        getNewPos(),
                        getNewV(),
                        BONUS_HEIGHT,
                        BonusType.SHOOT
                );
            }
        }
    }

    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }


    private Vector2 getNewPos() {
        float x = Rnd.nextFloat(-0.25f, 0.25f);
        float y = Rnd.nextFloat(0.44f, 0.4f);
        tempPos.set(x, y);
        return tempPos;
    }

    private Vector2 getNewV() {
        float x = Rnd.nextFloat(-0.05f, 0.05f);
        float y = Rnd.nextFloat(-0.1f, -0.05f);
        tempV.set(x, y);
        return tempV;
    }
}
