# Bisq2 Prototype

## Introduction

This repository prototypes an architecture vision for Bisq 2.0 (project codename Misq). Watch the demo to see it in action and jump to the Quick Start section below to try it out for yourself.

### Demo

- TODO: asciinema / youtube

### Goals

- TODO

### Principles

- TODO

### Requirements

All you strictly need is JDK 11 or better installed. Bash or zsh is required to run `bisq*` commands with tab completion.

- JDK 11+ is required to run the Gradle build itself
- JDK 16 will be used by Gradle to build the project
- JDK 16 will be auto-provisioned by Gradle if it is not already installed (see Tips below)
- JDK 16 is required to run the `bisq*` commands in the project root directory

Notes:

 - M1 Mac users will need some special setup, see Tips below.


## Quick start

### Build everything

    $ ./gradlew build

When complete, the `bisq`, `bisqd` and `bisqfx` commands in the project root directory will work. See the Tips section below for automatically adding these scripts to your `$PATH`.

### Source bash / zsh completion

Bash completion scripts are automatically generated for `bisq*` commands. Use them as follows:

    $ source ./bisq-completion.bash

    $ bisq <tab><tab>
    [ ... list of available subcommands ...]

Notes:

 - Currently only the `bisq` command has a completion script
 - See the Tips section below if you have errors when attempting tab completion

### Run the daemon

    $ bisqd
    listening on port 2140

### Run the cli

    $ bisq <subcommand>

### Run the desktop

    $ bisqfx


## Tips and troubleshooting

### Configure automatic PATH inclusion and bash completion for bisq commands

For the best user / dev experience, add the following to your `~/.bash_profile`:

    export BISQ_HOME=/path/to/this/repo
    export PATH=$PATH:$BISQ_HOME
    source $BISQ_HOME/bisq*-completion.bash

Notes:

 - These bash completion scripts are also compatible with `zsh`.
 - The goal will be to automatically install these commands, their completion scripts and manpages using system-specific package manegers such as `brew`, `apt`, etc. so that everything "just works".

### Troubleshooting tab completion errors

If you see `compopt: command not found` errors when attempting tab completion, it is because your `$SHELL` is pointing to a too-old version of bash. See [these instructions](https://github.com/Homebrew/homebrew-core/issues/18679#issuecomment-385442300) for how to fix the problem.

### For M1 Mac users

M1 Macs require custom builds of JDK 16 and JavaFX 17 to build and run bisq correctly.

 - Manually install Azul's JDK 16 for M1 (aarch64) Macs
 - Manually install the OpenJFX 17-EA SDK from Gluon
 - Configure the build to use your custom SDK (see gradle.properties)

### Using a Gradle-provisioned JDK 16

If Gradle auto-provisions JDK 16 for you, you will need to know where it is in order to use it directly yourself, e.g. when running `bisq*` commands. To find it, run the following:

    ./gradlew -q javaToolChains

You will see the path to your Gradle-provisioned JDK 16 and you can then set your `JAVA_HOME` accordingly:

    export JAVA_HOME=/path/to/jdk16/Home
