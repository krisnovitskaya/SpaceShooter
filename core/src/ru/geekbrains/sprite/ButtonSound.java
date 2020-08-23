package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonSound extends ScaledButton {
    GameScreen gameScreen;

    public ButtonSound(TextureAtlas atlas, GameScreen gameScreen) {
        super(atlas.findRegion("sound_button"), 1, 2, 2);
        this.gameScreen = gameScreen;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(worldBounds.getTop() - 2 * getHeight());
        setRight(worldBounds.getRight());

    }

    @Override
    public void action() {
        frame = frame == 0 ? 1 : 0;
        gameScreen.mute(frame);
    }
}
