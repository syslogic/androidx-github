## androidx-github

This is an unofficial Android client for GitHub, which utilizes the AndroidX & Retrofit2 libraries.<br/>
By default one can browse Android/Kotlin/Gradle projects, but it also features an editor the topics.

### IDE Setup

File `token.properties` must be created with a GitHub [access token](https://github.com/settings/tokens) declared as `accessToken=ghp_...`,<br/>
else the API will be "rate limited", after having loaded the first 10 pages in quick succession.<br/>
Make sure to use the new token format - when access fails, the token may need to be regenerated.

### Screenshots

![Repositories](screenshots/repositories_30.png?raw=true&sanitize=true "Repositories")
