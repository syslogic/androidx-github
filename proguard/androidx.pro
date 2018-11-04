-verbose

-keep class androidx.** { *; }
-keep interface androidx.** { *; }
-keep class androidx.databinding.** { *; }
-keep class androidx.databinding.BindingBuildInfo { *; }

-keep class androidx.**.R$id { int title; int icon; }

-keep class androidx.core.content.res.FontResourcesParserCompat$** { *; }
-keep class androidx.core.content.res.ResourcesCompat$FontCallback
-keep class androidx.core.widget.TextViewCompat$OreoCallback { *; }
-keep class androidx.core.provider.FontsContractCompat$FontInfo
-keep class androidx.core.provider.FontRequest

-keep,includedescriptorclass class androidx.versionedparcelable.VersionedParcel
-keep,includedescriptorclass class androidx.core.app.NotificationManagerCompat { *; }
-keep,includedescriptorclass class androidx.core.app.NotificationCompatJellybean { *; }
-keep,includedescriptorclass class androidx.core.text.ICUCompat { *; }
-keep,includedescriptorclass class androidx.core.graphics.** { *; }
-keep,includedescriptorclass class androidx.fragment.app.FragmentTransition
-keep,includedescriptorclass class androidx.appcompat.app.ResourcesFlusher
-keep,includedescriptorclass class androidx.appcompat.widget.ViewUtils

-keep class androidx.core.app.NotificationCompat$Action {
    android.app.PendingIntent actionIntent;
}
-keep class androidx.core.app.NotificationManagerCompat {
    java.lang.String OP_POST_NOTIFICATION;
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
-keep class androidx.core.text.ICUCompat {
    java.lang.String addLikelySubtags(java.util.Locale);
}

-keep class android.support.v4.app.INotificationSideChannel
-keep class androidx.appcompat.view.menu.MenuBuilder

-dontnote android.icu.text.DecimalFormatSymbols
-keep class android.icu.text.DecimalFormatSymbols {
    android.icu.text.** freeze();
}

-keep class android.graphics.**
-dontnote android.graphics.**
