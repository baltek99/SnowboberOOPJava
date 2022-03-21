package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;

public class ScorePoint extends Obstacle {

    public ScorePoint(Position position, int speed) {
        super(position, new Visual(new Texture("rail.png"), 1, ConstValues.V_HEIGHT), speed);
        obstacleType = ObstacleType.SCORE_POINT;
        collisionInfo = new CollisionInfo(1, ConstValues.V_HEIGHT);
    }

    @Override
    public void collide(Collidable object) {

    }
}
