package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Texture background;
    private Texture star;

    private Vector2 starPos;
    private Vector2 touch;
    private Vector2 move;
    private float lenPath;

    @Override
    public void show() {
        super.show();
        background = new Texture("background.jpg");
        star = new Texture("star.png");
        starPos = new Vector2(0,0);
        touch = new Vector2();
        move = new Vector2(0, 0);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        starPos.add(move);
        lenPath = lenPath - move.len();
        if(lenPath < 0.0f){
            move.set(0.0f,0.0f);
            starPos.set(touch.x, touch.y);
        }
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(star, starPos.x, starPos.y);
        batch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        star.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);
        move = touch.cpy().sub(starPos);
        lenPath = move.len();
        move.nor();
        return super.touchDown(screenX, screenY, pointer, button);
    }
}
