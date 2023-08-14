## androidx-github

This is an unofficial Android client for GitHub API, which utilizes the AndroidX & Retrofit2 libraries.<br/>
- by default, one can browse the own repositories and workflows.
- by default, one can browse Android/Kotlin/Gradle projects, but it features an editor for bookmarks.
- it generally supports git operations (check out only, so far), see [jgit](https://javadoc.io/doc/org.eclipse.jgit/org.eclipse.jgit/6.2.0.202206071550-r/org.eclipse.jgit/module-summary.html).

### Debug Builds

File `token.properties` may be created with a GitHub [access token](https://github.com/settings/tokens) declared as `accessToken=ghp_...`.<br/>
Without an access token the API will be "rate limited", after having loaded the first 10 pages in quick succession. When file `token.properties` is absent (or in release builds), the access token still can be added in the preferences.

### Security

Only GitHub traffic is being permitted, see [`network_security_config.xml`](mobile/src/main/res/xml/network_security_config.xml).

### Screenshots

![Repositories](screenshots/repositories_30.png?raw=true&sanitize=true "Repositories")<!-- @IGNORE PREVIOUS: link --><br/>
![Workflow Steps](screenshots/workflow_steps_30.png?raw=true&sanitize=true "Workflow Steps")<!-- @IGNORE PREVIOUS: link -->

 ---

### Android Library

The [`:library`](library/src/main/java/io/syslogic/github/api) module is available on JitPack; either by version tag or `master-SNAPSHOT`.<br/>
It provides the relevant GitHub API calls & data-models with data-binding annotations.<br/>
The JitPack repository URL would be: `maven { url 'https://jitpack.io' }`

    dependencies {
        // implementation "io.syslogic:androidx-github:master-SNAPSHOT"
        implementation "io.syslogic:androidx-github:1.0.1"
    }

[![Release](https://jitpack.io/v/syslogic/androidx-github.svg)](https://jitpack.io/#io.syslogic/androidx-github)
[![MIT License](https://img.shields.io/github/license/syslogic/androidx-github)](https://github.com/syslogic/androidx-github/blob/master/LICENSE)
