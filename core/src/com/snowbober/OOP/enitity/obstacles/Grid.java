package com.snowbober.OOP.enitity.obstacles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enitity.Player;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.enums.PlayerState;
import com.snowbober.OOP.interfaces.Collidable;

public class Grid extends Obstacle {
    public Grid(Position position, int speed) {
        super(position, new Visual(new Texture("grid.png"), ConstValues.GRID_WIDTH, ConstValues.GRID_HEIGHT), speed, ObstacleType.GRID);
        this.zIndex = 1;
        collisionInfo = new CollisionInfo(168, 350);
    }

    @Override
    public void collide(Collidable object) {
        Player player = (Player) object;
        if (player.getPlayerState() != PlayerState.CROUCH) {
            Visual visual = getVisual();
            visual.setTexture(new TextureRegion(new Texture("grid-broken.png")));
        }
    }
}
