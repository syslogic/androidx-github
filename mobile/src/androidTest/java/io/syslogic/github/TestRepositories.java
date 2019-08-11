package io.syslogic.github;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.By;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Repositories Test Case
 * @author Martin Zeitler
 * @version 1.0.2
**/
@RunWith(AndroidJUnit4.class)
public class TestRepositories extends TestSuite {

    private String className = TestRepositories.class.getSimpleName().concat("Activity").replace("Test", "");

    @Before
    public void setPackageName() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void RecyclerView() {
        UiObject2 recyclerview = this.mDevice.findObject(By.res(this.packageName, "recyclerview_repositories"));
        int pages = 6;
        for(int page = 0; page < pages; page++) {
            flingUp(recyclerview, 4000, 200);
        }
        Assert.assertThat(recyclerview.getChildCount() > 0, is(equalTo(true)));
    }

    @Test
    public void SpinnerTopic() {
        clickSpinnerItem("spinner_topic", 1);
        clickSpinnerItem("spinner_topic", 2);
        clickSpinnerItem("spinner_topic", 0);
    }
}
