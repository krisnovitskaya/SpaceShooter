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
        if(this.hp <= 0 || getBottom() <= worldBounds.getBottom()){
            destroy();
        }
//        if (getBottom() <= worldBounds.getBottom()) {
//            destroy();
//        }
    }
    public void takeDamage(int damage){
        this.hp -= damage;
    }

    @Override
    protected boolean checkStartPosition() {
        if(this.getTop() > worldBounds.getTop()){
            return true;
        }
        return false;
    }

    public void set(
            TextureRegion[] regions,
//            Vector2 v0,
            Vector2 v,
            TextureRegion bulletRegion,
            float bulletHeight,
            float bulletVY,
            int damage,
            float reloadInterval,
            int hp,
            float height
    ) {
        this.regions = regions;
//        this.v0.set(v0);
        this.v.set(v);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.hp = hp;
        setHeightProportion(height);
        //this.v0.set(v);
    }


//    private final Vector2 Y = new Vector2(0, -0.5f);

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
//    public Vector2 getNewPos(){
//        float x = Rnd.nextFloat(-0.25f, 0.25f);
//        float y = Rnd.nextFloat(0.44f, 0.4f);
//        tempPos.set(x, y);
//        return tempPos;
//    }
//    public Vector2 getNewV(){
//        float x = Rnd.nextFloat(-0.05f,0.05f);
//        float y = Rnd.nextFloat(-0.1f, -0.05f);
//        tempV.set(x, y);
//        return tempV;
//    }
}
