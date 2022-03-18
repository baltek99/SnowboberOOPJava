package com.snowbober.OOP.enitity;

import com.snowbober.OOP.Position;

public abstract class Entity {
    protected Position position;

    public Entity(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
