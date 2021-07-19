# Bisq2 Prototype

[![build status](https://github.com/cbeams/bisq2/actions/workflows/build.yml/badge.svg)](https://github.com/cbeams/bisq2/actions/workflows/build.yml)


## Introduction

This repository prototypes an architecture vision for Bisq 2.0 (project codename Misq).


## Status

See the [project board](https://github.com/cbeams/bisq2/projects/1).


## Quick start

### Requirements

JDK 16.

Notes:
- JDK 11+ will work to run the build. Gradle will auto-provision JDK 16.
- Apple Silicon machines require additional setup. See Tips below.

### Build

    $ ./gradlew build

### Enable tab completion

The build generates Bash/ZSH completion scripts for `bisq*` commands:

    $ source ./bisq-completion.bash
    $ source ./bisqfx-completion.bash

    $ bisq <tab><tab>
    [ ... list of available subcommands ...]

    $ bisqfx -<tab>
    [ ... list of available options ...]

### Run the daemon

    $ bisqd

### Run the cli

    $ bisq -h

### Run the gui

    $ bisqfx -h


## Tips and troubleshooting

### Configure automatic PATH inclusion and bash completion for bisq commands

For the best user / dev experience, add the following to your `~/.bash_profile`:

    export BISQ_HOME=/path/to/this/repo
    export PATH=$PATH:$BISQ_HOME
    source $BISQ_HOME/bisq-completion.bash
    source $BISQ_HOME/bisqfx-completion.bash

Notes:

 - Do not attempt to `source bisq*.bash`. Bash's `source` can accept only one file at a time.
 - These bash completion scripts are also compatible with `zsh`.

### Tab completion errors

If you see `compopt: command not found` errors when attempting tab completion, it is because your `$SHELL` is pointing to a too-old version of bash. See [these instructions](https://github.com/Homebrew/homebrew-core/issues/18679#issuecomment-385442300) for how to fix the problem.

### For Apple Silicon users

M1 Macs require custom builds of JDK 16 and JavaFX 17 to build and run bisq correctly.

 - Manually install Azul's JDK 16 for M1 (aarch64) Macs
 - Manually install the OpenJFX 17-EA SDK from Gluon
 - Configure the build to use your custom SDK (see gradle.properties)

### Using a Gradle-provisioned JDK 16

Gradle auto-provisions JDKs into a non-standard location. To use it directly, e.g. when running `bisq*` commands, first find it

    ./gradlew -q javaToolChains

and then update your `JAVA_HOME` accordingly:

    export JAVA_HOME=/path/to/jdk16/Home

### Running BisqFX from within IDEA

Edit the run configuration and add the following VM arguments:

    --add-modules=javafx.controls --module-path=/path/to/javafx-sdk/lib
