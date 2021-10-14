## androidx-github

This is an unofficial Android client for GitHub, which utilizes the AndroidX & Retrofit2 libraries.<br/>
Currently one can browse Android/Kotlin/Gradle projects (working on an editor for the topics).

File `token.properties` must be created with a GitHub [access token](https://github.com/settings/tokens) declared as `accessToken=ghp_...`,<br/>
else the API will be "rate limited", after having loaded the first 10 pages in quick succession.

Make sure to use the new token format - when access fails, the token may need to be regenerated.

### Screenshots

![Repositories](screenshots/repositories.png?raw=true&sanitize=true "Repositories")
