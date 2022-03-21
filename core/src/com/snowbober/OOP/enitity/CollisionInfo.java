package com.snowbober.OOP.enitity;

import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.enums.CollisionType;


public class CollisionInfo {
    public Rectangle rectangle;
    public CollisionType collisionType;

    public CollisionInfo(int width, int height) {
        this.rectangle = new Rectangle();
        rectangle.width = width;
        rectangle.height = height;
        this.collisionType = CollisionType.NONE;
    }
}
