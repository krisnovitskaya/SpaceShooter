package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.screen.MenuScreen;

public class ButtonNewGame extends ScaledButton {
    private ScreenController screenController;
    private GameScreen gameScreen;
    private static final float MARGIN = 0.05f;

    public ButtonNewGame(TextureAtlas atlas, ScreenController screenController, GameScreen gameScreen) {
        super(atlas.findRegion("button_new_game"));
        this.screenController = screenController;
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.02f);
        setBottom(worldBounds.getBottom() + MARGIN);
    }

    @Override
    public void action() {
//        screenController.setMenuScreen(); //вариант с переходом в меню
        gameScreen.newGame();
    }
}
