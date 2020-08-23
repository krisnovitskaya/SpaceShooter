package ru.geekbrains.pool;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Bonus;

public class BonusPool extends SpritesPool<Bonus> {
    private Rect worldBounds;

    public BonusPool(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected Bonus newObject() {
        return new Bonus(worldBounds);
    }
}
