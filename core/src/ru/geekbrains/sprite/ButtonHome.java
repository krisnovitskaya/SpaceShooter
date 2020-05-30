package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;

public class ButtonHome extends ScaledButton {

    public ButtonHome(TextureAtlas atlas) {
        super(atlas.findRegion("menu"));

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(worldBounds.getTop());
        setRight(worldBounds.getRight());
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
