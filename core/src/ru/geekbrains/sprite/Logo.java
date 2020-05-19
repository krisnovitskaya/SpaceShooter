package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {
    private Vector2 temp;
    private Vector2 distance;
    private float speed;

    public Logo(Texture texture, ScreenController screenController) {
        super(new TextureRegion(texture),screenController);
        temp = new Vector2();
        distance = new Vector2(this.pos);
        speed = 0.5f;
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



    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }
}
