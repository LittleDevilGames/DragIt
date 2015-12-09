package com.dragit.slickstars.game;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Timer;

import javafx.application.Application;

/**
 * Created by slick on 09.12.15.
 */
public class CountdownTest {

    @Test
    public void testOnInit() {
        Countdown cd = new Countdown(20, 5);

        Assert.assertNotNull("Countdown is null", cd);
    }

    @Test
    public void testOnTime() {
        final int TIME = 20;
        final int TIME_PART = 5;

        Countdown cd = new Countdown(TIME, TIME_PART);

        Assert.assertTrue("Time must be " + TIME, cd.getTime() == TIME);
        Assert.assertTrue("Time part must be " + TIME_PART, cd.getPartOfTime() == TIME_PART);
    }
}
