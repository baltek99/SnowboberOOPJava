package com.snowbober.OOP.enitity;

import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.MovableEntity;
import com.snowbober.OOP.enums.ObstacleType;

public abstract class Obstacle extends MovableEntity {

    protected Rectangle rectangle;
    protected ObstacleType obstacleType;

    public Obstacle(Position position, Visual visual, int speed) {
        super(position, visual, speed);
    }

    protected abstract void createRectangle();
}
