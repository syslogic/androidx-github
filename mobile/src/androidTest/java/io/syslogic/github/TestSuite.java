package io.syslogic.github;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.runner.RunWith;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.Until;

import io.syslogic.github.constants.Constants;

import androidx.test.platform.app.InstrumentationRegistry;

/**
 * Graphical User Interface Test TestSuite
 * @author Martin Zeitler
 * @version 1.0.0
**/
@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({TestMainActivity.class, TestDetailActivity.class})
public class TestSuite {

    private static final int LAUNCH_TIMEOUT = 5000;

    protected String packageName;

    protected Context mContext;

    protected UiDevice mDevice;

    /**
     * uses package manager to find the package name of the device launcher.
     * usually this package is "com.android.launcher" but can be different at times.
     * this is a generic solution which works on all platforms.`
    **/
    protected String getLauncherPackageName() {

        /* create a launcher Intent */
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        /* use PackageManager to get the launcher package name */
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        PackageManager pm = context.getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        return resolveInfo.activityInfo.name;
    }

    /* launch the blueprint application */
    protected void startTestActivity(Context context, String className){

        /* initialize UiDevice */
        this.mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        Assert.assertThat(this.mDevice, CoreMatchers.notNullValue());

        /* start from the home screen */
        this.mDevice.pressHome();

        /* obtain the launcher package */
        String launcherPackage = getLauncherPackageName();
        Assert.assertThat(launcherPackage, CoreMatchers.notNullValue());

        /* wait for launcher */
        this.mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        /* setting the package name and obtaining the launch intent */
        this.packageName = context.getPackageName().replace(".test", "");
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(this.packageName);

        if(intent != null) {

            intent.setComponent(new ComponentName(this.packageName, this.packageName.replace("debug", "activity." + className)));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            Bundle extras = new Bundle();
            if(className.equals("DetailActivity")) {
                extras.putLong(Constants.ARGUMENT_ITEM_ID, 86554331);
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

    protected void scollDown(UiObject2 view, int percent, int pause) {
        view.scroll(Direction.DOWN, percent);
        sleep(pause);
    }

    protected void sleep(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}