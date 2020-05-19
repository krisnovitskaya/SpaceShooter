package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.Logo;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.Starship;

public class GameScreen extends BaseScreen {

    private ScreenController screenController;

    private Texture bg;

    private Background background;
    private TextureAtlas atlas;
    private Starship starship;
    private Texture starTexture;   //TO_DO создать пак со своей звездой и прочими текстурами игрового экрана
    private Star[] stars;
    private BulletPool bulletPool;
    private Music music;

    private EnemyPool enemies;
    private float enemiesTimer;
    private TextureRegion test1;
    private Texture test;





    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(bg);
        atlas = new TextureAtlas("mainAtlas.tpack");
        starTexture = new Texture("star.png");
        stars = new Star[32];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(starTexture);
        }
        bulletPool = new BulletPool();
        starship = new Starship(atlas, bulletPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.play();
        music.setLooping(true);

        enemies = new EnemyPool();
        enemiesTimer = Rnd.nextFloat(1f, 2f);;
        test = new Texture("test.jpg");
        test1 = new TextureRegion(test);


    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        free();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        starship.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemies.dispose();
        starship.getSound().dispose();
        music.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        starship.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        starship.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        starship.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        starship.touchUp(touch, pointer, button);
        return false;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        bulletPool.updateActiveSprites(delta);
        starship.update(delta);
        enemiesUpd(delta);
        enemies.updateActiveSprites(delta);

    }
    private void enemiesUpd(float delta){
        enemiesTimer -= delta;
        if (enemiesTimer <= 0f) {
            Enemy en = enemies.obtain();
            en.set(atlas.findRegion("enemy1"), en.getNewPos(), en.getNewV(), 0.1f, this.getWorldBounds());
            enemiesTimer = Rnd.nextFloat(0.5f, 3f);
        }
    }

    private void free() {
        bulletPool.freeAllDestroyed();
        enemies.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemies.drawActiveSprites(batch);
        starship.draw(batch);
        batch.end();
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
