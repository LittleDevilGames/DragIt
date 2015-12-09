package com.dragit.slickstars.service;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by slick on 09.12.15.
 */
public class LevelServiceTest {

    @BeforeClass
    public static void initGdx() {
        Gdx.app = mock(Application.class);
    }

    @Test
    public void testOnInit() {
        LevelService ls = new LevelService(null);

        Assert.assertNotNull("LevelService is null", ls);
    }

    @Test
    public void testOnMaxBalls() {
        final int MAX_BALLS = 10;

        LevelService ls = new LevelService(null);
        ls.setMaxBalls(MAX_BALLS);

        Assert.assertTrue("Max balls must be " + MAX_BALLS, ls.getMaxBalls() == MAX_BALLS);
    }
}
