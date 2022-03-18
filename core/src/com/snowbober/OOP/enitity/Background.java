package com.snowbober.OOP.enitity;

import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;

public class Background extends MovableEntity {

    public Background(Position position, Visual visual, int speed) {
        super(position, visual, speed);
    }

    public void fixPosition() {
        int pos = this.getPosition().getX();
        int width = ConstValues.V_WIDTH;
        if (pos <= -width) {
            this.getPosition().setX(width + pos % width);
        }
    }
}
