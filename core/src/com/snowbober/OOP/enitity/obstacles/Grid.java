package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;

public class Grid extends Obstacle {
    public Grid(Position position, int speed) {
        super(position, new Visual(new Texture("rail.png"), 200, 350), speed);
        obstacleType = ObstacleType.GRID;
        collisionInfo = new CollisionInfo(168, 350);
    }

    @Override
    public void collide(Collidable object) {

    }
}
