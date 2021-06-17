# Bisq2 Prototype

## Introduction

This repository demonstrates a high-level architecture vision for Bisq 2.0.

## Requirements

- JDK 16+

## Import into IDEA

- Import as usual, but ensure that JDK 16 is selected in the project settings

## Build and test

    $ ./gradlew build

## Run the server

    $ ./bisqd
    listening on port 9999

The server will listen on port 9999, accept a single connection, return the value '42' and exit.

## Connect to the server

    $ ./bisq-cli
    42
