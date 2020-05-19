package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.SpaceShooter;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends Sprite {


    public ButtonPlay(TextureAtlas atlas, ScreenController screenController) {
        super(atlas.findRegion("play"), screenController);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        this.pos.set(-0.15f, -0.35f);
    }



    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(isMe(touch)){
            setScale(1.2f);
        }
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(!isMe(touch) && getScale() != 1f){
            setScale(1f);
        }
        if(isMe(touch)){
            screenController.setGameScreen();
        }
        return super.touchUp(touch, pointer, button);
    }
}
