package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;

public class Box extends Obstacle {
    public Box(Position position, int speed) {
        super(position, new Visual(new Texture("box.png"), 70, 70), speed);
        obstacleType = ObstacleType.BOX;
    }

    @Override
    protected void createRectangle() {
        this.rectangle = new Rectangle();
        rectangle.width = 70;
        rectangle.height = 70;
    }
}
