## androidx-github

This is an unofficial Android client for GitHub, which merely utilizes AndroidX & Retrofit2.<br/>
Currently one can browse Android/Kotlin/Gradle projects (working on an editor for dynamic topics).

File `token.properties` needs to be created with a GitHub [access token](https://github.com/settings/tokens) declared as `accessToken = ...`,<br/>
else the API will be "rate limited", after having loaded the first 10 pages in quick succession.

Make sure to use the new token format - when access fails, the token may need to be regenerated.
