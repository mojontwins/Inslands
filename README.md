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

# Meh

I did a very nice vanilla Indev in beta, it's so nice but:

1.- Add passing of days to get seasons working.
2.- SMP of course

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