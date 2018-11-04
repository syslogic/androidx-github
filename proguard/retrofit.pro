# Retrofit does reflection on generic parameters.

#InnerClasses is required to use Signature and EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
#-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
#-dontwarn retrofit2.-KotlinExtensions

-keep,includedescriptorclasses class okhttp3.internal.platform.AndroidPlatform
-keep class android.webkit.WebIconDatabase { void open(java.lang.String); }

-dontwarn okhttp3.internal.platform.*

-keep,includedescriptorclasses class com.sun.javadoc.**  { *; }
-keep,includedescriptorclasses class sun.security.ssl.** { *; }
-keep class org.apache.harmony.xnet.provider.jsse.** { *; }
-keep class com.android.org.conscrypt.** { *; }
-keep class sun.misc.Unsafe { *; }
-dontnote sun.misc.Unsafe

-dontnote java.security.cert.X509Certificate
-keep class java.security.cert.X509Certificate {
    public void findTrustAnchorByIssuerAndSignature();
}

-dontnote android.net.http.X509TrustManagerExtensions
-keep class android.net.http.X509TrustManagerExtensions {
    java.util.List checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String,java.lang.String);
}

-dontnote android.security.NetworkSecurityPolicy
-keep class android.security.NetworkSecurityPolicy {
    boolean isCleartextTrafficPermitted();
}

-dontnote javax.net.ssl.SSLSocket
-keep class javax.net.ssl.SSLSocket {
    public void remove();
    public void warnIfOpen();
}