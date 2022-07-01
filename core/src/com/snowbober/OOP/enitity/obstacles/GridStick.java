package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enitity.Player;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.enums.PlayerState;
import com.snowbober.OOP.interfaces.Collidable;

public class GridStick extends Obstacle {
    public GridStick(Position position, int speed) {
        super(position, new Visual(new Texture("grid-stick.png"), ConstValues.GRID_WIDTH, ConstValues.GRID_HEIGHT), speed, ObstacleType.GRID_STICK);
        collisionInfo = new CollisionInfo(168, 350);
    }

    @Override
    public void collide(Collidable object) {
        Player player = (Player) object;
        if (player.getPlayerState() != PlayerState.CROUCH) {
            setVisual(new Visual(new Texture("grid-stick.png"), 0, 0));
        }
    }
}
