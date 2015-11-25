package com.dragit.slickstars.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by slick on 24.11.15.
 */
public class Effect extends Actor {

    private ParticleEffect effect;

    public Effect(ParticleEffect effect) {
        this.effect = effect;
        this.effect.setPosition(-500, -500);
    }

    @Override
    public void draw(Batch batch, float delta) {
        effect.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        effect.setPosition(getX(), getY());
        effect.update(delta);
        effect.start();
    }

    public ParticleEffect getEffect() {
        return this.effect;
    }

    public void dispose() {
        this.effect.dispose();
    }
}
