# CaptureTheFlag

> ⚠️ **Disclaimer**: This project is an old personal development. It has recently been updated and made public. It dates back to that time and has not been actively maintained since.

Spigot/Bukkit Capture The Flag plugin for Minecraft 1.8.8. It provides a team-vs-team game mode with flags, compass tracking, and match management.

## Features

* CTF mode with up to 2 teams.
* Map and position loading (spawn, flag, lobby).
* Dedicated commands for gameplay and team chat.
* Scoreboard and animations.

## Prerequisites

* Java 8
* Spigot 1.8.8 server (or compatible Bukkit)
* Maven (for building)

## Installation

1. Build the plugin.
2. Place the jar file into your server’s `plugins/` folder.
3. Start the server to generate the default configuration.

## Build

```bash
mvn clean package
```

The final jar is generated in the `target/` directory.

## Configuration

The main configuration is located in `src/config.yml` and is copied on first launch. Example:

* `locations.lobby`: lobby position
* `maps.<name>.red/blue.spawn`: team spawn locations
* `maps.<name>.red/blue.flag`: flag positions

## Commands

* `/ctf`: main CTF commands
* `/tc`: team chat

## Development

* Main class: `doryanbessiere.capturetheflag.minecraft.CaptureTheFlag`
* Plugin YAML: `src/plugin.yml`
