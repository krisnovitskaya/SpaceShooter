package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.utils.BonusType;

public class Starship extends Ship {

    private static final float SIZE = 0.15f;
    private static final float MARGIN = 0.05f;
    private static final int INVALID_POINTER = -1;
    private static final int HP_MAX = 100;
    private static final int CLEAN_LEVEL_MAX = 3;

    private int leftPointer;
    private int rightPointer;

    private boolean pressedLeft;
    private boolean pressedRight;

    private int level;
    private int cleanLevelCounter;
    private BonusType bonusType;
    private final int BONUS_HP = 20;
    private final float BONUS_TIME = 10f;
    private final float RELOAD_INTERVAL = 0.25f;
    private final float BONUS_RELOAD_INTERVAL = RELOAD_INTERVAL * 0.3f;
    private float bonusTimer;
    private Shield shield;

    private Vector2 temp;

    @Override
    public void damage(int damage) {
        if (bonusType != BonusType.SHIELD) {
            super.damage(damage);
            cleanLevelCounter = 0;
        }
    }

    public Starship(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("starship2"), 1, 3, 3);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        bulletRegion = atlas.findRegion("bulletStarship");
        bulletV = new Vector2(0, 0.5f);
        bulletHeight = 0.01f;
        damage = 1;
        v0.set(0.5f, 0);
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        reloadInterval = RELOAD_INTERVAL;
        reloadTimer = reloadInterval;
        hp = HP_MAX;
        sound = Gdx.audio.newSound(Gdx.files.internal("sound/laser.wav"));
        shield = new Shield(atlas, BONUS_TIME);
        startNewGame();
    }

    public void startNewGame() {
        hp = HP_MAX;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        pressedLeft = false;
        pressedRight = false;
        cleanLevelCounter = 0;
        level = 1;
        stop();
        this.pos.x = 0;
        bonusType = BonusType.NONE;
        temp = new Vector2();
        flushDestroy();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(SIZE);
        setBottom(worldBounds.getBottom() + MARGIN);
        shield.resize(worldBounds, getHeight());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (bonusType == BonusType.SPEED || bonusType == BonusType.SHIELD || bonusType == BonusType.SHOOT) {
            bonusCheckAndUpdate(delta);
        }
        bulletPos.set(pos.x, pos.y + getHalfHeight());
        autoShoot(delta);
        if (getLeft() < worldBounds.getLeft()) {
            stop();
            setLeft(worldBounds.getLeft());
        }
        if (getRight() > worldBounds.getRight()) {
            stop();
            setRight(worldBounds.getRight());
        }
        if (cleanLevelCounter >= CLEAN_LEVEL_MAX) {
            frame = 2;
        }

    }

    private void bonusCheckAndUpdate(float delta) {
        switch (bonusType) {
            case SPEED:
                this.reloadInterval = BONUS_RELOAD_INTERVAL;
                break;
            case SHIELD:
                shield.pos.set(this.pos);
                this.reloadInterval = RELOAD_INTERVAL;
                break;
            case SHOOT:
                this.reloadInterval = RELOAD_INTERVAL;
        }
        bonusTimer -= delta;
        if (bonusTimer <= 0) {
            this.bonusType = BonusType.NONE;
            this.reloadInterval = RELOAD_INTERVAL;
        }
    }

    @Override
    protected void shoot() {
        super.shoot();
        if (bonusType == BonusType.SHOOT) {
            Bullet bullet = bulletPool.obtain();
            bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
            Bullet bullet2 = bulletPool.obtain();
            bulletPos.set(pos.x + getHalfWidth(), pos.y + getHalfHeight());
            bullet2.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
            Bullet bullet3 = bulletPool.obtain();
            bulletPos.set(pos.x - getHalfWidth(), pos.y + getHalfHeight());
            bullet3.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
            sound.play();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                shoot();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    public void dispose() {
        sound.dispose();
    }

    public boolean isBulletCollision(Bullet bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > pos.y
                || bullet.getTop() < getBottom()
        );
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public int getHpMax() {
        return HP_MAX;
    }

    public void checkImba(int frags) {
        int temp = frags / 10 + 1;
        if (temp > level) {
            level = temp;
            cleanLevelCounter++;
        }
    }

    public void bonusActivate(BonusType bonusType) {
        if (bonusType == BonusType.HP) {
            hp += BONUS_HP;
            if (hp > HP_MAX) {
                hp = HP_MAX;
            }
        } else {
            this.bonusType = bonusType;
            bonusTimer = BONUS_TIME;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        if (bonusType == BonusType.SHIELD) {
            shield.draw(batch);
        }
    }
}
