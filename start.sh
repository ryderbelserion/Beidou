#!/bin/bash

./gradlew :beidou-bot:shadowJar --configuration-cache shadowJar

java -jar build/libs/Beidou-1.3.4.jar --token "$DISCORD_TOKEN"