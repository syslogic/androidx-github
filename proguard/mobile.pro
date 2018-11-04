# ProGuard Configuration Rules
# @see http://developer.android.com/guide/developing/tools/proguard.html
# @see http://proguard.sourceforge.net/manual/troubleshooting.html

-printconfiguration ../results/proguard.txt

-keep public class **.BuildConfig { *; }

-keepclassmembers class **.R$* {public static <fields>;}

-keep,includedescriptorclasses class io.syslogic.github.databinding.** { *; }
