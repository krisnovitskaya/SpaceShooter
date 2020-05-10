package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.ButtonExit;
import ru.geekbrains.sprite.ButtonPlay;
import ru.geekbrains.sprite.Logo;

//Адаптировать ДЗ 2 к новой архитектуре проекта.
// Желательно всю логику которая касается обработки логотипа по максимуму разместить в классе Logo
//*** Реализовать кнопки Exit и Play с помощью спрайтов.
// Exit должна осуществлять выход из игры, а Play переключать на новый экран.
// Кнопки должны графически реагировать на нажатия

public class MenuScreen extends BaseScreen {
    private ScreenController screenController;
    private Texture bg;
    private Background background;
    private Texture txLogo;
    private Logo logo;
    private Texture exit;
    private ButtonExit bExit;
    private Texture play;
    private ButtonPlay bPlay;


    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(bg, screenController);
        exit = new Texture("exit.jpg");
        bExit = new ButtonExit(exit, screenController);
        play = new Texture("play.jpg");
        bPlay = new ButtonPlay(play, screenController);
        txLogo = new Texture("star.png");
        logo = new Logo(txLogo, screenController);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
//        update(delta);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        bPlay.draw(batch);
        bExit.draw(batch);

        batch.end();
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        bPlay.touchDown(touch, pointer, button);
        bExit.touchDown(touch, pointer, button);
//        logo.touchDown(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        bPlay.touchUp(touch, pointer, button);
        bExit.touchUp(touch, pointer, button);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public void dispose() {
        bg.dispose();
        exit.dispose();
        play.dispose();
        txLogo.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        bPlay.resize(worldBounds);
        bExit.resize(worldBounds);
        logo.resize(worldBounds);
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
