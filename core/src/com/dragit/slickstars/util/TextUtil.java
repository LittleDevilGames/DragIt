package com.dragit.slickstars.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by slick on 21.12.15.
 */
public class TextUtil {

    public static void drawText(BitmapFont font, float size, Color color, String text, float x, float y, SpriteBatch batch, boolean autoOffset) {
        font.getData().setScale(size);
        font.setColor(color);

        float offset = 0f;

        if(autoOffset) {
            offset = getHalfWidth(font, text) / 2;
        }
        font.draw(batch, text, x - offset, y);
    }

    public static void drawText(BitmapFont font, float size, Color color, String text, float x, float y, SpriteBatch batch) {
        drawText(font, size, color, text, x, y, batch, false);
    }

    // Vector position
    public static void drawText(BitmapFont font, float size, Color color, String text, Vector2 pos, SpriteBatch batch, boolean auotOffset) {
        drawText(font, size, color, text, pos.x, pos.y, batch, auotOffset);
    }

    public static void drawText(BitmapFont font, float size, Color color, String text, Vector2 pos, SpriteBatch batch) {
        drawText(font, size, color, text, pos.x, pos.y, batch);
    }

    public static float getHalfWidth(BitmapFont font, String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        return layout.width / 2;
    }
}
