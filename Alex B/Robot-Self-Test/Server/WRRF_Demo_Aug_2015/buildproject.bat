@Echo off
Echo Deploying Robot Project
gradlew deploy  --offline -Dorg.gradle.java.home="C:\Users\Public\frc2019\jdk"
PAUSE 