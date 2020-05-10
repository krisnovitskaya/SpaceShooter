package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;

public class Background extends Sprite {
    public Background(Texture texture, ScreenController screenController) {
        super(new TextureRegion(texture), screenController);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(1f);
        this.pos.set(worldBounds.pos);
    }
}
