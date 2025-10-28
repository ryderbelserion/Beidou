#!/bin/bash

./gradlew shadowJar

~/.jdks/corretto-24.0.2/bin/java -jar build/libs/Beidou-1.0.0.jar --token "$DISCORD_TOKEN"