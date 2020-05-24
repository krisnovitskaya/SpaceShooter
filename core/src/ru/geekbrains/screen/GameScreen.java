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
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.Logo;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.Starship;
import ru.geekbrains.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private ScreenController screenController;

    private Texture bg;

    private Background background;
    private TextureAtlas atlas;
    private Starship starship;
    private Texture starTexture;   //TO_DO создать пак со своей звездой и прочими текстурами игрового экрана
    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Music music;
    private EnemyEmitter enemyEmitter;


//    Сделать 2 режика корабля: когда он быстро вылетает на экран и когда начинает двигаться со своей скоростью и вести бой.
//    Важно чтобы стрельба началась сразу после того как корабль полностью появится на экране (сейчас маленькие корабли стреляют в самом конце)
//*Сделать проверку столкновения вражеского корабля с нашими пулями и уничтожение вражеского корабля


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
        explosionPool = new ExplosionPool(atlas);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        starship = new Starship(atlas, bulletPool, explosionPool);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.setLooping(true);
        music.play();


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
        enemyEmitter.resize(worldBounds);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        starTexture.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        music.dispose();
        starship.dispose();
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
        enemyPool.updateActiveSprites(delta);
        checkCollisions();
        explosionPool.updateActiveSprites(delta);
        starship.update(delta);
        enemyEmitter.generate(delta);

    }

    private void checkCollisions() {
        for(Enemy enemy : enemyPool.getActiveObjects()){
            for(Bullet bullet : bulletPool.getActiveObjects()){
                if(enemy.isMe(bullet.pos)){
                    if(bullet.getOwner() == starship){
                        bullet.destroy();
                        enemy.takeDamage(bullet.getDamage());
                    }
                }
            }
        }
    }


    private void free() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        starship.draw(batch);
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
