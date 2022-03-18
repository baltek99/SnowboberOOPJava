package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;

public class Rail extends Obstacle {

    public Rail(Position position, int speed) {
        super(position, new Visual(new Texture("rail.png"), 300, 60), speed);
        obstacleType = ObstacleType.RAIL;
    }

    @Override
    protected void createRectangle() {
        this.rectangle = new Rectangle();
        rectangle.width = 260;
        rectangle.height = 60;
    }
}
