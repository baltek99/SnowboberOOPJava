package com.snowbober.OOP.enitity;

import com.badlogic.gdx.math.Rectangle;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;

public abstract class EntityWithTexture extends Entity {
    private Visual visual;

    public EntityWithTexture(Position position, Visual visual) {
        super(position);
        this.visual = visual;
    }

    public Visual getVisual() {
        return visual;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }
}
