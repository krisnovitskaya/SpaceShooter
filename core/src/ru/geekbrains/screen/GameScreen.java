package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.base.Font;
import ru.geekbrains.base.Ship;
import ru.geekbrains.controller.ScreenController;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BonusPool;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bonus;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonHome;
import ru.geekbrains.sprite.ButtonMusic;
import ru.geekbrains.sprite.ButtonNewGame;
import ru.geekbrains.sprite.ButtonSound;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.Explosion;
import ru.geekbrains.sprite.GameOver;
import ru.geekbrains.sprite.HPline;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.sprite.Starship;
import ru.geekbrains.utils.BonusEmitter;
import ru.geekbrains.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private ScreenController screenController;

    private static final float TEXT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private enum State {PLAYING, GAME_OVER;}
    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Starship starship;
    private Star[] stars;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private BonusPool bonusPool;
    private BonusEmitter bonusEmitter;
    private Music music;
    private EnemyEmitter enemyEmitter;
    private State state;
    private GameOver gameOver;
    private ButtonNewGame bNewGame;
    private ButtonMusic buttonMusic;
    private ButtonSound buttonSound;
    private ButtonHome buttonHome;
    private int frags;
    private Font font;
    private StringBuilder sbFrags;
    private HPline hPline;


    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        background = new Background(bg);
        atlas = new TextureAtlas("texture/gameatlas.pack");
        stars = new Star[32];
        for (int i = 0; i < stars.length; i++) {
            stars[i] = new Star(atlas);
        }
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        starship = new Starship(atlas, bulletPool, explosionPool);
        hPline = new HPline(atlas, starship);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool);
        bonusPool = new BonusPool(worldBounds);
        bonusEmitter = new BonusEmitter(atlas, bonusPool);
        gameOver = new GameOver(atlas);
        bNewGame = new ButtonNewGame(atlas, this);
        font = new Font("font/font.fnt", "font/font.png");
        sbFrags = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.mp3"));
        music.setLooping(true);
        music.play();
        state = State.PLAYING;
        buttonHome = new ButtonHome(atlas);
        buttonMusic = new ButtonMusic(atlas, music);
        buttonSound = new ButtonSound(atlas, this);
        mute(0);
    }

    public void mute(int frame) {
        Ship.setMute(frame);
        Explosion.setMute(frame);
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
        hPline.resize(worldBounds);
        enemyEmitter.resize(worldBounds);
        bonusEmitter.resize(worldBounds);
        gameOver.resize(worldBounds);
        bNewGame.resize(worldBounds);
        buttonSound.resize(worldBounds);
        buttonHome.resize(worldBounds);
        buttonMusic.resize(worldBounds);
        font.setSize(FONT_SIZE);
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        bonusPool.dispose();
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
        if (!buttonHome.isMe(touch) && !buttonSound.isMe(touch) && !buttonMusic.isMe(touch)) {
            if (state == State.PLAYING) {
                starship.touchDown(touch, pointer, button);
            }
            if (state == State.GAME_OVER) {
                bNewGame.touchDown(touch, pointer, button);
            }
        } else {
            buttonHome.touchDown(touch, pointer, button);
            buttonMusic.touchDown(touch, pointer, button);
            buttonSound.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonHome.touchUp(touch, pointer, button);
        buttonMusic.touchUp(touch, pointer, button);
        buttonSound.touchUp(touch, pointer, button);
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
        bonusPool.freeAllActiveObjects();
        state = State.PLAYING;
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING) {
            starship.update(delta);
            hPline.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
            bonusPool.updateActiveSprites(delta);
            bonusEmitter.generate(delta);
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
        List<Bonus> bonusList = bonusPool.getActiveObjects();
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
        for (Bonus bonus : bonusList) {
            float minDist = bonus.getHalfWidth() + starship.getHalfWidth();
            if (starship.pos.dst(bonus.pos) < minDist) {
                starship.bonusActivate(bonus.getBonusType());
                bonus.destroy();
            }
        }
        if (starship.isDestroyed()) {
            state = State.GAME_OVER;
        }
        starship.checkImba(frags);
    }


    private void free() {
        bulletPool.freeAllDestroyed();
        enemyPool.freeAllDestroyed();
        explosionPool.freeAllDestroyed();
        bonusPool.freeAllDestroyed();
    }

    private void draw() {
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        buttonMusic.draw(batch);
        buttonHome.draw(batch);
        buttonSound.draw(batch);
        if (state == State.PLAYING) {
            starship.draw(batch);
            hPline.draw(batch);
            bonusPool.drawActiveSprites(batch);
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
        font.draw(batch, sbFrags.append(FRAGS).append(frags).append("\n").append(HP).append(starship.getHp()).append("\n").append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getLeft() + TEXT_MARGIN, worldBounds.getTop() - TEXT_MARGIN);
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }
}
