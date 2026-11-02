#!/bin/bash

./gradlew :beidou-bot:shadowJar --configuration-cache shadowJar

~/.jdks/corretto-25.0.4.1/bin/java -jar build/libs/Beidou-1.3.4.jar --token "$DISCORD_TOKEN"