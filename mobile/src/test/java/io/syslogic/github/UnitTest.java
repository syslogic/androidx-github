package io.syslogic.github;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * jUnit Test Case
 * @author Martin Zeitler
 */
@RunWith(JUnit4.class)
public class UnitTest extends TestCase {

    private final String className = UnitTest.class.getSimpleName().replace("Suite", "");

    @Before
    public void setup() {

    }

    @Test
    public void testConstants() {
        assertTrue(Constants.PARAMETER_PUSHED_WITHIN_LAST_DAYS > 0);
    }
}
