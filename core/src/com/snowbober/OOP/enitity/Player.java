package com.snowbober.OOP.enitity;

import com.badlogic.gdx.graphics.Texture;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.enums.CollisionType;
import com.snowbober.OOP.interfaces.PlayerActions;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enums.PlayerState;
import com.snowbober.Util.Util;

public class Player extends MovableEntity implements PlayerActions {

    private int score;
    private int lives;
    private PlayerState playerState;
    private CollisionType collisionType;
    private int jumpFrom;
    private long startJumpFrame;
    private int jumpHeight = 120;
    private float jumpDuration = 110;
    private float rotationSpeed = 3.4f;

    public Player(Position position, Visual visual) {
        super(position, visual);
        this.score = 0;
        this.lives = 3;
        this.playerState = PlayerState.IDLE;
        this.collisionType = CollisionType.NONE;
    }

    public Player(Position position, Visual visual, int score, int lives, PlayerState playerState) {
        super(position, visual);
        this.score = score;
        this.lives = lives;
        this.playerState = playerState;
        this.collisionType = CollisionType.NONE;
    }

    @Override
    public void jump(long gameFrame) {
        if (playerState == PlayerState.SLIDING) {
            playerState = PlayerState.JUMPING_ON_RAIL;
            jumpFrom = ConstValues.JUMP_FROM_RAIL_Y;
            startJumpFrame = gameFrame;
            Texture texture = new Texture("bober-jump.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT));
        } else if (playerState != PlayerState.JUMPING && playerState != PlayerState.JUMPING_ON_RAIL
                && playerState != PlayerState.JUMPING_FROM_CROUCH) {
            if (playerState == PlayerState.CROUCH) {
                playerState = PlayerState.JUMPING_FROM_CROUCH;
            } else {
                playerState = PlayerState.JUMPING;
            }
            jumpFrom = ConstValues.JUMP_FROM_GROUND_Y;
            startJumpFrame = gameFrame;
            Texture texture = new Texture("bober-jump.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT));
        }
    }

    @Override
    public void crouch() {
        if (playerState == PlayerState.IDLE) {
            playerState = PlayerState.CROUCH;

            position.setY(100);
            Texture texture = new Texture("bober-jump.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT));
        } else if (playerState == PlayerState.CROUCH) {
            playerState = PlayerState.IDLE;

            position.setY(ConstValues.IDLE_RIDE_Y);
            Texture texture = new Texture("bober-stand.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT));
        }
    }

    @Override
    public void move(long gameFrame) {
        if (playerState == PlayerState.JUMPING || playerState == PlayerState.JUMPING_ON_RAIL
                || playerState == PlayerState.JUMPING_FROM_CROUCH) {
            if (gameFrame == startJumpFrame + jumpDuration) {
                playerState = PlayerState.IDLE;
                position.setY(ConstValues.IDLE_RIDE_Y);
                this.getVisual().setRotation(0);
                Texture texture = new Texture("bober-stand.png");
                this.setVisual(new Visual(texture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT));
            } else {
                position.setY( (int) Util.lerp(
                        jumpFrom,
                        jumpFrom + jumpHeight,
                        Util.spike((gameFrame - startJumpFrame) / jumpDuration)
                ));

                if (playerState == PlayerState.JUMPING_ON_RAIL) {
                    this.getVisual().setRotation(this.getVisual().getRotation() + rotationSpeed);
                } else if (playerState == PlayerState.JUMPING_FROM_CROUCH) {
                    this.getVisual().setRotation(this.getVisual().getRotation() - rotationSpeed);
                } else {
                    if ((gameFrame - startJumpFrame) / jumpDuration < 0.15f) {
                        this.getVisual().setRotation(this.getVisual().getRotation() + 1.3f);
                    } else if (this.getVisual().getRotation() > -10) {
                        this.getVisual().setRotation(this.getVisual().getRotation() - 0.5f);
                    }
                }
            }
        }
    }
}
