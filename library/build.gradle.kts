import com.android.build.api.dsl.LibraryExtension
import io.syslogic.gpr.GprMaintenance

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.maven.publish)
}

val groupId: String by extra(libs.versions.group.id.get())
val artifactId: String by extra(libs.versions.artifact.id.get())
val artifactName: String by extra(libs.versions.artifact.name.get())
val artifactDesc: String by extra(libs.versions.artifact.desc.get())
val githubHandle: String by extra(libs.versions.github.handle.get())
val githubEmail: String by extra(libs.versions.github.email.get())
val githubDev: String by extra(libs.versions.github.dev.get())

group = groupId
version = libs.versions.app.version.name.get()
base.archivesName = artifactId

// Room
room {
    schemaDirectory("${rootDir}/schema")
}

configure<LibraryExtension> {

    namespace = "io.syslogic.github.api"
    buildToolsVersion = libs.versions.android.build.tools.get()
    compileSdk = Integer.parseInt(libs.versions.android.compile.sdk.get())

    defaultConfig {
        minSdk = Integer.parseInt(libs.versions.android.min.sdk.get())
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFile("${project.rootDir}/proguard/consumer.pro")
    }

    sourceSets {
        named("main") {
            java.directories.add("src/main/java")
        }
        named("test") {
            java.directories.add("src/test/java")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
    }

    buildTypes {
        debug {
            // it breaks the data-binding, e.g. when running ./gradlew :library:publishToMavenLocal
            enableAndroidTestCoverage = false
            enableUnitTestCoverage = false
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
        }
    }

    lint {
        lintConfig = rootProject.file("lint.xml")
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = false
        showAll = false
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {

    // Material Design Components
    implementation(libs.material.design)

    // Annotations
    implementation(libs.androidx.annotation)

    // AppCompat
    implementation(libs.androidx.appcompat)

    // Data-Binding Runtime
    implementation(libs.androidx.databinding)

    // Room
    annotationProcessor(libs.androidx.room.compiler)
    androidTestAnnotationProcessor(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)
    api(libs.androidx.room.runtime)
    api(libs.androidx.room.paging)

    // Retrofit2
    implementation(libs.gson)
    implementation(libs.bundles.retrofit) {
        exclude(group = "com.google.code.gson", module = "gson")
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
}

// Gradle 9.0 deprecation fix
val implCls: Configuration by configurations.creating {
    extendsFrom(configurations.getByName("implementation"))
    isCanBeResolved = true
}

val javadocs by tasks.registering(Javadoc::class) {
    title = "GitHub API ${libs.versions.app.version.name.get()}"
    // classpath = files(File("${android.sdkDirectory}/platforms/${android.compileSdkVersion}/android.jar"))
    // android.getBootClasspath().forEach{ classpath += fileTree(it) }
    // classpath += fileTree(dir: project.file("build/tmp/aarsToJars/").absolutePath)
    // classpath += implCls
    // exclude("**/BuildConfig.java", "**/R.java", "**/*.kt")
    destinationDir = rootProject.file("build/javadoc")
    // source = sourceSets.main.get().allJava
    // options.links = "https://docs.oracle.com/en/java/javase/17/docs/api/"
    // options.linkSource = true
    // options.author = true
    isFailOnError = false

    onlyIf {
        project.file("build/intermediates/aar_main_jar").exists()
    }

    doFirst {

        // extract AAR files
        configurations["implCls"].files
            .filter { it.name.endsWith(".aar") }
            .forEach { aar: File ->
                copy {
                    from(zipTree(aar))
                    include("**/classes.jar")
                    into(project.file("build/tmp/aarsToJars/${aar.name.replace(".aar", "")}/"))
                }
            }

        // provide JAR, which contains the generated data-binding classes
        val aarMain = project.file("build/intermediates/aar_main_jar")
        if (aarMain.exists()) {
            copy {
                from(aarMain)
                include("**/classes.jar")
                into(project.file("build/tmp/aarsToJars/aar_main_jar/"))
            }
        }
    }

    doLast {
        // delete temporary directory.
        delete(project.fileTree("build/tmp/aarsToJars"))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    from(rootProject.file("build/javadoc"))
    archiveClassifier.set("javadoc")
    dependsOn(javadocs)
}

val sourcesJar by tasks.registering(Jar::class) {
    from(android.sourceSets.getByName("main").java)
    archiveClassifier.set("sources")
}

// Gradle 9.1 deprecation fix
configurations {
    @Suppress("UnstableApiUsage")
    consumable("jars") {
        outgoing.artifact(javadocJar)
        outgoing.artifact(sourcesJar)
    }
}

tasks.named("assemble") {
    dependsOn(javadocJar)
    dependsOn(sourcesJar)
}

afterEvaluate {

    publishing {
        publications {
            register<MavenPublication>("GitHub API") {
                from(components["release"])
                groupId = groupId
                artifactId = artifactId
                version = version
                pom {
                    name = artifactName
                    description = artifactDesc
                    url = "https://github.com/${githubHandle}/${artifactId}"
                    scm {
                        connection = "scm:git:git://github.com/${githubHandle}/${artifactId}.git"
                        developerConnection = "scm:git:ssh://github.com/${githubHandle}/${artifactId}.git"
                        url = "https://github.com/${githubHandle}/${artifactId}"
                    }
                    developers {
                        developer {
                            name = githubDev
                            email = githubEmail
                            id = githubHandle
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${githubHandle}/${artifactId}")
                if (System.getenv("GITHUB_ACTOR") != null && System.getenv("GITHUB_ACTOR") != null) {
                    credentials {
                        username = System.getenv("GITHUB_ACTOR")
                        password = System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}

/** GitHub Package Registry */
if (pluginManager.hasPlugin("io.syslogic.gpr.maintenance")) {
    configure<GprMaintenance> {
         groupId = groupId        // group
         packageName = artifactId // repo
         listPackagesAfterPublish = false
         deleteOnConflict = true
         deleteLastVersion = false
         logHttp = false
         pageSize = 30
    }
}
