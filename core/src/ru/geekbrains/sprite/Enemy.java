package ru.geekbrains.sprite;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;


public class Enemy extends Ship {
    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds, Sound sound) {
        super(bulletPool, explosionPool, worldBounds, sound);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            Vector2 v0,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            int hp,
            float height
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        this.v.set(v0);
    }

//    private Vector2 velocity;
//    private Rect worldBounds;
//
//    private Vector2 tempV = new Vector2();
//    private Vector2 tempPos = new Vector2();
//    private Vector2 temp = new Vector2();
//    private final Vector2 Y = new Vector2(0, -0.5f);
//
//
//    public Enemy(){
//        regions = new TextureRegion[2];
//        velocity = new Vector2();
//    }
//
//    public void set(
//            TextureRegion region,
//            Vector2 pos0,
//            Vector2 v0,
//            float height,
//            Rect worldBounds
//    ) {
//        this.regions = Regions.split(region, 1, 2, 2);
//        this.pos.set(pos0);
//        this.velocity.set(v0);
//        setHeightProportion(height);
//        this.worldBounds = worldBounds;
//    }
//
//    @Override
//    public void update(float delta) {
//        tempPos.set(pos);
//        pos.mulAdd(velocity, delta);
//        if(getLeft() <= worldBounds.getLeft()){
//            pos.set(tempPos);
//            velocity.rotate(2 * velocity.angle(Y));
//        }
//        if (getRight() >= worldBounds.getRight()){
//            pos.set(tempPos);
//            velocity.rotate(2 * velocity.angle(Y));
//        }
//
//        if (getTop() <= worldBounds.getBottom()) {
//            destroy();
//        }
//    }
//
//
//    public Vector2 getNewPos(){
//        float x = Rnd.nextFloat(-0.25f, 0.25f);
//        float y = Rnd.nextFloat(0.44f, 0.4f);
//        tempPos.set(x, y);
//        return tempPos;
//    }
//
//    public Vector2 getNewV(){
//        float x = Rnd.nextFloat(-0.05f,0.05f);
//        float y = Rnd.nextFloat(-0.1f, -0.05f);
//        tempV.set(x, y);
//        return tempV;
//    }
}
