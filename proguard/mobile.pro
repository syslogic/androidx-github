# ProGuard Configuration Rules
# @see http://developer.android.com/guide/developing/tools/proguard.html
# @see http://proguard.sourceforge.net/manual/troubleshooting.html

-printconfiguration ../results/proguard.txt
-verbose

# Retrofit does reflection on generic parameters.
#InnerClasses is required to use Signature and EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

-keep public class **.BuildConfig { *; }

-keepclassmembers class **.R$* {public static <fields>;}

-keep,includedescriptorclasses class io.syslogic.github.databinding.** { *; }
