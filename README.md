# Bisq2 Prototype

## Introduction

High-level architecture prototype for Bisq 2.0 (project codename Misq).

## Requirements

- JDK 16+

## Import into IDEA

- Import as usual, but ensure that JDK 16 is selected in the project settings

## Build everything

    $ ./gradlew build

Scripts such as `bisq` and `bisqd` in the project root directory will now work.

For invocation convenience, update your path to include the current directory

    $ export PATH=$PATH:$PWD

## Source bash / zsh completion

    $ source ./bisq-completion.bash

    $ bisq <tab><tab>
    [ ... list of available subcommands ...]

## Run the daemon

    $ bisqd
    listening on port 2140

## Run the cli

    $ bisq <subcommand>

## Run the desktop

    $ bisqfx
