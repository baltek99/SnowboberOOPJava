package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;

public class Rail extends Obstacle {

    public Rail(Position position, int speed) {
        super(position, new Visual(new Texture("rail.png"), ConstValues.RAIL_WIDTH, ConstValues.RAIL_HEIGHT), speed);
        obstacleType = ObstacleType.RAIL;
        collisionInfo = new CollisionInfo(ConstValues.RAIL_WIDTH - 35, ConstValues.RAIL_HEIGHT);
    }

    @Override
    public void collide(Collidable object) {

    }
}
