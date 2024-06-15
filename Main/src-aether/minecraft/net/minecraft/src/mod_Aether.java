package net.minecraft.src;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.client.Minecraft;

import org.lwjgl.opengl.GL11;

public class mod_Aether extends BaseMod implements IInterceptBlockSet {
	private static DimensionAether dim;
	private static World world;
	private static World nbtWorld;
	private static long clock;
	private boolean cloudPara = false;
	private Random rand = new Random();
	private float zLevel = -90.0F;
	private int updateCounter;
	@MLProp
	public static int raritySwet = 8;
	@MLProp
	public static int rarityAechorPlant = 8;
	@MLProp
	public static int rarityCockatrice = 3;
	@MLProp
	public static int rarityAerwhale = 8;
	@MLProp
	public static int rarityZephyr = 5;
	@MLProp
	public static int raritySheepuff = 10;
	@MLProp
	public static int rarityPhyg = 12;
	@MLProp
	public static int rarityMoa = 10;
	@MLProp
	public static int rarityFlyingCow = 10;
	@MLProp
	public static int rarityWhirlwind = 8;
	@MLProp
	public static int rarityAerbunny = 11;
	@MLProp
	public static boolean worldMenu = true;
	@MLProp
	public static boolean aetherMenu = true;
	@MLProp
	public static boolean TMIhidden = true;
	@MLProp
	public static int idBlockAetherPortal = 165;
	@MLProp
	public static int idBlockAetherDirt = 166;
	@MLProp
	public static int idBlockAetherGrass = 167;
	@MLProp
	public static int idBlockQuicksoil = 168;
	@MLProp
	public static int idBlockHolystone = 169;
	@MLProp
	public static int idBlockIcestone = 170;
	@MLProp
	public static int idBlockAercloud = 171;
	@MLProp
	public static int idBlockAerogel = 172;
	@MLProp
	public static int idBlockEnchanter = 173;
	@MLProp
	public static int idBlockIncubator = 174;
	@MLProp
	public static int idBlockLog = 175;
	@MLProp
	public static int idBlockPlank = 176;
	@MLProp
	public static int idBlockSkyrootLeaves = 177;
	@MLProp
	public static int idBlockGoldenOakLeaves = 178;
	@MLProp
	public static int idBlockSkyrootSapling = 179;
	@MLProp
	public static int idBlockGoldenOakSapling = 180;
	@MLProp
	public static int idBlockAmbrosiumOre = 181;
	@MLProp
	public static int idBlockAmbrosiumTorch = 182;
	@MLProp
	public static int idBlockZaniteOre = 183;
	@MLProp
	public static int idBlockGravititeOre = 184;
	@MLProp
	public static int idBlockEnchantedGravitite = 185;
	@MLProp
	public static int idBlockTrap = 186;
	@MLProp
	public static int idBlockChestMimic = 187;
	@MLProp
	public static int idBlockTreasureChest = 188;
	@MLProp
	public static int idBlockDungeonStone = 189;
	@MLProp
	public static int idBlockLightDungeonStone = 190;
	@MLProp
	public static int idBlockLockedDungeonStone = 191;
	@MLProp
	public static int idBlockLockedLightDungeonStone = 192;
	@MLProp
	public static int idBlockPillar = 193;
	@MLProp
	public static int idBlockZanite = 194;
	@MLProp
	public static int idBlockQuicksoilGlass = 195;
	@MLProp
	public static int idBlockFreezer = 196;
	@MLProp
	public static int idBlockWhiteFlower = 197;
	@MLProp
	public static int idBlockPurpleFlower = 198;
	@MLProp
	public static int idBlockAetherBed = 199;
	@MLProp
	public static int idItemVictoryMedal = 17000;
	@MLProp
	public static int idItemKey = 17001;
	@MLProp
	public static int idItemLoreBook = 17002;
	@MLProp
	public static int idItemMoaEgg = 17003;
	@MLProp
	public static int idItemBlueMusicDisk = 17004;
	@MLProp
	public static int idItemGoldenAmber = 17005;
	@MLProp
	public static int idItemAechorPetal = 17006;
	@MLProp
	public static int idItemStick = 17007;
	@MLProp
	public static int idItemDart = 17008;
	@MLProp
	public static int idItemDartShooter = 17009;
	@MLProp
	public static int idItemAmbrosiumShard = 17010;
	@MLProp
	public static int idItemZanite = 17011;
	@MLProp
	public static int idItemBucket = 17012;
	@MLProp
	public static int idItemPickSkyroot = 17013;
	@MLProp
	public static int idItemPickHolystone = 17014;
	@MLProp
	public static int idItemPickZanite = 17015;
	@MLProp
	public static int idItemPickGravitite = 17016;
	@MLProp
	public static int idItemShovelSkyroot = 17017;
	@MLProp
	public static int idItemShovelHolystone = 17018;
	@MLProp
	public static int idItemShovelZanite = 17019;
	@MLProp
	public static int idItemShovelGravitite = 17020;
	@MLProp
	public static int idItemAxeSkyroot = 17021;
	@MLProp
	public static int idItemAxeHolystone = 17022;
	@MLProp
	public static int idItemAxeZanite = 17023;
	@MLProp
	public static int idItemAxeGravitite = 17024;
	@MLProp
	public static int idItemSwordSkyroot = 17025;
	@MLProp
	public static int idItemSwordHolystone = 17026;
	@MLProp
	public static int idItemSwordZanite = 17027;
	@MLProp
	public static int idItemSwordGravitite = 17028;
	@MLProp
	public static int idItemIronBubble = 17029;
	@MLProp
	public static int idItemPigSlayer = 17030;
	@MLProp
	public static int idItemVampireBlade = 17031;
	@MLProp
	public static int idItemNatureStaff = 17032;
	@MLProp
	public static int idItemSwordFire = 17033;
	@MLProp
	public static int idItemSwordHoly = 17034;
	@MLProp
	public static int idItemSwordLightning = 17035;
	@MLProp
	public static int idItemLightningKnife = 17036;
	@MLProp
	public static int idItemGummieSwet = 17037;
	@MLProp
	public static int idItemHammerNotch = 17038;
	@MLProp
	public static int idItemPhoenixBow = 17039;
	@MLProp
	public static int idItemCloudParachute = 17040;
	@MLProp
	public static int idItemCloudParachuteGold = 17041;
	@MLProp
	public static int idItemCloudStaff = 17042;
	@MLProp
	public static int idItemLifeShard = 17043;
	@MLProp
	public static int idItemGoldenFeather = 17044;
	@MLProp
	public static int idItemLance = 17045;
	@MLProp
	public static int idItemIronRing = 17046;
	@MLProp
	public static int idItemGoldRing = 17047;
	@MLProp
	public static int idItemZaniteRing = 17048;
	@MLProp
	public static int idItemIronPendant = 17049;
	@MLProp
	public static int idItemGoldPendant = 17050;
	@MLProp
	public static int idItemZanitePendant = 17051;
	@MLProp
	public static int idItemRepShield = 17052;
	@MLProp
	public static int idItemAetherCape = 17053;
	@MLProp
	public static int idItemLeatherGlove = 17054;
	@MLProp
	public static int idItemIronGlove = 17055;
	@MLProp
	public static int idItemGoldGlove = 17056;
	@MLProp
	public static int idItemDiamondGlove = 17057;
	@MLProp
	public static int idItemZaniteGlove = 17058;
	@MLProp
	public static int idItemZaniteHelmet = 17059;
	@MLProp
	public static int idItemZaniteChestplate = 17060;
	@MLProp
	public static int idItemZaniteLeggings = 17061;
	@MLProp
	public static int idItemZaniteBoots = 17062;
	@MLProp
	public static int idItemGravititeGlove = 17063;
	@MLProp
	public static int idItemGravititeHelmet = 17064;
	@MLProp
	public static int idItemGravititeBodyplate = 17065;
	@MLProp
	public static int idItemGravititePlatelegs = 17066;
	@MLProp
	public static int idItemGravititeBoots = 17067;
	@MLProp
	public static int idItemPhoenixGlove = 17068;
	@MLProp
	public static int idItemPhoenixHelm = 17069;
	@MLProp
	public static int idItemPhoenixBody = 17070;
	@MLProp
	public static int idItemPhoenixLegs = 17071;
	@MLProp
	public static int idItemPhoenixBoots = 17072;
	@MLProp
	public static int idItemObsidianGlove = 17073;
	@MLProp
	public static int idItemObsidianBody = 17074;
	@MLProp
	public static int idItemObsidianHelm = 17075;
	@MLProp
	public static int idItemObsidianLegs = 17076;
	@MLProp
	public static int idItemObsidianBoots = 17077;
	@MLProp
	public static int idItemNeptuneGlove = 17078;
	@MLProp
	public static int idItemNeptuneHelmet = 17079;
	@MLProp
	public static int idItemNeptuneChestplate = 17080;
	@MLProp
	public static int idItemNeptuneLeggings = 17081;
	@MLProp
	public static int idItemNeptuneBoots = 17082;
	@MLProp
	public static int idItemRegenerationStone = 17083;
	@MLProp
	public static int idItemInvisibilityCloak = 17084;
	@MLProp
	public static int idItemAgilityCape = 17085;
	@MLProp
	public static int idItemWhiteCape = 17086;
	@MLProp
	public static int idItemRedCape = 17087;
	@MLProp
	public static int idItemYellowCape = 17088;
	@MLProp
	public static int idItemBlueCape = 17089;
	@MLProp
	public static int idItemPickValkyrie = 17090;
	@MLProp
	public static int idItemAxeValkyrie = 17091;
	@MLProp
	public static int idItemShovelValkyrie = 17092;
	@MLProp
	public static int idItemHealingStone = 17093;
	@MLProp
	public static int idItemIceRing = 17094;
	@MLProp
	public static int idItemIcePendant = 17095;
	public static boolean hasLoreOverworld = false;
	public static boolean hasLoreNether = false;
	public static boolean hasLoreAether = false;
	public static IAetherBoss currentBoss = null;
	private KeyBinding key_loreGain = new KeyBinding("key.loreGain", 48);

	public mod_Aether() {
		dim = new DimensionAether();
		dim.name = "Aether";
		BiomeGenAether aether = new BiomeGenAether();

		try {
			ModLoader.setPrivateValue(BiomeGenBase.class, aether, "w", false);
		} catch (Exception exception5) {
			System.out.println("Forgot to update reflection. Trying MCP name for disabling rain.");

			try {
				ModLoader.setPrivateValue(BiomeGenBase.class, aether, "enableRain", false);
			} catch (Exception exception4) {
			}
		}

		new AetherBlocks();
		new AetherItems();
		new AetherMobs();
		new AetherPoison();
		new AetherAchievements();
		new AetherRecipes();
		ModLoader.RegisterKey(this, this.key_loreGain, false);
		ModLoader.AddLocalization("key.loreGain", "Gain Lore");
		ModLoader.SetInGameHook(this, true, false);
		SAPI.interceptAdd((IInterceptBlockSet)this);
		PlayerAPI.RegisterPlayerBase(PlayerBaseAether.class);
	}

	public void KeyboardEvent(KeyBinding event) {
		Minecraft mc = ModLoader.getMinecraftInstance();
		if(event == this.key_loreGain) {
			EntityPlayerSP entityplayer = ModLoader.getMinecraftInstance().thePlayer;
			if(getCurrentDimension() == 3) {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(AetherItems.LoreBook, 1, 2));
			} else if(getCurrentDimension() == 0) {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(AetherItems.LoreBook, 1, 0));
			} else if(getCurrentDimension() == -1) {
				entityplayer.inventory.addItemStackToInventory(new ItemStack(AetherItems.LoreBook, 1, 1));
			}
		}

	}

	public boolean OnTickInGame(Minecraft game) {
		if(!game.theWorld.multiplayerWorld) {
			if(game.theWorld != nbtWorld) {
				if(nbtWorld != null) {
					AetherNBT.save(nbtWorld);
				}

				if(game.theWorld != null) {
					AetherNBT.load(game.theWorld);
				}

				nbtWorld = game.theWorld;
			}

			if(nbtWorld != null && nbtWorld.worldInfo.getWorldTime() % (long)nbtWorld.autosavePeriod == 0L) {
				AetherNBT.save(nbtWorld);
			}
		}

		if(!(game.currentScreen instanceof GuiMainMenu)) {
			GuiMainMenu.mmactive = false;
		}

		if(game.thePlayer != null) {
			EntityPlayerSP time = game.thePlayer;
			if(time.dimension == dim.number && time.posY < -2.0D && !GuiMainMenu.mmactive) {
				Class enteredAether = null;
				NBTTagCompound tag = new NBTTagCompound();
				if(time.ridingEntity != null) {
					enteredAether = time.ridingEntity.getClass();
					time.ridingEntity.writeToNBT(tag);
					time.ridingEntity.setEntityDead();
				}

				double motionY = time.motionY;
				this.cloudPara = false;
				if(EntityCloudParachute.getCloudBelongingToEntity(time) != null) {
					this.cloudPara = true;
				}

				DimensionBase.usePortal(((BlockAetherPortal)AetherBlocks.Portal).getDimNumber());
				game.thePlayer.setLocationAndAngles(time.posX, 127.0D, time.posZ, time.rotationYaw, 0.0F);
				if(enteredAether != null && !game.theWorld.multiplayerWorld) {
					Entity i = null;

					try {
						i = (Entity)((Entity)enteredAether.getDeclaredConstructor(new Class[]{World.class}).newInstance(new Object[]{game.theWorld}));
						i.readFromNBT(tag);
						i.setLocationAndAngles(time.posX, 127.0D, time.posZ, time.rotationYaw, 0.0F);
						game.theWorld.entityJoinedWorld(i);
						time.mountEntity(i);
					} catch (Exception exception9) {
						System.out.println("Failed to transfer mount.");
					}
				}

				time.motionX = time.motionZ = 0.0D;
				time.motionY = motionY;
				int i12;
				if(this.cloudPara && EntityCloudParachute.entityHasRoomForCloud(game.theWorld, time)) {
					for(i12 = 0; i12 < 32; ++i12) {
						EntityCloudParachute.doCloudSmoke(game.theWorld, time);
					}

					game.theWorld.playSoundAtEntity(time, "cloud", 1.0F, 1.0F / (game.theWorld.rand.nextFloat() * 0.1F + 0.95F));
					if(!game.theWorld.multiplayerWorld) {
						game.theWorld.entityJoinedWorld(new EntityCloudParachute(game.theWorld, time, false));
					}
				}

				if(game.gameSettings.difficulty == 0) {
					time.fallDistance = -64.0F;
				}

				if(!this.cloudPara) {
					if(!time.inventory.consumeInventoryItem(AetherItems.CloudParachute.shiftedIndex)) {
						for(i12 = 0; i12 < time.inventory.getSizeInventory(); ++i12) {
							ItemStack itemstack = time.inventory.getStackInSlot(i12);
							if(itemstack != null && itemstack.itemID == AetherItems.CloudParachuteGold.shiftedIndex && EntityCloudParachute.entityHasRoomForCloud(game.theWorld, time)) {
								EntityCloudParachute.doCloudSmoke(game.theWorld, time);
								game.theWorld.playSoundAtEntity(time, "cloud", 1.0F, 1.0F / (game.theWorld.rand.nextFloat() * 0.1F + 0.95F));
								if(!game.theWorld.multiplayerWorld) {
									game.theWorld.entityJoinedWorld(new EntityCloudParachute(game.theWorld, time, true));
								}

								itemstack.damageItem(1, time);
								time.inventory.setInventorySlotContents(i12, itemstack);
							}
						}
					} else if(EntityCloudParachute.entityHasRoomForCloud(game.theWorld, time)) {
						for(i12 = 0; i12 < 32; ++i12) {
							EntityCloudParachute.doCloudSmoke(game.theWorld, time);
						}

						game.theWorld.playSoundAtEntity(time, "cloud", 1.0F, 1.0F / (game.theWorld.rand.nextFloat() * 0.1F + 0.95F));
						if(!game.theWorld.multiplayerWorld) {
							game.theWorld.entityJoinedWorld(new EntityCloudParachute(game.theWorld, time, false));
						}
					}
				}
			}

			if(getCurrentDimension() == 3) {
				boolean z11 = ModLoader.getMinecraftInstance().statFileWriter.hasAchievementUnlocked(AetherAchievements.enterAether);
				if(!z11) {
					giveAchievement(AetherAchievements.enterAether, time);
					time.inventory.addItemStackToInventory(new ItemStack(AetherItems.LoreBook, 1, 2));
					time.inventory.addItemStackToInventory(new ItemStack(AetherItems.CloudParachute, 1));
					game.theWorld.playSoundAtEntity(time, "random.pop", 0.2F, 1.0F);
				}
			}
		}

		if(world != game.theWorld || game.thePlayer != null && (game.thePlayer.isDead || game.thePlayer.health <= 0)) {
			world = game.theWorld;
			return true;
		} else if(world == null) {
			return true;
		} else {
			if(!world.multiplayerWorld && ModLoader.isGUIOpen((Class)null)) {
				this.renderHearts();
			}

			this.renderJumps();
			if(game.currentScreen instanceof GuiInventory) {
				game.displayGuiScreen(new GuiInventoryMoreSlots(game.thePlayer));
			}

			this.repShieldUpdate(game);
			AetherPoison.tickRender(game);
			this.renderBossHP();
			long j10 = game.theWorld.getWorldTime();
			if(clock != j10) {
				AetherItems.tick(game);
				++this.updateCounter;
				clock = j10;
			}

			return true;
		}
	}

	public static void giveAchievement(Achievement a) {
		giveAchievement(a, ModLoader.getMinecraftInstance().thePlayer);
	}

	public static void giveAchievement(Achievement a, EntityPlayer p) {
		if(!ModLoader.getMinecraftInstance().statFileWriter.hasAchievementUnlocked(a)) {
			ModLoader.getMinecraftInstance().sndManager.playSoundFX("aether.sound.other.achievement.achievementGen", 1.0F, 1.0F);
			p.triggerAchievement(a);
		}
	}

	public void repShieldUpdate(Minecraft game) {
		World world = game.theWorld;
		if(world != null && !world.multiplayerWorld) {
			List list1 = world.playerEntities;
			if(list1 != null && list1.size() > 0) {
				for(int i = 0; i < list1.size(); ++i) {
					EntityPlayer player = (EntityPlayer)list1.get(i);
					boolean flag = false;
					InventoryPlayer inv = player.inventory;
					ItemStack shieldItem = null;
					if(inv.armorInventory.length > 4) {
						flag = inv != null && inv.armorInventory[6] != null && inv.armorInventory[6].itemID == AetherItems.RepShield.shiftedIndex;
						shieldItem = inv.armorInventory[6];
					}

					if(flag && (player.onGround || player.ridingEntity != null && player.ridingEntity.onGround) && player.moveForward == 0.0F && player.moveStrafing == 0.0F) {
						if(!game.gameSettings.thirdPersonView && player == game.thePlayer) {
							this.renderShieldEffect(game);
						}

						List list2 = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(4.0D, 4.0D, 4.0D));

						for(int j = 0; j < list2.size() && shieldItem != null && shieldItem.getItemDamage() < 500; ++j) {
							Entity entity = (Entity)list2.get(j);
							boolean flag2 = false;
							EntityLiving dick;
							double a;
							double b;
							double c;
							double d;
							int k;
							double d1;
							double e1;
							double f1;
							if(!(entity instanceof EntityProjectileBase) || entity.getDistanceToEntity(player) >= 2.5F || entity.prevPosX == entity.posX && entity.prevPosY == entity.posY && entity.prevPosZ == entity.posZ) {
								if(entity instanceof EntityArrow && entity.getDistanceToEntity(player) < 2.5F && (entity.prevPosX != entity.posX || entity.prevPosY != entity.posY || entity.prevPosZ != entity.posZ)) {
									EntityArrow entityArrow30 = (EntityArrow)entity;
									if(entityArrow30.owner == null || entityArrow30.owner != player) {
										dick = entityArrow30.owner;
										entityArrow30.owner = player;
										flag2 = true;
										if(dick != null) {
											a = entityArrow30.posX - dick.posX;
											b = entityArrow30.boundingBox.minY - dick.boundingBox.minY;
											c = entityArrow30.posZ - dick.posZ;
										} else {
											a = player.posX - entityArrow30.posX;
											b = player.posY - entityArrow30.posY;
											c = player.posZ - entityArrow30.posZ;
										}

										d = Math.sqrt(a * a + b * b + c * c);
										a /= -d;
										b /= -d;
										c /= -d;
										entityArrow30.motionX = a * 0.75D;
										entityArrow30.motionY = b * 0.75D + 0.15D;
										entityArrow30.motionZ = c * 0.75D;
										entityArrow30.setArrowHeading(entityArrow30.motionX, entityArrow30.motionY, entityArrow30.motionZ, 0.8F, 0.5F);
										world.playSoundAtEntity(entityArrow30, "note.snare", 1.0F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.4F + 0.8F) * 1.1F);

										for(k = 0; k < 12; ++k) {
											d1 = -entityArrow30.motionX * (double)0.15F + (double)((entityArrow30.rand.nextFloat() - 0.5F) * 0.05F);
											e1 = -entityArrow30.motionY * (double)0.15F + (double)((entityArrow30.rand.nextFloat() - 0.5F) * 0.05F);
											f1 = -entityArrow30.motionZ * (double)0.15F + (double)((entityArrow30.rand.nextFloat() - 0.5F) * 0.05F);
											d1 *= 0.625D;
											e1 *= 0.625D;
											f1 *= 0.625D;
											world.spawnParticle("flame", entityArrow30.posX, entityArrow30.posY, entityArrow30.posZ, d1, e1, f1);
										}
									}
								}
							} else {
								EntityProjectileBase proj = (EntityProjectileBase)entity;
								if(proj.shooter == null || proj.shooter != player) {
									dick = proj.shooter;
									proj.shooter = player;
									flag2 = true;
									if(dick != null) {
										a = proj.posX - dick.posX;
										b = proj.boundingBox.minY - dick.boundingBox.minY;
										c = proj.posZ - dick.posZ;
									} else {
										a = player.posX - proj.posX;
										b = player.posY - proj.posY;
										c = player.posZ - proj.posZ;
									}

									d = Math.sqrt(a * a + b * b + c * c);
									a /= -d;
									b /= -d;
									c /= -d;
									proj.motionX = a * 0.75D;
									proj.motionY = b * 0.75D + 0.05D;
									proj.motionZ = c * 0.75D;
									proj.setArrowHeading(proj.motionX, proj.motionY, proj.motionZ, 0.8F, 0.5F);
									world.playSoundAtEntity(proj, "note.snare", 1.0F, ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.4F + 0.8F) * 1.1F);

									for(k = 0; k < 12; ++k) {
										d1 = -proj.motionX * (double)0.15F + (double)((proj.rand.nextFloat() - 0.5F) * 0.05F);
										e1 = -proj.motionY * (double)0.15F + (double)((proj.rand.nextFloat() - 0.5F) * 0.05F);
										f1 = -proj.motionZ * (double)0.15F + (double)((proj.rand.nextFloat() - 0.5F) * 0.05F);
										d1 *= 0.625D;
										e1 *= 0.625D;
										f1 *= 0.625D;
										world.spawnParticle("flame", proj.posX, proj.posY, proj.posZ, d1, e1, f1);
									}
								}
							}

							if(flag2 && shieldItem != null) {
								shieldItem.damageItem(1, (Entity)null);
								if(shieldItem.getItemDamage() >= 500) {
									player.inventory.armorInventory[6] = null;
								}
							}
						}
					}
				}
			}
		}

	}

	private void renderShieldEffect(Minecraft game) {
		ScaledResolution scaledresolution = new ScaledResolution(game.gameSettings, game.displayWidth, game.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, game.renderEngine.getTexture("/aether/other/shieldEffect.png"));
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)j, -90.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, (double)j, -90.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double)i, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_NORMALIZE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	private String randomText() {
		int i = this.rand.nextInt(19);
		switch(i) {
		case 0:
			return "Taming Moas";
		case 1:
			return "Hiding Chests";
		case 2:
			return "Defying Gravity";
		case 3:
			return "Enchanting Darts";
		case 4:
			return "Sssssssss.....";
		case 5:
			return "Growing Skyroot";
		case 6:
			return "Writing Lore";
		case 7:
			return "Puffing Sheepuffs";
		case 8:
			return "Making Portals";
		case 9:
			return "Locking Chests";
		case 10:
			return "Making Pigs Fly";
		case 11:
			return "Growing Grass";
		case 12:
			return "Freezing Icestone";
		case 13:
			return "Building Temples";
		case 14:
			return "Planting Golden Oaks";
		case 15:
			return "Angering Zephyrs";
		case 16:
			return "Mimicing Chests";
		case 17:
			return "Poisoning Plants";
		case 18:
			return "Writing Music";
		default:
			return "Loading Aether Mod";
		}
	}

	private void renderHearts() {
		Minecraft mc = ModLoader.getMinecraftInstance();
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int width = scaledresolution.getScaledWidth();
		int height = scaledresolution.getScaledHeight();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
		GL11.glColor3f(1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_BLEND);
		boolean flag1 = mc.thePlayer.heartsLife / 3 % 2 == 1;
		if(mc.thePlayer.heartsLife < 10) {
			flag1 = false;
		}

		int halfHearts = mc.thePlayer.health - 20;
		int prevHalfHearts = mc.thePlayer.prevHealth - 20;
		this.rand.setSeed((long)(this.updateCounter * 312871));
		if(mc.playerController.shouldDrawHUD()) {
			for(int heart = 0; heart < getPlayer(mc.thePlayer).maxHealth / 2 - 10; ++heart) {
				int yPos = height - 44;
				if(mc.thePlayer.isInsideOfMaterial(Material.water)) {
					yPos -= 9;
				}

				byte k5 = 0;
				if(flag1) {
					k5 = 1;
				}

				int xPos = width / 2 - 91 + heart * 8;
				if(mc.thePlayer.health <= 4) {
					yPos += this.rand.nextInt(2);
				}

				this.drawTexturedModalRect(xPos, yPos, 16 + k5 * 9, 0, 9, 9);
				if(flag1) {
					if(heart * 2 + 1 < prevHalfHearts) {
						this.drawTexturedModalRect(xPos, yPos, 70, 0, 9, 9);
					}

					if(heart * 2 + 1 == prevHalfHearts) {
						this.drawTexturedModalRect(xPos, yPos, 79, 0, 9, 9);
					}
				}

				if(heart * 2 + 1 < halfHearts) {
					this.drawTexturedModalRect(xPos, yPos, 52, 0, 9, 9);
				}

				if(heart * 2 + 1 == halfHearts) {
					this.drawTexturedModalRect(xPos, yPos, 61, 0, 9, 9);
				}
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderJumps() {
		Minecraft mc = ModLoader.getMinecraftInstance();
		if(mc.thePlayer.ridingEntity instanceof EntityMoa) {
			if(mc.playerController.shouldDrawHUD() && mc.inGameHasFocus) {
				ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
				EntityMoa moa = (EntityMoa)mc.thePlayer.ridingEntity;
				int width = scaledresolution.getScaledWidth();
				int height = scaledresolution.getScaledHeight();
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/aether/gui/jumps.png"));
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
				GL11.glColor3f(1.0F, 1.0F, 1.0F);
				GL11.glDisable(GL11.GL_BLEND);

				for(int jump = 0; jump < moa.colour.jumps; ++jump) {
					int yPos = height - 44;
					int xPos = width / 2 + 1 + 9 * (jump + 1);
					if(jump < moa.jrem) {
						this.drawTexturedModalRect(xPos, yPos, 0, 0, 9, 11);
					} else {
						this.drawTexturedModalRect(xPos, yPos, 10, 0, 9, 11);
					}
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	private void renderBossHP() {
		if(currentBoss != null) {
			Minecraft mc = ModLoader.getMinecraftInstance();
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int width = scaledresolution.getScaledWidth();
			int height = scaledresolution.getScaledHeight();
			String s = currentBoss.getBossTitle();
			mc.fontRenderer.drawStringWithShadow(s, width / 2 - mc.fontRenderer.getStringWidth(s) / 2, 2, -1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/aether/gui/bossHPBar.png"));
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
			GL11.glColor3f(1.0F, 1.0F, 1.0F);
			GL11.glDisable(GL11.GL_BLEND);
			this.drawTexturedModalRect(width / 2 - 128, 12, 0, 16, 256, 32);
			int w = (int)((float)currentBoss.getBossHP() / (float)currentBoss.getBossMaxHP() * 256.0F);
			this.drawTexturedModalRect(width / 2 - 128, 12, 0, 0, w, 16);
		}

	}

	public void drawTexturedModalRect(int i, int j, int k, int l, int i1, int j1) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + j1), (double)this.zLevel, (double)((float)(k + 0) * f), (double)((float)(l + j1) * f1));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + j1), (double)this.zLevel, (double)((float)(k + i1) * f), (double)((float)(l + j1) * f1));
		tessellator.addVertexWithUV((double)(i + i1), (double)(j + 0), (double)this.zLevel, (double)((float)(k + i1) * f), (double)((float)(l + 0) * f1));
		tessellator.addVertexWithUV((double)(i + 0), (double)(j + 0), (double)this.zLevel, (double)((float)(k + 0) * f), (double)((float)(l + 0) * f1));
		tessellator.draw();
	}

	public void AddRenderer(Map map) {
		AetherBlocks.AddRenderer(map);
		AetherItems.AddRenderer(map);
		AetherMobs.AddRenderer(map);
		AetherPoison.AddRenderer(map);
		map.put(EntityPlayer.class, new RenderPlayerAether());
	}

	public void TakenFromCrafting(EntityPlayer player, ItemStack item) {
		AetherItems.takenCrafting(player, item);
	}

	public static boolean equippedSkyrootSword() {
		ItemStack itemstack = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();
		return itemstack != null && itemstack.itemID == AetherItems.SwordSkyroot.shiftedIndex;
	}

	public static boolean equippedSkyrootPick() {
		ItemStack itemstack = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();
		return itemstack != null && itemstack.itemID == AetherItems.PickSkyroot.shiftedIndex;
	}

	public static boolean equippedSkyrootShovel() {
		ItemStack itemstack = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();
		return itemstack != null && itemstack.itemID == AetherItems.ShovelSkyroot.shiftedIndex;
	}

	public static boolean equippedSkyrootAxe() {
		ItemStack itemstack = ModLoader.getMinecraftInstance().thePlayer.inventory.getCurrentItem();
		return itemstack != null && itemstack.itemID == AetherItems.AxeSkyroot.shiftedIndex;
	}

	public static int getCurrentDimension() {
		EntityPlayerSP player = ModLoader.getMinecraftInstance().thePlayer;
		return player == null ? 0 : player.dimension;
	}

	public String Version() {
		return "r1";
	}

	public boolean canIntercept(World world, Loc loc, int blockID) {
		if(getCurrentDimension() == 3) {
			if(blockID == Block.torchWood.blockID) {
				return true;
			}

			if(blockID == Block.fire.blockID) {
				return true;
			}

			if(blockID == Block.netherrack.blockID) {
				return true;
			}

			if(blockID == Block.slowSand.blockID) {
				return true;
			}

			if(blockID == Block.lavaMoving.blockID || blockID == Block.lavaStill.blockID) {
				return true;
			}

			if(blockID == Block.portal.blockID) {
				return true;
			}

			if(blockID == Block.blockBed.blockID) {
				return true;
			}
		} else if(getCurrentDimension() < 0) {
			if(blockID == AetherBlocks.Portal.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Dirt.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Grass.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Aercloud.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.AmbrosiumOre.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.AmbrosiumTorch.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.DungeonStone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.EnchantedGravitite.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.GoldenOakLeaves.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.GoldenOakSapling.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.GravititeOre.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Holystone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Icestone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.LightDungeonStone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.LockedDungeonStone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.LockedLightDungeonStone.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.ChestMimic.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Pillar.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Quicksoil.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.SkyrootLeaves.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Plank.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.SkyrootSapling.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Log.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.Trap.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.TreasureChest.blockID) {
				return true;
			}

			if(blockID == AetherBlocks.ZaniteOre.blockID) {
				return true;
			}
		}

		if(blockID != Block.waterMoving.blockID && blockID != Block.waterStill.blockID) {
			return false;
		} else {
			return true;
		}
	}

	public int intercept(World world, Loc loc, int blockID) {
		if(blockID == Block.blockBed.blockID) {
			return AetherBlocks.Bed.blockID;
		} else {
			if(blockID == Block.waterMoving.blockID || blockID == Block.waterStill.blockID) {
				if(world.getBlockId(loc.x(), loc.y() - 1, loc.z()) == Block.glowStone.blockID && ((BlockAetherPortal)((BlockAetherPortal)AetherBlocks.Portal)).tryToCreatePortal(world, loc.x(), loc.y(), loc.z())) {
					return AetherBlocks.Portal.blockID;
				}

				if(getCurrentDimension() != -1) {
					return blockID;
				}
			}

			Random rand = new Random();

			for(int n = 0; n < 10; ++n) {
				world.spawnParticle("smoke", loc.x + 0.5D + rand.nextGaussian() * 0.1D, loc.y + 0.5D + rand.nextGaussian() * 0.1D, loc.z + 0.5D + rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.01D, rand.nextGaussian() * 0.1D);
			}

			return blockID != Block.lavaMoving.blockID && blockID != Block.lavaStill.blockID ? (getCurrentDimension() >= 0 || blockID != Block.waterMoving.blockID && blockID != Block.waterStill.blockID ? 0 : Block.cobblestone.blockID) : AetherBlocks.Aerogel.blockID;
		}
	}

	public static PlayerBaseAether getPlayer() {
		return getPlayer(ModLoader.getMinecraftInstance().thePlayer);
	}

	public static PlayerBaseAether getPlayer(EntityPlayer player) {
		return (PlayerBaseAether)PlayerAPI.getPlayerBase((EntityPlayerSP)player, PlayerBaseAether.class);
	}
}
