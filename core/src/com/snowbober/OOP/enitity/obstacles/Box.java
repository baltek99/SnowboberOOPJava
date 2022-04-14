package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.interfaces.Collidable;

public class Box extends Obstacle {

    public Box(Position position, int speed) {
        super(position, new Visual(new Texture("box.png"), ConstValues.BOX_WIDTH, ConstValues.BOX_HEIGHT), speed, ObstacleType.BOX);
    }

    @Override
    public void collide(Collidable object) {

    }
}
