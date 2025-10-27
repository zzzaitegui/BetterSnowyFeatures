# Better Snowy Features

This is a simple mod that makes snowy/frozen biomes look a bit more polished. It's highly configurable and expandable through simple datapacks and a config file. 
This mod has a FULL version and a CLIENT-SIDE version. 

Features: 
1. Snowy Leaves
When a tree generates or grows in a snowy/frozen biome it will have snowy textures to fit better with the environment. 

2. Snowy Plants
Grass, tall grass, ferns, large ferns and vines have snowy textures only in snowy/frozen biomes.

3. Permanent Snowy Grass Blocks
In snowy/frozen biomes grass will always look snowy, using the same overlay that applies when you put a snow layer over a normal grass block, this makes green patches that stand out too much in snowy biomes disappear. 

4. Plants + Snow
Plants grow inside snow instead of making holes in the snow layer, the snow below plants is "fake" and just visually rendered when a plant block has 2 or more adjacent snow layers and is also in a snowy biome.

5. Icicles (Worldgen) (Only on FULL version)
Icicles will generated underneath the leaves of trees that generate in snowy biomes. This works for all tree types.

DATAPACK/MOD SUPPORT:
This mods uses tags to know what biomes are snowy/frozen biomes. If you want to the add/remove the mod's features to other biomes or even modded biomes you just need to add/remove the biome name from the tag, although to remove biomes remember to use (replace": true).
There are 2 tags, "snowy_biomes.json" which adds the snowy foliage and icicles to biomes on that tag and "snowy_block_biomes.json" which adds the snowy (white) dirt to the biomes on it. 
The mod already has tags for the following mods: Biomes O' Plenty, Oh The Biomes We've Gone, Regions Unexplored and Terralith. 

CONFIG FILE: 
If you don't want specific features from the mod, in the config file you can disable snowy plants (grass, ferns, tall grass and large ferns), so they have the normal vanilla texture + they won't grow inside snow layers, you can also disable icicle generation.
