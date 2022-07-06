package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;

import java.util.Random;

public class Rail extends Obstacle {
    public Rail(Position position, int speed) {
        super(position, new Visual(new Texture(new Random().nextInt(1000) < 500 ? "rail.png" : "grubas.png"),
                ConstValues.RAIL_WIDTH, ConstValues.RAIL_HEIGHT), speed, ObstacleType.RAIL);
        collisionInfo = new CollisionInfo(ConstValues.RAIL_WIDTH - 35, ConstValues.RAIL_HEIGHT);
    }

    public void setRailCollisionHeight(int height) {
        collisionInfo = new CollisionInfo(ConstValues.RAIL_WIDTH, height);
    }

    @Override
    public void collide(Collidable object) {

    }
}
