package com.snowbober.OOP;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Visual {
    private TextureRegion texture;
    private int imgWidth;
    private int imgHeight;
    private float rotation;

    public Visual(Texture texture, int imgWidth, int imgHeight) {
        this.texture = new TextureRegion(texture);
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.rotation = 0;
    }

    public Visual(TextureRegion texture, int imgWidth, int imgHeight, float rotation) {
        this.texture = texture;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        this.rotation = rotation;
    }

    public TextureRegion getTexture() {
        return texture;
    }

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
