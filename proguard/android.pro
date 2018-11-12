# ProGuard Configuration Rules
# @see http://developer.android.com/guide/developing/tools/proguard.html
# @see http://proguard.sourceforge.net/manual/troubleshooting.html

-dontnote android.content.res.Resources
-keep class android.content.res.Resources {
    java.lang.reflect.Field mDrawableCache;
    java.lang.reflect.Field mUnthemedEntries;
}

-dontnote android.icu.text.DecimalFormatSymbols
-keep class android.icu.text.DecimalFormatSymbols {
    android.icu.text.DecimalFormatSymbols freeze();
}

-keep class android.support.v4.app.INotificationSideChannel

-keep class android.graphics.**
-dontnote android.graphics.**

-dontnote com.android.internal.view.menu.MenuBuilder
-dontnote android.content.res.ThemedResourceCache
-dontnote android.databinding.DataBinderMapper

# duplicate entries
-dontnote org.apache.http.params.**
-dontnote org.apache.http.conn.**
-dontnote android.net.http.**
-dontnote java.lang.invoke.**
