package com.snowbober.GDX;

import com.badlogic.gdx.Game;

public class SnowBoberGame extends Game {

    private GameScreen gameScreen;
    public static final int V_WIDTH = 964;
    public static final int V_HEIGHT = 540;

    @Override
    public void create() {
        gameScreen = new GameScreen();
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }
}
