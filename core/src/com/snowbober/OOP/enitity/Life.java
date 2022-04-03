package com.snowbober.OOP.enitity;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;

public class Life extends EntityWithTexture {
    public Life(Position position) {
        super(position, new Visual(new Texture("heart.png"), ConstValues.HEART_WIDTH, ConstValues.HEART_HEIGHT));
    }
}
