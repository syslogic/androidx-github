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
 * Repository Test Case
 * @author Martin Zeitler
 */
@RunWith(AndroidJUnit4.class)
public class TestRepository extends TestSuite {

    private String className = TestRepository.class.getSimpleName().concat("Activity").replace("Test", "");

    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void ButtonDownload() {

        UiObject2 view = this.mDevice.findObject(By.res(this.packageName, "button_download"));
        Assert.assertThat(view.isClickable(), is(equalTo(true)));
        view.click();

        sleep(1000);
        grantPermission();

        sleep(30000);
    }

    @Test
    public void SpinnerBranch() {
        clickSpinnerItem("spinner_branch", 1);
        clickSpinnerItem("spinner_branch", 2);
        clickSpinnerItem("spinner_branch", 0);
    }
}
