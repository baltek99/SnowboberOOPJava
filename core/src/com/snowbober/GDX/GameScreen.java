package com.snowbober.GDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.Background;
import com.snowbober.OOP.enitity.CollisionInfo;
import com.snowbober.OOP.enitity.Obstacle;
import com.snowbober.OOP.enitity.Player;
import com.snowbober.OOP.enitity.obstacles.Box;
import com.snowbober.OOP.enitity.obstacles.Grid;
import com.snowbober.OOP.enitity.obstacles.Rail;
import com.snowbober.OOP.enitity.obstacles.ScorePoint;
import com.snowbober.OOP.enums.CollisionType;
import com.snowbober.OOP.interfaces.Movable;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class GameScreen implements Screen {

    public static final int V_WIDTH = ConstValues.V_WIDTH;
    public static final int V_HEIGHT = ConstValues.V_HEIGHT;

    private final Camera camera;
    private final Viewport viewport;
    private final SpriteBatch batch;

    private Background background1;
    private Background background2;
    private Player player;
    private Queue<Obstacle> obstacles;
    private List<ScorePoint> scorePoints;

    private long gameFrame;
    private int obstacleSpawnRate;

    public GameScreen() {
        this.batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT, camera);

        createWorld();
        gameFrame = 0;
        obstacleSpawnRate = 300;
        obstacles = new LinkedList<>();
        scorePoints = new LinkedList<>();
    }

    private void createWorld() {
        Texture backgroundTexture = new Texture("background.jpg");
        Texture playerTexture = new Texture("bober-stand.png");
        background1 = new Background(new Position(0, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), -2);
        background2 = new Background(new Position(V_WIDTH, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), -2);
        player = new Player(new Position(ConstValues.BOBER_DEFAULT_POSITION_X, ConstValues.BOBER_DEFAULT_POSITION_Y),
                new Visual(playerTexture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT));
    }

    //todo:
    // zeskok z raila, nieśmiertelność, stany gry, render wyniku
    @Override
    public void render(float delta) {

        detectInput(gameFrame);
        move(gameFrame);

        generateObstacle();

        if (!player.isImmortal()) {
            detectCollisions();
        } else {

        }

        draw();

        gameFrame++;
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background1.render(batch);
        background2.render(batch);

        for (Obstacle obstacle : obstacles) {
            obstacle.render(batch);
        }
//        for (ScorePoint scorePoint : scorePoints) {
//            drawEntity(scorePoint);
//        }

        player.render(batch);



        batch.end();
    }

    private void move(long gameFrame) {
        background1.fixPosition();
        background2.fixPosition();
        moveEntity(background1, gameFrame);
        moveEntity(background2, gameFrame);
        moveEntity(player, gameFrame);

        for (Obstacle obstacle : obstacles) {
            obstacle.move(gameFrame);
        }
        for (ScorePoint scorePoint : scorePoints) {
            scorePoint.move(gameFrame);
        }
    }

    private void moveEntity(Movable entity, long gameFrame) {
        entity.move(gameFrame);
    }

    private void detectCollisions() {
        for (Obstacle obstacle : obstacles) {
            CollisionType type = intersects(player, obstacle);
            if (type != CollisionType.NONE) {
                player.collide(obstacle);
            }
        }

        for (ScorePoint point : scorePoints) {
            CollisionType type = intersects(player, point);
            if (type != CollisionType.NONE) {
                player.collide(point);
                scorePoints.remove(point);
            }
        }
    }

    private CollisionType intersects(Player player, Obstacle obstacle) {

        CollisionInfo playerInfo = player.getCollisionInfo();
        CollisionInfo obstacleInfo = obstacle.getCollisionInfo();

        playerInfo.rectangle.x = player.getPosition().getX();
        playerInfo.rectangle.y = player.getPosition().getY();

        obstacleInfo.rectangle.x = obstacle.getPosition().getX();
        obstacleInfo.rectangle.y = obstacle.getPosition().getY();

        if (touch(playerInfo.rectangle, obstacleInfo.rectangle)) {
            return CollisionType.TOUCH;
        }
        if (playerInfo.rectangle.overlaps(obstacleInfo.rectangle)) {
            return CollisionType.INTERSECT;
        }

        return CollisionType.NONE;
    }

    private boolean touch(Rectangle s, Rectangle r) {
        boolean left = s.x == r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean right = s.x < r.x + r.width && s.x + s.width == r.x && s.y < r.y + r.height && s.y + s.height > r.y;
        boolean down = s.x < r.x + r.width && s.x + s.width > r.x && s.y == r.y + r.height && s.y + s.height > r.y;
        boolean up = s.x < r.x + r.width && s.x + s.width > r.x && s.y < r.y + r.height && s.y + s.height == r.y;

        return left || right || down || up;
    }

    private void generateObstacle() {
        if (gameFrame % obstacleSpawnRate == 0) {
            Random random = new Random();
            int x = random.nextInt();

            if (x % 3 == 0) {
                createBox();
                createScorePoint(270);
            } else if (x % 4 == 0) {
                createGrid();
                createScorePoint(500);
            } else {
                createRail();
                createScorePoint(500);
            }
        }
    }

    private void createGrid() {
        Grid grid = new Grid(new Position(V_WIDTH, 110), -3);
        obstacles.add(grid);
    }

    private void createRail() {
        Rail rail = new Rail(new Position(V_WIDTH, 110), -3);
        obstacles.add(rail);
    }

    private void createBox() {
        Box box = new Box(new Position(V_WIDTH, 100), -3);
        obstacles.add(box);
    }

    private void createScorePoint(int extra) {
        ScorePoint scorePoint = new ScorePoint(new Position(V_WIDTH + extra, 0), -3);
        scorePoints.add(scorePoint);
    }

    private void detectInput(long gameFrame) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump(gameFrame);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            player.crouch();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

}
