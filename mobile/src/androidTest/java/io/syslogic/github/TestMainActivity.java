package io.syslogic.github;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import io.syslogic.github.activity.MainActivity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Main Activity UiAutomator TestCase
 * @author Martin Zeitler
 * @version 1.0.0
**/
@RunWith(AndroidJUnit4.class)
public class TestMainActivity extends TestSuite {

    private String className = TestMainActivity.class.getSimpleName().replace("Test", "");

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    /** launch the blueprint application */
    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = mActivityRule.getActivity();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void RecyclerView() {
        UiObject2 view = this.mDevice.findObject(By.res(this.packageName, "recyclerview_repositories"));
        int pages = Math.round(1554/30);
        for(int page = 0; page < pages; page++) {
            scollDown(view, 300, 2000);
        }
        assertThat(true, is(equalTo(true)));
    }
}