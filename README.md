# Better Snowy Features

This is a simple mod that makes snowy/frozen biomes look a bit more polished. It's highly configurable and expandable through it's config file. 1.21.1+NeoForge coming soon!
This mod has a FULL version and a CLIENT-SIDE version.

Features:

1. Snowy Leaves

When a tree generates or grows in a snowy/frozen biome it will have snowy textures to fit better with the environment.

2. Snowy Plants

Grass, tall grass, ferns, large ferns and vines have snowy textures only in snowy/frozen biomes.

3. Permanent Snowy Grass Blocks

In snowy/frozen biomes grass will always look snowy, even if there's now snow layer on it, this makes green patches that stand out too much in snowy biomes disappear.

4. Snowy Overlays
   In snowy/frozen biomes stone, sand, blue ice, gravel and spruce planks will have a snowy overlay when they have snow blocks or snow layers on top. This is disabled by default on the config file since it impacts performance, it is recommended to use Embeddium or other performance mods along side this feature if you like it.

5. Plants + Snow
   Plants grow inside snow instead of making holes in the snow layer, the snow below plants is "fake" and just visually rendered when a plant block has 2 or more adjacent snow layers and is also in a snowy biome.

5. Icicles (Worldgen) (Only on FULL version)
   Icicles will generate underneath the leaves of trees in snowy biomes. This works for all tree types.

CONFIG FILE:
You can find the config file in ".minecraft\config\bsf-common.toml" or "bsf-client.toml" depending which on version you are using.
The config file will let you disable or enable features from the mod, use a fallback method for detecting cold biomes automatically (even modded biomes), and finally, edit the biome whitelist ↓ ↓ ↓

MOD COMPAT/MODPACK SUPPORT:
On the config's snowyBiomes list you can add or remove biomes to choose which ones get the mods features applied to them. If you don't want to manually add each biome to this list you can try "useTemperatureFallback = true" which will apply the features to all biomes where it can snow. If you now want to exclude some biomes from this automatic selection, you can use the second list snowyBiomesBlacklist.
Currently, the list includes modded biomes from the following mods: Biomes O' Plenty, Terralith, Oh The Biomes You'll Go, Regions Unexplored, Windswept and Oh The Biomes We've Gone.
The third list just makes dirt snowy in the selected biomes, this is only for rare cases where biomes have snow blocks on the surface and on slopes dirt blocks may peek through, which looks really bad.

RESOURCEPACK COMPAT (WIP):
Since the mod changes a bunch of textures with it's own files they may look out of place for resourcepack users. While I can't really do anything about this I can make it easier for you. In the GitHub releases page you will find "template" resourcepacks for popular packs like Mizuno's or Bare Bones, these will ONLY include the textures for the snowy overlays not any leaves or plants made in the style of each resourcepack. You can use these templates to make you own resourcepacks for the mod or create them from scratch.

CHECK OUT THE MOD HERE: https://www.curseforge.com/minecraft/mc-mods/bsf