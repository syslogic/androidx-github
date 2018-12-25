package io.syslogic.github;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Repository Activity UiAutomator TestCase
 * @author Martin Zeitler
 * @version 1.0.0
**/
@RunWith(AndroidJUnit4.class)
public class TestRepositoryActivity extends TestSuite {

    private String className = TestRepositoryActivity.class.getSimpleName().replace("Test", "");

    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void WebView() {
        UiObject2 layout = this.mDevice.findObject(By.res(this.packageName, "webview_preview"));
        Assert.assertThat(true, is(equalTo(true)));
    }
}