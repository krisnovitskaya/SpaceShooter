package ru.geekbrains.sprite;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;


import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Shield extends Sprite {
    private final float ANIMATE_TIME;
    private float animateTimer;


    public Shield(TextureAtlas atlas, float BONUS_TIMER) {
        super(atlas.findRegion("shield"), 1, 2, 2);
        ANIMATE_TIME = BONUS_TIMER;
    }

    @Override
    public void update(float delta) {

        animateTimer += delta;
        if (animateTimer > ANIMATE_TIME) {
            frame = frame == 0 ? 1 : 0;
            animateTimer = 0;
        }
    }


    public void resize(Rect worldBounds, float height) {
        super.resize(worldBounds);
        setHeightProportion(height);
    }
}
