# CaptureTheFlag

> ⚠️ **Disclaimer** : Ce projet est un ancien développement personnellement. Il a été récemment mis à jour et rendu public. Il remonte à cette époque et n'a pas eu de maintenance active depuis.

Plugin Spigot/Bukkit de type Capture The Flag pour Minecraft 1.8.8. Il propose un mode de jeu équipe contre équipe avec drapeau, boussole et gestion de parties.

## Fonctionnalités

- Mode CTF avec 2 équipes maximum.
- Chargement de maps et positions (spawn, drapeau, lobby).
- Commandes dédiées au jeu et au chat d'équipe.
- Scoreboard et animations.

## Prérequis

- Java 8
- Serveur Spigot 1.8.8 (ou Bukkit compatible)
- Maven (pour compiler)

## Installation

1. Compile le plugin.
2. Dépose le fichier jar dans le dossier `plugins/` de ton serveur.
3. Démarre le serveur pour générer la configuration par défaut.

## Compilation

```bash
mvn clean package
```

Le jar final est généré dans `target/`.

## Configuration

La configuration principale se trouve dans `src/config.yml` et est copiée au premier lancement. Exemple :

- `locations.lobby` : position du lobby.
- `maps.<nom>.red/blue.spawn` : spawn par équipe.
- `maps.<nom>.red/blue.flag` : position du drapeau.

## Commandes

- `/ctf` : commandes principales du mode CTF.
- `/tc` : chat d'équipe.

## Développement

- Main class : `doryanbessiere.capturetheflag.minecraft.CaptureTheFlag`
- Plugin YAML : `src/plugin.yml`

## Licence

Aucune licence n'est indiquée dans le dépôt. Ajoute-en une si besoin.
