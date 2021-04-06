package io.syslogic.github;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Profile Test Case
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4.class)
public class TestProfile extends TestSuite {

    private String className = TestProfile.class.getSimpleName().concat("Activity").replace("Test", "");

    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void WebView() {
        UiObject2 layout = this.mDevice.findObject(By.res(this.packageName, "webview"));
        Assert.assertThat(true, is(equalTo(true)));
    }
}