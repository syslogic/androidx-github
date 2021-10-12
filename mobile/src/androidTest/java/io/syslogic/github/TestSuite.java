package io.syslogic.github;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.StaleObjectException;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Assert;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

import java.util.List;

/**
 * Application Test Suite
 * @author Martin Zeitler
 */
@RunWith(org.junit.runners.Suite.class)
@SuiteClasses({
    TestRepositories.class,
    TestRepository.class,
    TestProfile.class
})
public class TestSuite {

    private static final int LAUNCH_TIMEOUT = 5000;

    String packageName;
    Context mContext;
    UiDevice mDevice;

    /**
     * uses package manager to find the package name of the device launcher.
     * usually this package is "com.android.launcher" but can be different at times.
     * this is a generic solution which works on all platforms.`
    **/
    private String getLauncherPackageName() {

        /* create a launcher Intent */
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        /* use PackageManager to get the launcher package name */
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        if (resolveInfo != null) {
            return resolveInfo.activityInfo.name;
        } else {
            return null;
        }
    }

    /** launches the blueprint application */
    void startTestActivity(Context context, String className){

        /* initialize UiDevice */
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Assert.assertThat(this.mDevice, notNullValue());

        /* start from the home screen */
        this.mDevice.pressHome();

        /* obtain the launcher package */
        String launcherPackage = getLauncherPackageName();
        Assert.assertThat(launcherPackage, notNullValue());

        /* wait for launcher */
        this.mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        /* setting the package name and obtaining the launch intent */
        this.packageName = context.getPackageName().replace(".test", "");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(this.packageName);

        if(intent != null) {

            intent.setComponent(new ComponentName(this.packageName, this.packageName.replace("debug", "activity." + className)));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle extras = new Bundle();
            if(className.equals("RepositoryActivity")) {
                extras.putLong(Constants.ARGUMENT_ITEM_ID, 51148780);
                intent.putExtras(extras);
            }
            context.startActivity(intent);

            /* wait for the app to appear */
            this.mDevice.wait(Until.hasObject(By.pkg(this.packageName.replace(".debug", "")).depth(0)), LAUNCH_TIMEOUT);
        }
    }

    protected UiObject2 getItemById(String resourceId) {
        return this.mDevice.findObject(By.res(this.packageName, resourceId));
    }

    /** it clicks spinner items by index */
    public void clickSpinnerItem(String spinnerName, int itemIndex) {

        UiObject2 spinner = this.mDevice.findObject(By.res(this.packageName, spinnerName));
        Assert.assertThat(spinner.isClickable(), is(equalTo(true)));
        spinner.click();
        sleep(2000);

        List<UiObject2> items = this.mDevice.findObjects(By.res("android:id/text1"));
        Assert.assertThat(items.size() > itemIndex, is(equalTo(true)));

        UiObject2 item = items.get(itemIndex);
        Assert.assertThat(item.isClickable(), is(equalTo(true)));
        item.click(500);
        sleep(2000);
    }

    /** it clicks the "Allow" button on a permission request dialog */
    void grantPermission()  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            UiObject textAllow = this.mDevice.findObject(new UiSelector().text("Allow"));
            try {
                if (textAllow.exists() && textAllow.isClickable()) {
                    textAllow.click();
                }
            } catch (UiObjectNotFoundException e) {
                Log.e("", "no permissions dialog", e);
            }
        }
    }

    void flingUp(UiObject2 view, int speed, int pause) {
        Assert.assertThat(view, not(equalTo(null)));
        try {
            view.fling(Direction.DOWN, speed);
        } catch (StaleObjectException e) {
            Assert.fail();
        }
        sleep(pause);
    }

    void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}