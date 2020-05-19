package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;

public class ButtonExit extends Sprite {
    public ButtonExit(TextureAtlas atlas) {
        super(atlas.findRegion("exit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        this.pos.set(0.15f, -0.35f);
    }
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(isMe(touch)){
            setScale(1.2f);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(!isMe(touch) && getScale() != 1f){
            setScale(1f);
        }
        if(isMe(touch)){
            Gdx.app.exit();
        }
        return false;
    }
}
