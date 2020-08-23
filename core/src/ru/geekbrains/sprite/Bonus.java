package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.utils.BonusType;


public class Bonus extends Sprite {
    private Vector2 velocity;
    private Rect worldBounds;

    private Vector2 tempPos = new Vector2();
    private final Vector2 Y = new Vector2(0, -0.5f);
    private final float ANIMATE_TIME = 0.1f;
    private float animateTimer;
    private BonusType bonusType;


    public Bonus(Rect worldBounds) {
        this.worldBounds = worldBounds;
        animateTimer = 0;
        velocity = new Vector2();
    }

    public void set(
            TextureRegion[] regions,
            Vector2 pos0,
            Vector2 v0,
            float height,
            BonusType bonusType
    ) {
        this.regions = regions;
        this.pos.set(pos0);
        this.velocity.set(v0);
        setHeightProportion(height);
        this.bonusType = bonusType;
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer > ANIMATE_TIME) {
            frame = frame == 0 ? 1 : 0;
            animateTimer = 0;
        }
        tempPos.set(pos);
        pos.mulAdd(velocity, delta);
        if (getLeft() <= worldBounds.getLeft()) {
            pos.set(tempPos);
            velocity.rotate(2 * velocity.angle(Y));
        }
        if (getRight() >= worldBounds.getRight()) {
            pos.set(tempPos);
            velocity.rotate(2 * velocity.angle(Y));
        }

        if (getTop() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public BonusType getBonusType() {
        return bonusType;
    }
}
