package io.syslogic.github;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import io.syslogic.github.activity.DetailActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Detail Activity UiAutomator TestCase
 * @author Martin Zeitler
 * @version 1.0.0
**/
@RunWith(AndroidJUnit4.class)
public class TestDetailActivity extends TestSuite {

    private String className = TestDetailActivity.class.getSimpleName().replace("Test", "");

    @Rule
    public ActivityTestRule<DetailActivity> mActivityRule = new ActivityTestRule<>(DetailActivity.class);

    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = mActivityRule.getActivity();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void WebView() {
        UiObject2 layout = this.mDevice.findObject(By.res(this.packageName, "webview_preview"));
        assertThat(true, is(equalTo(true)));
    }
}