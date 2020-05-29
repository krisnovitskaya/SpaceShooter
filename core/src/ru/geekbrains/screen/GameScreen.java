package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.GameOver;
import ru.geekbrains.sprite.Logo;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.Starship;
import ru.geekbrains.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private ScreenController screenController;

    private static final float TEXT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, GAME_OVER}
    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Starship starship;
    private Texture starTexture;
    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Music music;
    private EnemyEmitter enemyEmitter;
    private State state;
    private GameOver gameOver;
    private ButtonNewGame bNewGame;
    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;




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
        gameOver = new GameOver(atlas);
        bNewGame = new ButtonNewGame(atlas, screenController, this);
        font = new Font("font/font.fnt", "font/font.png");
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.setLooping(true);
        music.play();
        state = State.PLAYING;

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
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
        gameOver.resize(worldBounds);
        bNewGame.resize(worldBounds);
        font.setSize(FONT_SIZE);
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
        font.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING) {
            starship.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(state == State.PLAYING) {
            starship.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(state == State.PLAYING) {
            starship.touchDown(touch, pointer, button);
        }
        if(state == State.GAME_OVER){
            bNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(state == State.PLAYING) {
            starship.touchUp(touch, pointer, button);
        }
        if(state == State.GAME_OVER){
            bNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    public void startNewGame() {
        frags = 0;
        starship.startNewGame();
        bulletPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        state = State.PLAYING;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING) {
            starship.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER) {
            bNewGame.update(delta);
        }

    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            float minDist = enemy.getHalfWidth() + starship.getHalfWidth();
            if (starship.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
                starship.damage(enemy.getDamage());
                continue;
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != starship ||  bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) {
                        frags += 1;
                    }
                }
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == starship || bullet.isDestroyed()) {
                continue;
            }
            if (starship.isBulletCollision(bullet)) {
                starship.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
        if (starship.isDestroyed()) {
            state = State.GAME_OVER;
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
        if (state == State.PLAYING) {
            starship.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else if (state == State.GAME_OVER) {
            gameOver.draw(batch);
            bNewGame.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN);
        font.draw(batch, sbHp.append(HP).append(starship.getHp()), worldBounds.pos.x, worldBounds.getTop() - TEXT_MARGIN, Align.center);
        font.draw(batch, sbHp, starship.pos.x, starship.getBottom() - TEXT_MARGIN, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN, Align.right);
    }
    public ScreenController getScreenController() {
        return screenController;
    }



    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
