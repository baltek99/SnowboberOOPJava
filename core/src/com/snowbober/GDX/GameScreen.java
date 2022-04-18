package com.snowbober.GDX;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.gson.Gson;
import com.snowbober.OOP.ConstValues;
import com.snowbober.OOP.HighScores;
import com.snowbober.OOP.Position;
import com.snowbober.OOP.Visual;
import com.snowbober.OOP.enitity.*;
import com.snowbober.OOP.enitity.obstacles.*;
import com.snowbober.OOP.enums.CollisionType;
import com.snowbober.OOP.enums.GameState;
import com.snowbober.OOP.enums.ObstacleType;
import com.snowbober.OOP.enums.PlayerState;
import com.snowbober.OOP.interfaces.Movable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GameScreen implements Screen, Input.TextInputListener {

    public static final int V_WIDTH = ConstValues.V_WIDTH;
    public static final int V_HEIGHT = ConstValues.V_HEIGHT;

    private final Camera camera;
    private final Viewport viewport;
    private SpriteBatch batch;
    private final BitmapFont font;

    private GameState gameState;

    private Player player;
    private Queue<Obstacle> obstacles;
    private List<ScorePoint> scorePoints;
    private List<Background> backgrounds;

    private HighScores highScores;
    private String highScoresPath = "highscores.json";
    private Gson gson;

    private long gameFrame;
    private int obstacleFrame;
    private int obstacleSpawnRate;
    private int currentObstacleSpeed;
    private String playerName;
    private int result;

    public GameScreen() {
        this.batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT, camera);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cour.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 30;
        font = generator.generateFont(parameter);
        generator.dispose();
        font.setColor(Color.BLACK);

        gameFrame = 0;
        obstacleFrame = 0;
        obstacleSpawnRate = 300;
        currentObstacleSpeed = -3;

        obstacles = new LinkedList<>();
        scorePoints = new LinkedList<>();
        backgrounds = new LinkedList<>();

        highScores = new HighScores();
        gson = new Gson();
        try {
            FileReader fileReader = new FileReader(highScoresPath);
            highScores = gson.fromJson(fileReader, HighScores.class);

        } catch (FileNotFoundException e) {
            System.out.println("HighScores file not found!");
            e.printStackTrace();
        }

        gameState = GameState.MAIN_MENU;
        createMainMenuWorld();
    }

    private void createMainMenuWorld() {
        resetWorld();
        Texture backgroundTexture = new Texture("start.jpg");
        Background background = new Background(new Position(0, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), 0);
        backgrounds.add(background);
        playerName = null;
    }

    private void createGameWorld(String playerName) {
        resetWorld();
        Texture backgroundTexture = new Texture("background.jpg");
        Texture playerTexture = new Texture("bober-stand.png");
        Background background1 = new Background(new Position(0, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), -2);
        Background background2 = new Background(new Position(V_WIDTH, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), -2);
        backgrounds.add(background1);
        backgrounds.add(background2);
        player = new Player(new Position(ConstValues.BOBER_DEFAULT_POSITION_X, ConstValues.BOBER_DEFAULT_POSITION_Y),
                new Visual(playerTexture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT), playerName);
    }

    private void createGameOverWorld(int score) {
        resetWorld();
        result = score;
        highScores.addResult(playerName, result);

        try {
            FileWriter writer = new FileWriter(highScoresPath);
            gson.toJson(highScores, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("HighScores object serialization failed!");
            e.printStackTrace();
        }

        Texture backgroundTexture = new Texture("game-over.jpg");
        Background background = new Background(new Position(0, 0), new Visual(backgroundTexture, SnowBoberGame.V_WIDTH, SnowBoberGame.V_HEIGHT), 0);
        backgrounds.add(background);
    }

    private void createHighScoreWorld() {
        resetWorld();
    }

    private void resetWorld() {
        gameFrame = 0;
        obstacleFrame = 0;
        obstacleSpawnRate = 300;
        currentObstacleSpeed = -3;
        player = null;
        disposeTextures(obstacles);
        obstacles = new LinkedList<>();
        disposeTextures(scorePoints);
        scorePoints = new LinkedList<>();
        disposeTextures(backgrounds);
        backgrounds = new LinkedList<>();
        batch.dispose();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
    }

    private void disposeTextures(Collection collection) {
        for (Object entity : collection) {
            ((EntityWithTexture) entity).getVisual().getTexture().getTexture().dispose();
        }
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.H) && (gameState == GameState.MAIN_MENU || gameState == GameState.GAME_OVER)) {
            gameState = GameState.HIGH_SCORES;
            drawHighScores();
        } else if (gameState == GameState.HIGH_SCORES) {
            drawHighScores();
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                gameState = GameState.MAIN_MENU;
                createMainMenuWorld();
            }
        } else if (gameState == GameState.MAIN_MENU) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
                Gdx.input.getTextInput(this, "Podaj swój nick", "", "Player");
            }
            if (playerName != null) {
                gameState = GameState.GAMEPLAY;
                createGameWorld(playerName);
            }
            drawStart();
        } else if (gameState == GameState.GAMEPLAY) {
            detectInput(gameFrame);
            move(gameFrame);

            generateObstacle();

            detectCollisions();

            clearObstacles();

            if (player.getLives().size() == 0) {
                gameState = GameState.GAME_OVER;
                createGameOverWorld(player.getScore());
            }
            drawGame();
        } else if (gameState == GameState.GAME_OVER) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
//                playerName = null;
                gameState = GameState.GAMEPLAY;
//                createMainMenuWorld();
                createGameWorld(playerName);
            }
            drawEnd();
        }


        gameFrame++;
    }

    private void drawHighScores() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.setColor(Color.WHITE);

        int size = highScores.getScores().size();
        if (size != 0) {
            int inc = V_HEIGHT / size;

            for (int i = size - 1; i >= 0; i--) {
                font.draw(batch, size - i + ". " + highScores.getScores().get(i),
                        300, (i + 1) * inc - 25);
            }
        }

        batch.end();
    }

    private void drawStart() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Background background : backgrounds) {
            background.render(batch);
        }
        batch.end();
    }

    private void drawEnd() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        for (Background background : backgrounds) {
            background.render(batch);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "Player: " + playerName + "    Score: " + result, 200, 50);

        batch.end();
    }

    private void drawGame() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        font.setColor(Color.BLACK);

        batch.begin();

        for (Background background : backgrounds) {
            background.render(batch);
        }

        for (Obstacle obstacle : obstacles) {
            if (obstacle.getZIndex() == 0) {
                obstacle.render(batch);
            }
        }

        if (player != null) {
            player.render(batch);
            font.draw(batch, "Score: " + player.getScore(), ConstValues.SCORE_POSITION_X, ConstValues.SCORE_POSITION_Y);
            for (Life life : player.getLives()) {
                life.render(batch);
            }
        }

        for (Obstacle obstacle : obstacles) {
            if (obstacle.getZIndex() == 1) {
                obstacle.render(batch);
            }
        }

        batch.end();
    }

    private void move(long gameFrame) {
        for (Background background : backgrounds) {
            background.fixPosition();
            moveEntity(background, gameFrame);
        }

        if (player != null) {
            moveEntity(player, gameFrame);
        }

        for (Obstacle obstacle : obstacles) {
            moveEntity(obstacle, gameFrame);
        }

        for (ScorePoint scorePoint : scorePoints) {
            moveEntity(scorePoint, gameFrame);
        }

        if (gameFrame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {

            currentObstacleSpeed--;
        }
    }

    private void moveEntity(Movable entity, long gameFrame) {
        entity.move(gameFrame);
        entity.speedUp(gameFrame);
    }

    private void detectCollisions() {
        if (!player.isImmortal()) {
            for (Obstacle obstacle : obstacles) {
                CollisionType type = intersects(player, obstacle);
                if (type != CollisionType.NONE) {
                    boolean collisionFlag = true;
                    if (obstacle.getObstacleType() == ObstacleType.RAIL) {
                        Rail rail = (Rail) obstacle;
                        collisionFlag = getOffRail(rail);
                    }

                    if (collisionFlag) player.collide(obstacle);
                }
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

    private boolean getOffRail(Rail rail) {
        int obstacleX = rail.getPosition().getX();
        int playerX = player.getPosition().getX();
        if (obstacleX < playerX && Math.abs(playerX - obstacleX) >= ConstValues.RAIL_AND_BOBER_DIFFERENCE) {
            if (player.getPlayerState() == PlayerState.SLIDING) {
                player.setPlayerState(PlayerState.IDLE);
                player.getPosition().setY(ConstValues.IDLE_RIDE_Y);
                Texture texture = new Texture("bober-stand.png");
                player.setVisual(new Visual(texture, ConstValues.BOBER_DEFAULT_WIDTH, ConstValues.BOBER_DEFAULT_HEIGHT));
            }
            player.getCollisionInfo().collisionType = CollisionType.NONE;
            return false;
        }
        return true;
    }

    private void generateObstacle() {
        obstacleFrame++;
        if (obstacleFrame % ConstValues.NUMBER_OF_FRAMES_TO_INCREMENT == 0) {
            obstacleSpawnRate = obstacleSpawnRate - obstacleSpawnRate / -currentObstacleSpeed;
            obstacleFrame = 1;
//            java.lang.System.out.println("Spawn rate " + obstacleSpawnRate + " speed " + -currentObstacleSpeed);
        }

        if (obstacleFrame % obstacleSpawnRate == 0) {
            Random random = new Random();
            int x = random.nextInt(1000);

            if (x < 333) {
                createBox();
                createScorePoint(270);
            } else if (x < 666) {
                createGrid();
                createScorePoint(500);
            } else {
                createRail();
                createScorePoint(500);
            }
        }
    }

    private void createGrid() {
        Grid grid = new Grid(new Position(V_WIDTH, 60), currentObstacleSpeed);
//        grids.add(grid);
        GridStick gridStick = new GridStick(new Position(V_WIDTH, 60), currentObstacleSpeed);
        obstacles.add(grid);
        obstacles.add(gridStick);
    }

    private void createRail() {
        Rail rail = new Rail(new Position(V_WIDTH, 110), currentObstacleSpeed);
        obstacles.add(rail);
    }

    private void createBox() {
        Box box = new Box(new Position(V_WIDTH, 100), currentObstacleSpeed);
        obstacles.add(box);
    }

    private void createScorePoint(int extra) {
        ScorePoint scorePoint = new ScorePoint(new Position(V_WIDTH + extra, 0), currentObstacleSpeed);
        scorePoints.add(scorePoint);
    }

    private void detectInput(long gameFrame) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.jump(gameFrame);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
            player.crouch();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
    }

    private void clearObstacles() {
        if (obstacles.peek() != null && obstacles.peek().getPosition().getX() < -500) {
            Obstacle poll = obstacles.poll();
            poll.getVisual().getTexture().getTexture().dispose();
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

    @Override
    public void input(String text) {
        playerName = text;
    }

    @Override
    public void canceled() {

    }
}
