package com.snowbober.OOP.enitity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.interfaces.Renderable;

public abstract class EntityWithTexture extends Entity implements Renderable {
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

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(
                visual.getTexture(),
                this.getPosition().getX(),
                this.getPosition().getY(),
                visual.getImgWidth() / 2f,
                visual.getImgHeight() / 2f,
                visual.getImgWidth(),
                visual.getImgHeight(),
                1,
                1,
                visual.getRotation()
        );
    }
}
