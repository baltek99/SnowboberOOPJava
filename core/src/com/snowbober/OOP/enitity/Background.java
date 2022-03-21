package com.snowbober.OOP.enitity;

import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.interfaces.Movable;

public class Background extends EntityWithTexture implements Movable {

    protected int speed;

    public Background(Position position, Visual visual, int speed) {
        super(position, visual);
        this.speed = speed;
    }

    public void fixPosition() {
        int pos = this.getPosition().getX();
        int width = ConstValues.V_WIDTH;
        if (pos <= -width) {
            this.getPosition().setX(width + pos % width);
        }
    }

    @Override
    public void move(long gameFrame) {
        this.position.setX(this.position.getX() + speed);
    }
}
