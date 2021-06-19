# Bisq2 Prototype

## Introduction

High-level architecture prototype for Bisq 2.0 (project codename Misq).

## Requirements

- JDK 16+

## Import into IDEA

- Import as usual, but ensure that JDK 16 is selected in the project settings

## Build and test

    $ ./gradlew build

## Run the daemon

    $ ./bisqd
    listening on port 2140

## Connect to the daemon

    $ ./bisq getprice
    39472
