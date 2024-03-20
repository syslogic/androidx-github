package io.syslogic.github;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObject2;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Repository Test Case
 *
 * @author Martin Zeitler
 */
@Deprecated
@RunWith(AndroidJUnit4.class)
public class TestRepository extends TestSuite {

    private final String className = TestRepository.class.getSimpleName().concat("Activity").replace("Test", "");

    @Before
    public void startActivityFromHomeScreen() {
        this.mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        this.startTestActivity(this.mContext, this.className);
    }

    @Test
    public void ButtonDownload() {

        UiObject2 view = this.mDevice.findObject(By.res(this.packageName, "button_download"));
        assertThat(view.isClickable(), is(equalTo(true)));
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
