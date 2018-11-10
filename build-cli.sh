#!/usr/bin/env bash
set -ex

./gradlew clean shadowJar

$GRAALVM_HOME/bin/native-image -cp ./command-line/build/libs/command-line-all.jar -H:Name=linalyzer -H:Class=graphql.linalyzer.cli.Main --verbose
