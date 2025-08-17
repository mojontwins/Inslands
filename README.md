# PE_OLD

(Temporarily while I can think of a better name).

Ideas:

- Worlds are limited to the circular buffer, which will be made fixed.
- All the world is calculated on world creatinon, that 32x32 chunks area, somehow.

So:

1.- Make it so the buffer is not circular and no chunks x, z < 0 or x, z > 31 are generated.
2.- Make it so you can't exit the world from the side.

Then:

Expand!

1.- The nether should have nether walls and be 256x256
2.- Position in the nether is overworld x 2

Than:

Go crazy!

1.- Make chunks in the overworld 256 blocks tall. Generate the sky dimension on top of the normal dimension.
2.- Indev level themes.
3.- Profit!

## Meh

I did a very nice vanilla Indev in beta, it's so nice but:

1.- Add passing of days to get seasons working.
2.- SMP of course

# 2nd iteration

Most of the stuff never got done, but I have limited worlds with Indev themes. Now I want to take every theme and make a slightly different experience. The 1st I'm visiting is the Hell theme which would need a ways to make trees renewable. Remember that trees spawn without canopy.

THe way is using acorn seeds, which you can plan in the groudn to get oak saplings. Acorn seeds are sometimes dropped by skeletons.  

# Wha

Added 0 notes during months. It's quite evolved and works great. Uses lightmaps and starlight and actually runs at a very good frame rate on very old or very limited hardware, which was the goal.

Now spice it up a bit:

* Define a proper gameplay for each level theme.
* Add the poison island level theme.

# Different gameplays for different level themes

* [X] Rename world generators to "Infdev Island", "Alpha Island" and "Floating Island".
* [X] Base achievements for hell, forest & paradise
* [X] Eat animation from infhell
* [X] Open creative inventory directly fix like infhell

## Hell

* [X] Very tough survival, also wood is a very valuable resource as most trees come up without canopy so you can't get saplings that way. Skeletons drop acorns which you should plant in tilled land to grow saplings. --> 
	* [X] Needs testing - in hell it needs to be fertilized with bonemeal otherwise tilled land will decay and acorn will drop, which is nice.

* [X] Thing something that has to do with fossils! Well, they are a huge source of bone meal, there should be a way to produce GRASS using them (A very small chance?) 
	* [?] Fine tune... Why shrooms pop out as drops sometimes?

* [X] Big mushrooms should have a purpose. ¿Smelt into coal? Maybe a smaller coal item that produces just 2 torches and burns for 1/2 the time.
	* [X] Item charcoal
	* [X] Smelting recipes (to obtain, as fuel)
	* [X] Crafting recipes: combine with stick for 2 torches

* [ ] Iron boat. What happens if you put a wooden boat in the lava?

* Achievements
	* [X] Grass from dirt - Fertilize dead soil and bring it back to life
	* [X] Plant an acorn - Plant an acorn on tilled field
	* [X] Fertililze an accorn
	* [X] Cook big shrooms - To get some cheap charcoal

## Forest

* Asegurarse de generar un laberinto.

* Idear una progresión tipo twilight, con cosas que hacer para hacer otras cosas.

## Paradise

* Paradise level should trigger "sky" generator by default (you can change it ofc)

* This was supposed the "peaceful" or "easy" variant, but I think something should be done to this. I know (or I believe) he original Aether was somewhat inspired in this. It would be nice to do some research and know where the aether came from and what were the initial builds and what kind of stuff they had and do something similar.

* Add big flowers and some Aether stuff: bronze dungeons and some mobs would be a nice start.

* [X] The sky generator generated a FLOOR??? at y = 0
* [X] Ported Aether stuff
* [X] Ultra bothered by the fact that apparently a bunch of mobs are being spawned in the sun light?!!?

## Forest

* [X] I've added good ol' mazes but I'm adding the "good" maze for non sky forests. The minotaur maze.
* [X] Everything (maze) is ported - have to sort out the blocks used. 
* [X] Backport minotaurs and minoshrooms. They use new AI... 1. Try them as is as most of the new AI foundation is there - and 2. If taht fails, it should be easy to back port them to the old system.

--

* [X] Port kobolds and/or redcaps. Think on where/purpose. Both could be natural spawns if !blockcanseethesky stuff. Or whatever. Dunno if there's backend for cave monsters ?
* [ ] Add more spawners to mazes?
* [ ] Achievement
	[ ] Hunt Minotaur
	[X] Hunt Minoshroom.
	[X] Whatever with kobolds/redcaps.
* [X] Add special axe that axes a lot, which is what the minoshroom drops.

--

* [X] Added a means to re-generate the world if conditions are not met.
	* [X] Forest maze conditions:
		* [X] Small sized or floating island based: at least 1 random hill maze
		+ [X] Normal sized: 1 minotaur maze spawning in the central chunk. 
		* [X] Big/Huge sized: 1 minutaur maze per 16x16 chunk subregion.

		* Minotaur mazes have a 14 block high entrance. Condition is that they must generate at 64+14 = 78 or below, in the open.
		
* [ ] Message "retrying world generation".
* [X] Modify feature provider so it doesn't try outside of the map!
* [X] Very important modification! upon processing the entity list, only update those within a square radius of 8 chunks in every direction, which saves TONS of time in BIG and HUGE levels! 

* [ ] Themed dungeons ? was that a thing? (i.e. select spawner characteristics)

## Added optifine

Yay! fps boosted on shitty systems!

## New stuff

* [X] Use b173 Twilight Forest entities for animales in the forest theme.
* [X] Undo (if done) the level theme based temperature/humidity and extract it from the biome.
* [X] Make it so if the level theme based fixed biome is null, it uses the normal ramp for a new theme "biomes".
* [X] Attempt to make a more flexible day cycle management (i.e. fixed celestial angle based upon level theme, not world provider) 
	(RN how does it do the fixed hour in paradise / hell? Can't understand HOW!!) 

	In hell you can get night, not in paradise. The only difference is on level theme settings: dayCycle = false. But that flag only seems to be used to run the bad moon logic or not.

	OH. It seems to be tied to the world provider. FUCK. It is fixed to 0 for the world provider sky. I would get day cycle in another world provider for paradise. Time to make properl logic for this.

	`fixedCelestialAngle`.

	* For hell: Day cycle.
	* For forest: Check vanilla twilite
	* For paradise: `1.0F` (still doesn't seem to affect????  HOW TF DO I DO THIS LOL)

	Celestial angle is advancing normally, or so it seems. So it has to be lightmap related? Nah I was being stupid. Paradise is summer so time set night which sets 13000 still gets daylight.

	OK

	Forget the ton of paragraphs. Implemeting this rn.



* [X] Add "Override level test" to level creation extra options to override "level is valid" check
* [X] Add a biome map. Simple, don't over do it. Use PostAlpha's. Which is almost vanilla's. Recreate new biomegen classes. BiomeGenBetaForest... etc
* [X] Remove optifog log and other stupid shit.
* [X] Add "preferredWorldgen" to levelthemes and de-hardcode it
* [X] Add torches to some trees, somehow
* [X] Biomes level theme uses PostAlpha ramps;
* [ ] optional fancy grass?
* [X] Make sure the needed initialization is due so multibiomes work in SMP
* [X] Renderpass for shiny eyes is broken and entities show white.

* [x] Special renderbiped for 2 layer-skin bipeds. Need to research how renderpass armor is made "bigger" than the main body.

	* RenderPlayer has 3 models: `modelBipedMain`, `modelArmorChestPlate` and `modelArmor`.
	* On object instantiation, `modelBipedMain` is assigned a "new ModelBiped(0.0F)".
	* `modelArmorChestPlate` is a `ModelBiped(1.0F);`
	* `modelArmor` is a `ModelBiped(0.5F);`
	* Constructor `ModelBiped(f1)` calls `ModelBiped(f1, 0)`. 
	* Constructor `ModelBiped(f1, f2)` uses f2 as an angle for the (unused?) headwear box, and f1 for each `addBox` method call.
	* **That value is used to be subtracted/added to vertex coordinates, effectively making every box bigger (or smaller)**
	* So that's what I need: an extra `ModelBiped(0.5F)` mapped to the extra texture?

	I need to add an offset to `modelBiped` so it can pick up the lower half of the texture by default for the 2nd layer.
	
* [X] Port eat animation from Infhell
* [ ] Make corridors in hedge mazes hollow if they overwrite terrain!

# More

* [ ] Nether!
	* [/] Nether should be 1/2 the size of the overworld in sizes >= normal or 1/1 in size small. The chunk provider should take this in account automaticly. The teleporter should also convert coordinates accordingly.
	* [/] Nether should have cool all-bedrock (with holes) walls. Think on a nice algorithm to make irregular surfaces in all 4 lateral sides.

	In my first attempt, I made it so the nether is actually half the size, and the Provider adjusts and returns the fake chunk for x/z = half, but this is not satisfactory as the bedrock in these chunks can't be lit.

	So maybe the thing would be having the provider generate the full world size but only having a 1/2 sized portion in the center, generating REAL chunks of bedrock surrounding it, via the WorldProviderHell.

	So when travelling to the nether, `X = SizeX/4 + X/2`. When going back, `X = (X - SizeX/4) * 2`.

	Also Starlight is NOT running when creating the nether, but it does when reloading the world.
	
	So new approach: 

	* [X] add `getMinXChunk/getMaxXChunk` methods and apply where necessary - i.e. when spawning animals.
	* [X] Change the nether chunk provider generate to generate actual bedrock filled chunks for `X < SizeX/4` and `X >= 3*SizeX/4`. (for the small size, just generate 1 chunk wide border)	  

	COOL. Now the nether spawns correctly but there's no lights still. Need to check why is this.

	* [ ] Nether should be somewhat themed. Good look on thinking on "paradise nether" features :-D 

	So level Themes should store separate (fixed or multi) biome info for both dimensions.

	* [X] Nether lighting works. It was a very, very stupid bug integrating starlight (it was in Chunk.setBlockIdAndMetadata)

	* [ ] Nether biomes / content todo:
		* [ ] Attempt to do this with the least amount possible of new blocks and textures!

		* [ ] Create new fire proof, colorisable leaf block.
		* [ ] Create new fire proof, colorisable log block.
		* [ ] Use them for custom trees.

		Bloodbark was gross but great for hell.

		* [ ] Paradise theme nether should be bluish with blue lit stuff like transparents mushrooms and crystals. 
		* [ ] Hell's nether is fleshy. I need vine-like hanging guts, a meta block, eyes and shit. Look for eye-themed mobs in old mods.
		* [ ] Forest's nether should be full of trees, mushrooms and vines.

* [ ] Think about gameplay items for a desert and a glacier based theme (for the future).
	* [ ] both lack trees, dirt and saplings must be obtained. Make sure skeletons still drop acorns.
	* [ ] cold mechanics. Need to reactivate code for freeze


# Server

* [ ] {SMP} Add TP to SMP
* [ ] {SMP} Still no border chunks.
* [X] {SMP} TileEntityMobSpawnerOneShot not working.
* [ ] {SMP} Calculate spawn point properly.
* [ ] {SMP} Client is no aware of effects from the Server.
* [X] {SMP} Packet52 stuff (multiblock change) not 8bit metadata aware!

# Poison Island

Port/adapt from the Indev Modloader example. 

## Achievements

* "Welcome to the Poison Island" - create a world.
* "A trusty container" - "Craft a bottle using glass panes"
* "Somewhere to cook!" - "Toss diamonds to the old man and obtain a cauldron!"
* "A good start" - "Add water to a cauldron"
* "A source of food" - "Experiment with mushrooms to make soup"
* "Don't let it spill!" - "Obtain acid"
* "A powerful weapon" - "A strong disolvent in the cauldron with the right mushrooms..."
* "Poison!" - "Obtain poison!"

## DO / FIX

* [X] Biome causes lots of lag over time for some reason. Find what it is . I was adding mobs as animals
* [X] Find the way to make the theme be able to fire a secondary cave carver above the sea level.
* [ ] Make it darker.
* [X] run the terraformer directy over the heightmap rather than carving the world after it's terrain generated!! And apply the modifier afterwards.

* [X] Cave vines
* [X] Adapt lake gens to work on podzol.
* [X] EntityItem spawns smoke in acid
* [X] Acid hurts
* [X] Render green fog under acid
* [X] Material.acid = Material.water
<<<<<<< HEAD
* [/] Add ballon to witch "I want ..." so you know what to give her.
* [ ] Add ballon to witch "I want ..." so you know what to give her.

## More

Make floating forests work. 

* [ ] Fix maze checks
* [ ] Make them work XD

## TF Mazes

Given W, H, mazes are `W*4 x H*4` blocks in size. Almosth therere. Problems:

- 100% empty cells not carving.
- Floating islands maze blocks should only substitute dirt, stone, gravel, grass, ores.

# Relive the project

After 8 months... I'd refactor and reorganize everything in packages like my other projects. Then I need to add game hints that describe stuff you can do:

- Use bonemeal on dirt to get a chance to get grass.
- Animals only spawn in well lit grass!
- Give diamonds to the sorceress!

Also

[ ] Add the witch hut to the poison island.
[ ] Add the stone arches in deserts.
[ ] Feature smaller biomes in biomed world theme and hell

# Removing the need to rely on the original minecraft.jar

* Removed it from the project, errors of missing stuff:

	* SoundManager.java
		* paulscode.sound.SoundSystem
		* paulscode.sound.SoundSystemConfig
		* paulscode.sound.codec.CodecJOrbis
		* paulscode.sound.codec.CodecWav
		* paulscode.sound.libraries.LibraryLWJGLOpenAL

	* CodecMus.java
		* paulscode.sound.codecs.CodecJOrbis

* Added the paulscode soundsystem as found in the MinecraftForge github. It still lacks codecs.

* Completed with https://github.com/kovertopz/Paulscode-SoundSystem/tree/master and got everything I needed.

* Of course, now I lack lwgl in the library path (no minecraft.jar)... I have to solve this and I'll be set!

# This is standalone nowyou

Client->Export jar generates a .jar you can use directly. **Make sure you uncheck the lib folder**. in betacraft. Add a b1.7.3 equivalent .info in the versions/json folder or something like this:

```info
release-date:1755269895000
compile-date:1755269894000
url:https://launcher.mojang.com/v1/objects/43db9b498cb67058d2e12d394e6507722e71bb45/client.jar
launch-method:indev
launch-method-link:
proxy-args:-Dhttp.proxyHost=betacraft.uk -Dhttp.proxyPort=11705
other-name:Inslads-latest
protocolVersion:beta_14
file-ver:1
```

Server->Export executable jar

# Bigger texture atlases

Voy a adaptar el código para poder ampliar a placer el atlas de texturas.



# Clean / simplify

There are several blocks that produce different blockstates depending on metadata. Each of them use a special subclass of ItemBlock that's very simmilar. I want to reuse the same in every one of them:

* BlockSapling - ItemSapling, placedBlockMetadata = m, iconFromDamage: side 0
* BlockSapling - ItemSapling2, lo mismo.