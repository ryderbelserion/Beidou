#!/bin/bash

./gradlew :beidou-bot:shadowJar --configuration-cache shadowJar

~/.jdks/corretto-25.0.3/bin/java -jar build/libs/Beidou-1.2.0.jar --token "$DISCORD_TOKEN"