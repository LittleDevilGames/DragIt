package com.dragit.slickstars.service;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.dragit.slickstars.game.MainGame;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by slick on 09.12.15.
 */
public class GameServiceTest {

    private static MainGame game;

    @BeforeClass
    public static void initGdx() {
        Gdx.app = mock(Application.class);
        game = mock(MainGame.class);
    }

    @Test
    public void testOnInit() {
        GameService gs = new GameService(game);

        Assert.assertNotNull("GameService is null", gs);
    }

    @Test
    public void testOnPause() {
        GameService gs = new GameService(game);
        gs.pause(true);

        Assert.assertTrue("Pause must be true", gs.countdown.isPause() == true);
    }
}
