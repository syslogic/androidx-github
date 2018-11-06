package io.syslogic.github;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

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

    @Before
    public void setPackageName() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void RecyclerView() {
        UiObject2 recyclerview = this.mDevice.findObject(By.res(this.packageName, "recyclerview_repositories"));
        int pages = 16;
        for(int page = 0; page < pages; page++) {
            flingUp(recyclerview, 4000, 200);
        }
        assertThat(recyclerview.getChildCount() > 0, is(equalTo(true)));
    }

    @Test
    public void SpinnerTopic() {

        UiObject2 view = this.mDevice.findObject(By.res(this.packageName, "spinner_topic"));
        view.click();
        sleep(2000);

        List<UiObject2> items = this.mDevice.findObjects(By.res("android:id/text1"));
        assertThat(items.size() > 0, is(equalTo(true)));

        items.get(1).click(500);
        sleep(2000);

        assertThat(true, is(equalTo(true)));
    }
}