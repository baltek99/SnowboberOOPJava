package com.snowbober.OOP.enitity;

import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.interfaces.Movable;

public abstract class MovableEntity extends EntityWithTexture implements Movable {
    protected int speed;

    public MovableEntity(Position position, Visual visual) {
        super(position, visual);
        this.speed = 0;
    }

    public MovableEntity(Position position, Visual visual, int speed) {
        super(position, visual);
        this.speed = speed;
    }

    @Override
    public void move(long gameFrame) {
        this.position.setX(this.position.getX() + speed);
    }
}
