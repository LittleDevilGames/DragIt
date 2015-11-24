package com.dragit.slickstars.util;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class Particle {
	public static final String PARTICLE_PATH = "data/particle";
	
	public static ParticleEffect fireParticle;

	public static void load() {
		try {
			fireParticle = loadEffect(PARTICLE_PATH + File.separator + "pixel.p");
			
			Logger.log("Particle", "particles loaded");
		}
		catch(GdxRuntimeException e) {
			System.err.println("Error loading particle file - " + e.getMessage());
		}
		catch(Exception e) {
			System.err.println("Error loading particle file - " + e);
		}
	}
	
	public static ParticleEffect loadEffect(String path) {
		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal(path), Gdx.files.internal(PARTICLE_PATH));
		effect.setPosition(-500, -500);
		effect.start();
		return effect;
	}
	
	public static void dispose() {
		fireParticle.dispose();
	}
}
