## androidx-github

This is an unofficial Android client for GitHub, which utilizes the AndroidX & Retrofit2 libraries.<br/>
By default one can browse Android/Kotlin/Gradle projects, but it also features an editor for bookmarks.

### Client Setup

File `token.properties` may be created with a GitHub [access token](https://github.com/settings/tokens) declared as `accessToken=ghp_...`.<br/>
without personal access token the API will be "rate limited", after having loaded the first 10 pages in quick succession.<br/>
When file `token.properties` is absent, the personal access token still can be added in the preferences.

### Screenshots

![Repositories](screenshots/repositories_30.png?raw=true&sanitize=true "Repositories")<!-- @IGNORE PREVIOUS: link -->


[![](https://jitci.com/gh/syslogic/androidx-github/svg)](https://jitci.com/gh/syslogic/androidx-github) [![Release](https://jitpack.io/v/syslogic/androidx-github.svg)](https://jitpack.io/#io.syslogic/androidx-github)
[![MIT License](https://img.shields.io/github/license/syslogic/androidx-github)](https://github.com/syslogic/androidx-github/blob/master/LICENSE)

 ---

The library is also available on JitPack; either by version tag or `master-SNAPSHOT`.<br/>
The JitPack repository URL would be: `maven { url 'https://jitpack.io' }`

    dependencies {
        implementation "io.syslogic:androidx-github:1.0.0"
    }
