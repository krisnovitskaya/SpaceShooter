package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;

public class ButtonMusic extends ScaledButton {
    private Music music;

    public ButtonMusic(TextureAtlas atlas, Music music) {
        super(atlas.findRegion("music_button"), 1, 2, 2);
        this.music = music;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.05f);
        setTop(worldBounds.getTop() - getHeight());
        setRight(worldBounds.getRight());
    }

    @Override
    public void action() {
        frame = frame == 0 ? 1 : 0;
        if (frame == 1) {
            music.pause();
        } else {
            music.play();
        }
    }
}
