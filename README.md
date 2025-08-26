# PE_OLD

(Temporarily while I can think of a better name).

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

* [X] Iron boat. What happens if you put a wooden boat in the lava?

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
* [X] optional fancy grass
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
* [X] Make corridors in hedge mazes hollow if they overwrite terrain!

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

	* [X] Nether should be somewhat themed. Good look on thinking on "paradise nether" features :-D 

	So level Themes should store separate (fixed or multi) biome info for both dimensions.

	* [X] Nether lighting works. It was a very, very stupid bug integrating starlight (it was in Chunk.setBlockIdAndMetadata)

	* [ ] Nether biomes / content todo:
		* [-] Attempt to do this with the least amount possible of new blocks and textures!

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
	* [ ] The ice palace in glacier.

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
* [-] Make it darker.
* [X] run the terraformer directy over the heightmap rather than carving the world after it's terrain generated!! And apply the modifier afterwards.

* [X] Cave vines
* [X] Adapt lake gens to work on podzol.
* [X] EntityItem spawns smoke in acid
* [X] Acid hurts
* [X] Render green fog under acid
* [X] Material.acid = Material.water
* [/] Add ballon to witch "I want ..." so you know what to give her.

## More

Make floating forests work. 

* [X] Fix maze checks
* [X] Make them work XD

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

* [X] Add the witch hut to the poison island. And make the witch home on that. Add a cat, also home on that.
	* [X] `setHomeArea` - API for homing is in `EntityLiving` but it's there so it can be used by some modern AI tasks. If I want normal mobs to be able to home, I'll have to do it myself. Maybe adapt this bit of entity AI:

```java
	public boolean shouldExecute() {
		if(this.theEntity.isWithinHomeDistanceCurrentPosition()) {
			return false;
		} else {
			ChunkCoordinates chunkCoordinates1 = this.theEntity.getHomePosition();
			Vec3D vec3D2 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, Vec3D.createVector((double)chunkCoordinates1.posX, (double)chunkCoordinates1.posY, (double)chunkCoordinates1.posZ));
			if(vec3D2 == null) {
				return false;
			} else {
				this.movePosX = vec3D2.xCoord;
				this.movePosY = vec3D2.yCoord;
				this.movePosZ = vec3D2.zCoord;
				return true;
			}
		}
	}
```

	* [X] Finally, I just patched where it selects a new random position.
	* [X] Be sure to save the home position.
	* [X] Add a method in Entity that is called right after being spawned in the world for the first time - but only if it's possible to not doing it if we are loading entities.

* [X] Add the stone arches in deserts.
* [X] Feature smaller biomes in biomed world theme and hell
* [X] Fix pistons in SMP (they work, but need the special Packet for the animation)

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

```properties
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

## Tree types - leaf types - Sapling types

I need a class or enum that, for a tree type, stores:

- Leaves BlockState.
- Trunk BlockState.
- Sapling BlockState.
- Get a worldgen to make sapling grow.

New leaves/log/sapling textures

line 16: leaves, fancy
line 17: leaves, fast
line 18: wood
line 19: sapling

Things to do to finish tree refactoring

* [X] Convert all trees to use the new enum for resources.
* [ ] Finish the ItemMulti shit and give proper names to all new blocks via the item.
* [ ] Think on a clever way to make this work in vanilla metas so I can reuse this in the b1666 mod.

**Added blockStateDropped to Block**. By default, this will call idDropped and damageDropped.

* [ ] Revise all themes again
* [X] grass as item is colourised ALL FACES but shouldn't
* [X] Coral wrong texture on inventory,
* [X] Sign twince on inventory; should show item not block tex.
* [X] Gui gets the brightness of the entity the cross is pointing to !? Didn't this happen before in either this or infhell? - Added `GL11.glDisable(GL11.GL_BLEND);` before drawing GUI
* [X] Change block of iron, gold, diamond textures for alpha's
* [X] Add lapis lazuli block.
* [ ] Option to turn layered sand off when creating new world.
* [X] Add smooth stone block, smelted from stone.
* [X] Nerf swarm spiders a bit so they poison less often and less time.
* [/] Prune world loaded entity list after a blood moon to kill excess entities. NEEDS TESTING
* [X] Make sure poison witch never dies or despawns.
* [ ] Make a smaller feature-size fossil so poison islands have bones too and can grow animals.

# When I'm ready to add new stuff

* [ ] Goats and the named entity dynamic.
* [ ] Backport all the custom command block shit.

# Hedge mazes - again

I want hedge mazes to carve thru rock - only. The way tey work is that hollow is created and then walls are painted, but that won't work for me i.e. for floating islands. I need to understand how mazes are built so I can change the code.

RN the maze generator generates a maze in a 2D array that looks like this:

```
	000000000000000005000000000000000 
	010121212121012121212121212101210 
	020000020002000005000200020200020 
	010121010101210555550101210121010 
	020202020200020555550202000002020 
	012101010121215555555101210121210 
	000002020200000555550200020000000 
	012101210101210555550121012121210 
	020000000202020005000002000000020 
	012101212101012121210121012101010 
	000202000000020000000200020202020 
	012101012121010121210121210121010 
	020002020002020200020200050000020 
	012121012101012121012105555501210 
	020000020202000000000205555502000 
	010121210101212121210155555551210 
	020002000200000000020205555502020 
	512101210101212121210105555501015 
	000202000202000005000005050002020 
	012101012101010555550555550121010 
	020000020002020555550555550200020 
	010121012101215555555555555101010 
	020202000200020555550555550202020 
	010101210121010555550555550121010 
	020200020002020005000005000000020 
	012101212121010121212121012121010 
	000200050000020200000002000202020 
	012105555501210101210121012101010 
	020005555502000202020200020002020 
	010155555551210101010121010101210 
	020205555500020202000002020200020 
	012105555501212101212121210121210 
	000000000000000005000000000000000 
	·x·x·x·x·x·x·x·x·x·x·x·x·x·x·x·x·
```

ofc 0 mean walls but I need to understand how this is translated to a real world maze i.e. blocks.

* Note how # of rows and # of columns is always an odd number.
* Note how even x or even z means WALL.

I believe that even / odd coordinates in the array translate to different sized block coordinates. Which may be the case, 'cause we have two attributes: `evenBias = 1` and `oddBias = 3`. We can think of a maze cell like this portion of the array, measuring 2x2:

```
	00
	01
```

Which should render, in blocks, to:

```
	 e/o\
	eWWWW
	/W···
	oW···
	\W···
```

Where W is a wall block, · is air, `e` is the even row/column of the main array and `o` is the odd row/column of the array.

This is a great initial piece of understanding I can use. Let's look again at the algorithm.

Yay I got it.

## Many level retries are because no valid spawn point is found.

On floating islands this may be 'cause no enough land is being generated. Or 'cause we are not trying hard enough.

I'm sure could fill a list of possible spawn points while creating the level and use one of those a random if normal spawn point search ends.

* [X] Hmmm But also, the algo that finds the spawn point makes a call to `getFirstUncoveredBlock` for the selected random x, z, but that method iterates from y = 63 upwards and assumes lowland is solid. So I need to write a different `canCoordinateBeSpawn` in `WorldProviderSky`. Or better, produce a better `World.getFirstUncoveredBlock`. It will receive a boolean to start looking from a hollow world bottom.

* [X] Make iron boats able to sail the acid.
* [ ] Make sure mazes still work on inslands.

