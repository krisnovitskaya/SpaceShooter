package ru.geekbrains.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Logo;

public class GameScreen extends BaseScreen {

    private ScreenController screenController;

    private Texture bg;
    private Background background;
    private Texture txLogo;
    private Logo logo;



    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(bg, screenController);
        txLogo = new Texture("star.png");
        logo = new Logo(txLogo, screenController);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);


        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {

        logo.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public void dispose() {
        bg.dispose();

        txLogo.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        logo.resize(worldBounds);
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

}
