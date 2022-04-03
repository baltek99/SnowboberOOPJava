package com.snowbober.OOP.enitity;

import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;
import com.snowbober.OOP.interfaces.Movable;

public abstract class Obstacle extends EntityWithTexture implements Movable, Collidable {

    protected CollisionInfo collisionInfo;
    protected ObstacleType obstacleType;
    protected int speed;

    public Obstacle(Position position, Visual visual, int speed) {
        super(position, visual);
        this.zIndex = 0;
        this.speed = speed;
        collisionInfo = new CollisionInfo(visual.getImgWidth(), visual.getImgHeight());
    }

    public CollisionInfo getCollisionInfo() {
        return collisionInfo;
    }

    @Override
    public void move(long gameFrame) {
        this.position.setX(this.position.getX() + speed);
    }
}
