package com.snowbober.OOP.enitity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.enums.PlayerState;
import com.snowbober.OOP.interfaces.Collidable;
import com.snowbober.OOP.interfaces.Movable;
import com.snowbober.OOP.interfaces.PlayerActions;
import com.snowbober.Util.Util;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Player extends EntityWithTexture implements PlayerActions, Movable, Collidable {

    private int score;
    private Queue<Life> lives;
    private boolean immortal = false;
    private PlayerState playerState;
    private CollisionInfo collisionInfo;
    private int jumpFrom;
    private long startJumpFrame;
    private int jumpHeight = 120;
    private float jumpDuration = 110;
    private float flipRotationSpeed = 3.4f;
    private float ollieUpRotationSpeed = 1.3f;
    private float ollieDownRotationSpeed = 0.5f;
    private final int initialImmortalDurationVal = 150;
    private int immortalDuration = 150;

    public CollisionInfo getCollisionInfo() {
        return collisionInfo;
    }

    public Player(Position position, Visual visual) {
        super(position, visual);
        this.zIndex = 0;
        this.score = 0;
        this.lives = new LinkedList<>();
        lives.add(new Life(new Position(ConstValues.HEART_POSITION_X_1, ConstValues.HEART_POSITION_Y)));
        lives.add(new Life(new Position(ConstValues.HEART_POSITION_X_2, ConstValues.HEART_POSITION_Y)));
        lives.add(new Life(new Position(ConstValues.HEART_POSITION_X_3, ConstValues.HEART_POSITION_Y)));
        this.playerState = PlayerState.IDLE;
        collisionInfo = new CollisionInfo(visual.getImgWidth(), visual.getImgHeight());
    }
    
    public boolean isImmortal() {
        return immortal;
    }

    public void setImmortal(boolean immortal) {
        this.immortal = immortal;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public Queue<Life> getLives() {
        return lives;
    }
    
    public int getNumberOfLives() {
        return lives.size();
    }

    public int getScore() {
        return score;
    }

    @Override
    public void jump(long gameFrame) {
        Texture texture;
        if (playerState == PlayerState.SLIDING) {
            playerState = PlayerState.JUMPING_ON_RAIL;
            jumpFrom = ConstValues.JUMP_FROM_RAIL_Y;
            startJumpFrame = gameFrame;
            texture = new Texture("bober-flip.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT));
        } else if (playerState != PlayerState.JUMPING && playerState != PlayerState.JUMPING_ON_RAIL
                && playerState != PlayerState.JUMPING_FROM_CROUCH) {
            if (playerState == PlayerState.CROUCH) {
                texture = new Texture("bober-flip.png");
                playerState = PlayerState.JUMPING_FROM_CROUCH;
            } else {
                texture = new Texture("bober-jump.png");
                playerState = PlayerState.JUMPING;
            }
            jumpFrom = ConstValues.JUMP_FROM_GROUND_Y;
            startJumpFrame = gameFrame;
            this.setVisual(new Visual(texture, ConstValues.BOBER_IN_JUMP_WIDTH, ConstValues.BOBER_IN_JUMP_HEIGHT));
        }
    }

    @Override
    public void crouch() {
        if (playerState == PlayerState.IDLE) {
            playerState = PlayerState.CROUCH;

            position.setY(100);
            Texture texture = new Texture("bober-luzny.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_CROUCH_WIDTH, ConstValues.BOBER_CROUCH_HEIGHT));
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
                position.setY((int) Util.lerp(
                        jumpFrom,
                        jumpFrom + jumpHeight,
                        Util.spike((gameFrame - startJumpFrame) / jumpDuration)
                ));

                if (playerState == PlayerState.JUMPING_ON_RAIL) {
                    this.getVisual().setRotation(this.getVisual().getRotation() + flipRotationSpeed);
                } else if (playerState == PlayerState.JUMPING_FROM_CROUCH) {
                    this.getVisual().setRotation(this.getVisual().getRotation() - flipRotationSpeed);
                } else {
                    if ((gameFrame - startJumpFrame) / jumpDuration < 0.15f) {
                        this.getVisual().setRotation(this.getVisual().getRotation() + ollieUpRotationSpeed);
                    } else if (this.getVisual().getRotation() > -10) {
                        this.getVisual().setRotation(this.getVisual().getRotation() - ollieDownRotationSpeed);
                    }
                }
            }
        }
    }

    @Override
    public void collide(Collidable collidable) {
        Obstacle obstacle = (Obstacle) collidable;
        if (obstacle.obstacleType == ObstacleType.SCORE_POINT) {
            System.out.println("Punkt");
            score++;
        } else if (obstacle.obstacleType == ObstacleType.BOX || (obstacle.obstacleType == ObstacleType.RAIL && playerState == PlayerState.IDLE)) {
            lives.poll();
            immortal = true;
        } else if (obstacle.obstacleType == ObstacleType.RAIL && (playerState == PlayerState.JUMPING || playerState == PlayerState.JUMPING_FROM_CROUCH)) {
            this.getPosition().setY(ConstValues.SLIDING_ON_RAIL_Y);
            playerState = PlayerState.SLIDING;
            Texture texture = new Texture("bober-rail.png");
            this.setVisual(new Visual(texture, ConstValues.BOBER_ON_RAIL_WIDTH, ConstValues.BOBER_ON_RAIL_HEIGHT));
            System.out.println("rail");
        } else if (obstacle.obstacleType == ObstacleType.GRID && playerState != PlayerState.CROUCH) {
            lives.poll();
            immortal = true;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        if (immortal && immortalDuration > 0) {
            if (immortalDuration % 40 < 20) {
                super.render(batch);
            }
            immortalDuration--;
        } else if (immortal) {
            immortal = false;
            immortalDuration = initialImmortalDurationVal;
        } else {
            super.render(batch);
        }
    }
}
