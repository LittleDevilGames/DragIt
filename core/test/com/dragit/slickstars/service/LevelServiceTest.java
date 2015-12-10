package com.dragit.slickstars.service;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.service.LevelService;
import com.dragit.slickstars.util.Res;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.logging.Level;

import javax.xml.soap.Text;

import sun.applet.Main;

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

    @Test
    public void testOnBallOut() {
        MainGame game = new MainGame();
        LevelService ls = new LevelService(game);

        Ball ball = new Ball();
        ball.setX(100);
        ball.setY(-100);

        Assert.assertTrue(ls.isBallOut(ball));
    }

    @Test
    public void testOnPushingBalls() {
        final int BALL_COUNT = 50;
        final int MAX_BALLS = 30;

        MainGame game = new MainGame();
        game.res = new Res();
        LevelService ls = new LevelService(game);

        game.res.ballTexture = mock(Texture.class);
        game.ballGroup = mock(Group.class);

        ls.setMaxBalls(MAX_BALLS);
        for(int i = 0; i < BALL_COUNT; i++) {
            ls.pushBall(100, false);
        }

        Assert.assertTrue("Count of balls not equal: " + MAX_BALLS, ls.balls.size() == ls.getMaxBalls());
    }
}
