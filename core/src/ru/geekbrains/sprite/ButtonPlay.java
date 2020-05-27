package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.SpaceShooter;
import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends ScaledButton {
    ScreenController screenController;
    private static final float MARGIN = 0.05f;

    public ButtonPlay(TextureAtlas atlas, ScreenController screenController) {
        super(atlas.findRegion("play"));
        this.screenController = screenController;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setBottom(worldBounds.getBottom() + MARGIN);
        setLeft(worldBounds.getLeft() + MARGIN);
    }
    @Override
    public void action() {
        screenController.setGameScreen();
    }
}
