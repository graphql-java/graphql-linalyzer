#!/usr/bin/env bash
set -ex

./gradlew clean shadowJar

$GRAALVM_HOME/bin/native-image -cp ./library/build/libs/library-all.jar -H:Name=linalyzer -H:Class=graphql.linalyzer.Main --verbose
