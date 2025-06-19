<img src="https://geysermc.org/img/geyser-1760-860.png" alt="Geyser" width="600"/>

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)](https://discord.gg/geysermc)
[![Crowdin](https://badges.crowdin.net/e/51361b7f8a01644a238d0fe8f3bddc62/localized.svg)](https://translate.geysermc.org/)

Geyser is a bridge between Minecraft: Bedrock Edition and Minecraft: Java Edition, closing the gap from those wanting to play true cross-platform.

Geyser is an open collaboration project by [CubeCraft Games](https://cubecraft.net).

## What is Geyser?
Geyser is a proxy, bridging the gap between Minecraft: Bedrock Edition and Minecraft: Java Edition servers.
The ultimate goal of this project is to allow Minecraft: Bedrock Edition users to join Minecraft: Java Edition servers as seamlessly as possible. However, due to the nature of Geyser translating packets over the network of two different games, *do not expect everything to work perfectly!*

Special thanks to the DragonProxy project for being a trailblazer in protocol translation and for all the team members who have joined us here!

## Supported Versions
Geyser is currently supporting Minecraft Bedrock 1.21.50 - 1.21.90 and Minecraft Java 1.21.5. For more information, please see [here](https://geysermc.org/wiki/geyser/supported-versions/).

## Setting Up
Take a look [here](https://geysermc.org/wiki/geyser/setup/) for how to set up Geyser.

## Links:
- Website: https://geysermc.org
- Docs: https://geysermc.org/wiki/geyser/
- Download: https://geysermc.org/download
- Discord: https://discord.gg/geysermc
- Donate: https://opencollective.com/geysermc
- Test Server: `test.geysermc.org` port `25565` for Java and `19132` for Bedrock

## What's Left to be Added/Fixed
- Near-perfect movement (to the point where anticheat on large servers is unlikely to ban you)
- Some Entity Flags

## What can't be fixed
There are a few things Geyser is unable to support due to various differences between Minecraft Bedrock and Java. For a list of these limitations, see the [Current Limitations](https://geysermc.org/wiki/geyser/current-limitations/) page.

## Compiling
1. Clone the repo to your computer
2. Navigate to the Geyser root directory and run `git submodule update --init --recursive`. This command downloads all the needed submodules for Geyser and is a crucial step in this process.
3. Run `gradlew build` and locate to `bootstrap/build` folder.

## Contributing
Any contributions are appreciated. Please feel free to reach out to us on [Discord](https://discord.gg/geysermc) if
you're interested in helping out with Geyser.

## Libraries Used:
- [Adventure Text Library](https://github.com/KyoriPowered/adventure)
- [CloudburstMC Bedrock Protocol Library](https://github.com/CloudburstMC/Protocol)
- [GeyserMC's Java Protocol Library](https://github.com/GeyserMC/MCProtocolLib)
- [TerminalConsoleAppender](https://github.com/Minecrell/TerminalConsoleAppender)
- [Simple Logging Facade for Java (slf4j)](https://github.com/qos-ch/slf4j)


## Experimental Features (Work in Progress)

This version of Geyser includes early implementations of the following experimental features, inspired by techniques used in servers like Cubecraft. Configuration and full functionality are still under development.

### Dynamic Entity Scaling
- Entities can be made to visually scale (grow/shrink) through animations.
- Animations (sequences of scaling steps) can be defined in Geyser's configuration.
- Triggers for these animations (e.g., player interaction with an entity) can also be configured.
- *Currently implemented: Configuration loading for animations, interaction-based triggers. Spawn-based triggers are planned.*

### Custom Entity Models via "Fake Players"
- Allows specific Java entities to be represented as player-like entities on Bedrock, which can then use custom models and skins defined in a Bedrock resource pack.
- This enables server administrators to create custom-looking entities for visual purposes.
- The mapping of Java entities to these custom "fake player" models (including the specific model/skin ID from the resource pack) will be configurable.
- *Currently implemented: Core entity class (`CustomModelPlayerEntity`) that spawns as a Bedrock player and can set a variant ID. A temporary test spawns this for AreaEffectClouds. Configuration for mapping and resource pack integration is planned.*

**Note:** These features are experimental. Their configuration and behavior may change in future updates.
