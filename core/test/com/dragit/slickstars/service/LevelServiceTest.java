package com.dragit.slickstars.service;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.dragit.slickstars.entity.Ball;
import com.dragit.slickstars.game.MainGame;
import com.dragit.slickstars.util.Res;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

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

    @Test
    public void testOnCreationSides() {
        MainGame game = new MainGame();
        LevelService ls = new LevelService(game);

        ls.createSides();

        Assert.assertTrue("Count of side must be " + game.MAX_SIDES, ls.sides.size() == game.MAX_SIDES);
    }

    @Test
    public void testOnCheckDragDirection() {
        MainGame game = new MainGame();
        LevelService ls = new LevelService(game);

        Ball ball = new Ball();
        ball.isDragged = false;
        ball.setX(100);
        ball.setY(100);
        ball.setWidth(game.BALL_SIZE);
        ball.setHeight(game.BALL_SIZE);

        float testDragPosX = 120f;
        float testDrahPosY = 120f;

        Vector3 pos = new Vector3(testDragPosX, testDrahPosY, 0);
        boolean isDragged = ls.checkDrag(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight(), pos);

        Assert.assertTrue("Ball not dragged with this coords: (ball)" + ball.getX() + ":" + ball.getY() + " (drag)" + testDragPosX + ":" + testDrahPosY, isDragged);
    }
}
