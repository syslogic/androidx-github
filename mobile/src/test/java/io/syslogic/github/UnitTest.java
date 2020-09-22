package io.syslogic.github;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import androidx.annotation.NonNull;
import io.syslogic.github.activity.BaseActivity;
import io.syslogic.github.activity.RepositoriesActivity;
import io.syslogic.github.constants.Constants;

/**
 * jUnit Test Case
 * @author Martin Zeitler
 * @version 1.0.0
**/
@RunWith(JUnit4.class)
public class UnitTest extends TestCase {

    private String className = UnitTest.class.getSimpleName().replace("Suite", "");

    @Before
    public void setup() {

    }

    @Test
    public void testConstants() {
        assertTrue(Constants.PARAMETER_PUSHED_WITHIN_LAST_DAYS > 0);
    }
}
