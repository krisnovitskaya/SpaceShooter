package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Texture background;
    private Texture star;

    private Vector2 starPosition;
    private Vector2 vel;

    @Override
    public void show() {
        super.show();
        background = new Texture("background.jpg");
        star = new Texture("star.png");
        starPosition = new Vector2(0,0);
        vel = new Vector2(1,1);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        starPosition.add(vel);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(star, starPosition.x, starPosition.y);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        star.dispose();
        super.dispose();
    }
}
