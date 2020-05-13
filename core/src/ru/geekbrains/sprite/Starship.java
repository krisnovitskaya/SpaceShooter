package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;

public class Starship extends Sprite {

    private Vector2 temp;
    private Vector2 distance;
    private float speed;


    public Starship(TextureAtlas atlas, ScreenController screenController) {
        super(atlas.findRegion("main_ship"), screenController);
        TextureRegion buf = atlas.findRegion("main_ship");
        TextureRegion[][] reg = buf.split(buf.getRegionWidth()/2, buf.getRegionHeight());
        this.regions[0] = reg[0][0];
        this.temp = new Vector2();
        this.distance = new Vector2(this.pos);
        this.speed = 0.5f;

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
        this.pos.set(worldBounds.pos);
    }
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        distance.set(touch);
        temp.set(distance).sub(pos).nor().scl(speed);
        return super.touchDown(touch, pointer, button);
    }

    @Override
    public void update(float delta) {
        if(pos.dst(distance) > speed * delta){
            pos.mulAdd(temp, delta);
        } else {
            pos.set(distance);
        }
    }
}
