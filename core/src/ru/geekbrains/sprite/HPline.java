package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class HPline extends Sprite {
    private Starship myShip;

    public HPline(TextureAtlas atlas, Starship myShip) {
        super(atlas.findRegion("hp"));
        this.myShip = myShip;
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeight(0.01f);
    }

    @Override
    public void update(float delta) {
        float width = myShip.getWidth() * ((float)myShip.getHp()/myShip.getHpMax());
        setWidth(width);
        pos.set(myShip.getLeft() + width/2, myShip.getBottom() -  getHeight());
    }
}
