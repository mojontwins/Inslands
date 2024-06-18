package net.minecraft.src;

import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;

public class ConsoleCommandHandler {
	private static Logger minecraftLogger = Logger.getLogger("Minecraft");
	private MinecraftServer minecraftServer;

	public ConsoleCommandHandler(MinecraftServer minecraftServer1) {
		this.minecraftServer = minecraftServer1;
	}

	public synchronized void handleCommand(ServerCommand serverCommand1) {
		String string2 = serverCommand1.command;
		String[] string3 = string2.split(" ");
		String string4 = string3[0];
		String string5 = string2.substring(string4.length()).trim();
		ICommandListener iCommandListener6 = serverCommand1.commandListener;
		String string7 = iCommandListener6.getUsername();
		ServerConfigurationManager serverConfigurationManager8 = this.minecraftServer.configManager;
		if(!string4.equalsIgnoreCase("help") && !string4.equalsIgnoreCase("?")) {
			if(string4.equalsIgnoreCase("list")) {
				iCommandListener6.log("Connected players: " + serverConfigurationManager8.getPlayerList());
			} else if(string4.equalsIgnoreCase("stop")) {
				this.sendNoticeToOps(string7, "Stopping the server..");
				this.minecraftServer.initiateShutdown();
			} else {
				int i9;
				WorldServer worldServer10;
				if(string4.equalsIgnoreCase("save-all")) {
					this.sendNoticeToOps(string7, "Forcing save..");
					if(serverConfigurationManager8 != null) {
						serverConfigurationManager8.savePlayerStates();
					}

					for(i9 = 0; i9 < this.minecraftServer.worldMngr.length; ++i9) {
						worldServer10 = this.minecraftServer.worldMngr[i9];
						boolean z11 = worldServer10.levelSaving;
						worldServer10.levelSaving = false;
						worldServer10.saveWorld(true, (IProgressUpdate)null);
						worldServer10.levelSaving = z11;
					}

					this.sendNoticeToOps(string7, "Save complete.");
				} else if(string4.equalsIgnoreCase("save-off")) {
					this.sendNoticeToOps(string7, "Disabling level saving..");

					for(i9 = 0; i9 < this.minecraftServer.worldMngr.length; ++i9) {
						worldServer10 = this.minecraftServer.worldMngr[i9];
						worldServer10.levelSaving = true;
					}
				} else if(string4.equalsIgnoreCase("save-on")) {
					this.sendNoticeToOps(string7, "Enabling level saving..");

					for(i9 = 0; i9 < this.minecraftServer.worldMngr.length; ++i9) {
						worldServer10 = this.minecraftServer.worldMngr[i9];
						worldServer10.levelSaving = false;
					}
				} else if(string4.equalsIgnoreCase("op")) {
					serverConfigurationManager8.addOp(string5);
					this.sendNoticeToOps(string7, "Opping " + string5);
					serverConfigurationManager8.sendChatMessageToPlayer(string5, "\u00a7eYou are now op!");
				} else if(string4.equalsIgnoreCase("deop")) {
					serverConfigurationManager8.removeOp(string5);
					serverConfigurationManager8.sendChatMessageToPlayer(string5, "\u00a7eYou are no longer op!");
					this.sendNoticeToOps(string7, "De-opping " + string5);
				} else if(string4.equalsIgnoreCase("ban-ip")) {
					serverConfigurationManager8.banIP(string5);
					this.sendNoticeToOps(string7, "Banning ip " + string5);
				} else if(string4.equalsIgnoreCase("pardon-ip")) {
					serverConfigurationManager8.pardonIP(string5);
					this.sendNoticeToOps(string7, "Pardoning ip " + string5);
				} else {
					EntityPlayerMP entityPlayerMP18;
					if(string4.equalsIgnoreCase("ban")) {
						serverConfigurationManager8.banPlayer(string5);
						this.sendNoticeToOps(string7, "Banning " + string5);
						entityPlayerMP18 = serverConfigurationManager8.getPlayerEntity(string5);
						if(entityPlayerMP18 != null) {
							entityPlayerMP18.playerNetServerHandler.kickPlayer("Banned by admin");
						}
					} else if(string4.equalsIgnoreCase("pardon")) {
						serverConfigurationManager8.pardonPlayer(string5);
						this.sendNoticeToOps(string7, "Pardoning " + string5);
					} else {
						String string19;
						int i20;
						if(string4.equalsIgnoreCase("kick")) {
							string19 = string5;
							entityPlayerMP18 = null;

							for(i20 = 0; i20 < serverConfigurationManager8.playerEntities.size(); ++i20) {
								EntityPlayerMP entityPlayerMP12 = (EntityPlayerMP)serverConfigurationManager8.playerEntities.get(i20);
								if(entityPlayerMP12.username.equalsIgnoreCase(string19)) {
									entityPlayerMP18 = entityPlayerMP12;
								}
							}

							if(entityPlayerMP18 != null) {
								entityPlayerMP18.playerNetServerHandler.kickPlayer("Kicked by admin");
								this.sendNoticeToOps(string7, "Kicking " + entityPlayerMP18.username);
							} else {
								iCommandListener6.log("Can\'t find user " + string19 + ". No kick.");
							}
						} else if(string4.equalsIgnoreCase("tp")) {
							if(string3.length == 3) {
								EntityPlayerMP entityPlayerMP21 = serverConfigurationManager8.getPlayerEntity(string3[1]);
								entityPlayerMP18 = serverConfigurationManager8.getPlayerEntity(string3[2]);
								if(entityPlayerMP21 == null) {
									iCommandListener6.log("Can\'t find user " + string3[1] + ". No tp.");
								} else if(entityPlayerMP18 == null) {
									iCommandListener6.log("Can\'t find user " + string3[2] + ". No tp.");
								} else if(entityPlayerMP21.dimension != entityPlayerMP18.dimension) {
									iCommandListener6.log("User " + string3[1] + " and " + string3[2] + " are in different dimensions. No tp.");
								} else {
									entityPlayerMP21.playerNetServerHandler.teleportTo(entityPlayerMP18.posX, entityPlayerMP18.posY, entityPlayerMP18.posZ, entityPlayerMP18.rotationYaw, entityPlayerMP18.rotationPitch);
									this.sendNoticeToOps(string7, "Teleporting " + string3[1] + " to " + string3[2] + ".");
								}
							} else {
								iCommandListener6.log("Syntax error, please provide a source and a target.");
							}
						} else if(string4.equalsIgnoreCase("give")) {
							if(string3.length != 3 && string3.length != 4 && string3.length != 5) {
								return;
							}

							string19 = string3[1];
							entityPlayerMP18 = serverConfigurationManager8.getPlayerEntity(string19);
							if(entityPlayerMP18 != null) {
								try {
									i20 = Integer.parseInt(string3[2]);
									if(Item.itemsList[i20] != null) {
										this.sendNoticeToOps(string7, "Giving " + entityPlayerMP18.username + " some " + i20);
										int i22 = 1;
										int i13 = 0;
										if(string3.length > 3) {
											i22 = this.tryParse(string3[3], 1);
										}

										if(string3.length > 4) {
											i13 = this.tryParse(string3[4], 1);
										}

										if(i22 < 1) {
											i22 = 1;
										}

										if(i22 > 64) {
											i22 = 64;
										}

										entityPlayerMP18.dropPlayerItem(new ItemStack(i20, i22, i13));
									} else {
										iCommandListener6.log("There\'s no item with id " + i20);
									}
								} catch (NumberFormatException numberFormatException16) {
									iCommandListener6.log("There\'s no item with id " + string3[2]);
								}
							} else {
								iCommandListener6.log("Can\'t find user " + string19);
							}
						} else if(string4.equalsIgnoreCase("xp")) {
							if(string3.length != 3) {
								return;
							}

							string19 = string3[1];
							entityPlayerMP18 = serverConfigurationManager8.getPlayerEntity(string19);
							if(entityPlayerMP18 != null) {
								try {
									i20 = Integer.parseInt(string3[2]);
									i20 = i20 > 5000 ? 5000 : i20;
									this.sendNoticeToOps(string7, "Giving " + i20 + " orbs to " + entityPlayerMP18.username);
									entityPlayerMP18.addExperience(i20);
								} catch (NumberFormatException numberFormatException15) {
									iCommandListener6.log("Invalid orb count: " + string3[2]);
								}
							} else {
								iCommandListener6.log("Can\'t find user " + string19);
							}
						} else if(string4.equalsIgnoreCase("gamemode")) {
							if(string3.length != 3) {
								return;
							}

							string19 = string3[1];
							entityPlayerMP18 = serverConfigurationManager8.getPlayerEntity(string19);
							if(entityPlayerMP18 != null) {
								try {
									i20 = Integer.parseInt(string3[2]);
									i20 = WorldSettings.validGameType(i20);
									if(entityPlayerMP18.itemInWorldManager.getGameType() != i20) {
										this.sendNoticeToOps(string7, "Setting " + entityPlayerMP18.username + " to game mode " + i20);
										entityPlayerMP18.itemInWorldManager.toggleGameType(i20);
										entityPlayerMP18.playerNetServerHandler.sendPacket(new Packet70Bed(3, i20));
									} else {
										this.sendNoticeToOps(string7, entityPlayerMP18.username + " already has game mode " + i20);
									}
								} catch (NumberFormatException numberFormatException14) {
									iCommandListener6.log("There\'s no game mode with id " + string3[2]);
								}
							} else {
								iCommandListener6.log("Can\'t find user " + string19);
							}
						} else if(string4.equalsIgnoreCase("time")) {
							if(string3.length != 3) {
								return;
							}

							string19 = string3[1];

							try {
								int i23 = Integer.parseInt(string3[2]);
								WorldServer worldServer24;
								if("add".equalsIgnoreCase(string19)) {
									for(i20 = 0; i20 < this.minecraftServer.worldMngr.length; ++i20) {
										worldServer24 = this.minecraftServer.worldMngr[i20];
										worldServer24.advanceTime(worldServer24.getWorldTime() + (long)i23);
									}

									this.sendNoticeToOps(string7, "Added " + i23 + " to time");
								} else if("set".equalsIgnoreCase(string19)) {
									for(i20 = 0; i20 < this.minecraftServer.worldMngr.length; ++i20) {
										worldServer24 = this.minecraftServer.worldMngr[i20];
										worldServer24.advanceTime((long)i23);
									}

									this.sendNoticeToOps(string7, "Set time to " + i23);
								} else {
									iCommandListener6.log("Unknown method, use either \"add\" or \"set\"");
								}
							} catch (NumberFormatException numberFormatException17) {
								iCommandListener6.log("Unable to convert time value, " + string3[2]);
							}
						} else if(string4.equalsIgnoreCase("say") && string5.length() > 0) {
							minecraftLogger.info("[" + string7 + "] " + string5);
							serverConfigurationManager8.sendPacketToAllPlayers(new Packet3Chat("\u00a7d[Server] " + string5));
						} else if(string4.equalsIgnoreCase("tell")) {
							if(string3.length >= 3) {
								string2 = string2.substring(string2.indexOf(" ")).trim();
								string2 = string2.substring(string2.indexOf(" ")).trim();
								minecraftLogger.info("[" + string7 + "->" + string3[1] + "] " + string2);
								string2 = "\u00a77" + string7 + " whispers " + string2;
								minecraftLogger.info(string2);
								if(!serverConfigurationManager8.sendPacketToPlayer(string3[1], new Packet3Chat(string2))) {
									iCommandListener6.log("There\'s no player by that name online.");
								}
							}
						} else if(string4.equalsIgnoreCase("whitelist")) {
							this.handleWhitelist(string7, string2, iCommandListener6);
						} else if(string4.equalsIgnoreCase("toggledownfall")) {
							this.minecraftServer.worldMngr[0].commandToggleDownfall();
							iCommandListener6.log("Toggling rain and snow, hold on...");
						} else if(string4.equalsIgnoreCase("banlist")) {
							if(string3.length == 2) {
								if(string3[1].equals("ips")) {
									iCommandListener6.log("IP Ban list:" + this.func_40648_a(this.minecraftServer.getBannedIPsList(), ", "));
								}
							} else {
								iCommandListener6.log("Ban list:" + this.func_40648_a(this.minecraftServer.getBannedPlayersList(), ", "));
							}
						} else {
							minecraftLogger.info("Unknown console command. Type \"help\" for help.");
						}
					}
				}
			}
		} else {
			this.printHelp(iCommandListener6);
		}

	}

	private void handleWhitelist(String string1, String string2, ICommandListener iCommandListener3) {
		String[] string4 = string2.split(" ");
		if(string4.length >= 2) {
			String string5 = string4[1].toLowerCase();
			if("on".equals(string5)) {
				this.sendNoticeToOps(string1, "Turned on white-listing");
				this.minecraftServer.propertyManagerObj.setProperty("white-list", true);
			} else if("off".equals(string5)) {
				this.sendNoticeToOps(string1, "Turned off white-listing");
				this.minecraftServer.propertyManagerObj.setProperty("white-list", false);
			} else if("list".equals(string5)) {
				Set set6 = this.minecraftServer.configManager.getWhiteListedIPs();
				String string7 = "";

				String string9;
				for(Iterator iterator8 = set6.iterator(); iterator8.hasNext(); string7 = string7 + string9 + " ") {
					string9 = (String)iterator8.next();
				}

				iCommandListener3.log("White-listed players: " + string7);
			} else {
				String string10;
				if("add".equals(string5) && string4.length == 3) {
					string10 = string4[2].toLowerCase();
					this.minecraftServer.configManager.addToWhiteList(string10);
					this.sendNoticeToOps(string1, "Added " + string10 + " to white-list");
				} else if("remove".equals(string5) && string4.length == 3) {
					string10 = string4[2].toLowerCase();
					this.minecraftServer.configManager.removeFromWhiteList(string10);
					this.sendNoticeToOps(string1, "Removed " + string10 + " from white-list");
				} else if("reload".equals(string5)) {
					this.minecraftServer.configManager.reloadWhiteList();
					this.sendNoticeToOps(string1, "Reloaded white-list from file");
				}
			}

		}
	}

	private void printHelp(ICommandListener iCommandListener1) {
		iCommandListener1.log("To run the server without a gui, start it like this:");
		iCommandListener1.log("   java -Xmx1024M -Xms1024M -jar minecraft_server.jar nogui");
		iCommandListener1.log("Console commands:");
		iCommandListener1.log("   help  or  ?               shows this message");
		iCommandListener1.log("   kick <player>             removes a player from the server");
		iCommandListener1.log("   ban <player>              bans a player from the server");
		iCommandListener1.log("   pardon <player>           pardons a banned player so that they can connect again");
		iCommandListener1.log("   ban-ip <ip>               bans an IP address from the server");
		iCommandListener1.log("   pardon-ip <ip>            pardons a banned IP address so that they can connect again");
		iCommandListener1.log("   op <player>               turns a player into an op");
		iCommandListener1.log("   deop <player>             removes op status from a player");
		iCommandListener1.log("   tp <player1> <player2>    moves one player to the same location as another player");
		iCommandListener1.log("   give <player> <id> [num]  gives a player a resource");
		iCommandListener1.log("   tell <player> <message>   sends a private message to a player");
		iCommandListener1.log("   stop                      gracefully stops the server");
		iCommandListener1.log("   save-all                  forces a server-wide level save");
		iCommandListener1.log("   save-off                  disables terrain saving (useful for backup scripts)");
		iCommandListener1.log("   save-on                   re-enables terrain saving");
		iCommandListener1.log("   list                      lists all currently connected players");
		iCommandListener1.log("   say <message>             broadcasts a message to all players");
		iCommandListener1.log("   time <add|set> <amount>   adds to or sets the world time (0-24000)");
		iCommandListener1.log("   gamemode <player> <mode>  sets player\'s game mode (0 or 1)");
		iCommandListener1.log("   toggledownfall            toggles rain on or off");
		iCommandListener1.log("   xp <player> <amount>      gives the player the amount of xp (0-5000)");
	}

	private void sendNoticeToOps(String string1, String string2) {
		String string3 = string1 + ": " + string2;
		this.minecraftServer.configManager.sendChatMessageToAllOps("\u00a77(" + string3 + ")");
		minecraftLogger.info(string3);
	}

	private int tryParse(String string1, int i2) {
		try {
			return Integer.parseInt(string1);
		} catch (NumberFormatException numberFormatException4) {
			return i2;
		}
	}

	private String func_40648_a(String[] string1, String string2) {
		int i3 = string1.length;
		if(0 == i3) {
			return "";
		} else {
			StringBuilder stringBuilder4 = new StringBuilder();
			stringBuilder4.append(string1[0]);

			for(int i5 = 1; i5 < i3; ++i5) {
				stringBuilder4.append(string2).append(string1[i5]);
			}

			return stringBuilder4.toString();
		}
	}
}
