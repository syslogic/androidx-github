# ProGuard Configuration Rules
# @see http://developer.android.com/guide/developing/tools/proguard.html
# @see http://proguard.sourceforge.net/manual/troubleshooting.html

-keep class androidx.** { *; }
-keep interface androidx.** { *; }

-keep class androidx.databinding.** { *; }
-keep class androidx.databinding.BindingBuildInfo { *; }

-keep class androidx.**.R$id { int title; int icon; }

-keep class androidx.appcompat.view.menu.MenuBuilder
-keep class androidx.core.content.res.FontResourcesParserCompat$** { *; }
-keep class androidx.core.content.res.ResourcesCompat$FontCallback
-keep class androidx.core.widget.TextViewCompat$OreoCallback { *; }
-keep class androidx.core.provider.FontsContractCompat$FontInfo
-keep class androidx.core.provider.FontRequest

-keep,includedescriptorclass class androidx.appcompat.widget.ViewUtils
-keep,includedescriptorclass class androidx.versionedparcelable.VersionedParcel
-keep,includedescriptorclass class androidx.fragment.app.FragmentTransition { *; }
-keep,includedescriptorclass class androidx.core.app.NotificationCompatJellybean
-keep,includedescriptorclass class androidx.core.text.ICUCompat { *; }
-keep,includedescriptorclass class androidx.core.graphics.** { *; }

-dontnote androidx.transition.FragmentTransitionSupport

-dontnote androidx.core.graphics.TypefaceCompatApi24Impl
-keep class androidx.core.graphics.TypefaceCompatApi24Impl {
    java.lang.reflect.Method addFontWeightStyle(java.nio.ByteBuffer, int, java.util.List, int, boolean);
}

-dontnote androidx.core.graphics.TypefaceCompatApi26Impl
-keep class androidx.core.graphics.TypefaceCompatApi26Impl {
    java.lang.reflect.Method addFontFromAssetManager(android.content.res.AssetManager, java.lang.String, int, boolean, int, int, int, android.graphics.fonts.FontVariationAxis[]);
    java.lang.reflect.Method addFontFromBuffer(java.nio.ByteBuffer, int, android.graphics.fonts.FontVariationAxis[], int, int);
    java.lang.reflect.Method abortCreation();
}

-dontnote androidx.core.app.NotificationManagerCompat
-keep class androidx.core.app.NotificationManagerCompat {
    java.lang.reflect.Method checkOpNoThrow(int, int, java.lang.String);
}
-keep class androidx.core.app.NotificationManagerCompat {
    java.lang.String OP_POST_NOTIFICATION;
}
-keep class androidx.core.app.NotificationCompat$Action {
    android.app.PendingIntent actionIntent;
}
-keep class androidx.appcompat.app.** {
    boolean onMenuKeyEvent(android.view.KeyEvent);
}
-keep class androidx.recyclerview.widget.SortedList {
    java.lang.Object removeItemAt(int);
}
-keep class androidx.core.graphics.drawable.IconCompatParcelizer {
    androidx.core.graphics.drawable.IconCompat read(androidx.versionedparcelable.VersionedParcel);
}

-dontnote androidx.appcompat.widget.ViewUtils
-keep class androidx.appcompat.widget.ViewUtils {
    java.lang.reflect.Method makeOptionalFitsSystemWindows();
}

-dontnote androidx.core.text.ICUCompat
-keep class androidx.core.text.ICUCompat {
    java.lang.String addLikelySubtags(java.util.Locale);
}

-dontnote android.icu.text.DecimalFormatSymbols
-keep class android.icu.text.DecimalFormatSymbols {
    android.icu.text.DecimalFormatSymbols freeze();
}
