# androidx-github

This is an Android client for GitHub, which merely utilizes AndroidX & Retrofit2.

One can browse Android/Kotlin/Gradle projects (working on an editor for the topics to browse).

File `token.properties` needs to be created and have a GitHub access token defined with `accesstoken=...`,

else the API will be "rate limited", after having loaded the first 10 pages in quick succession.
