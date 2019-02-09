//Made by RedLime :)

package redlime.fishing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Main extends JavaPlugin implements Listener {

	public static Main plugin;
	private Metrics metrics;
	boolean kbtoggle = true;
	boolean debug = false;
	boolean endkbtoggle = true;
	boolean worldGuard = false;
	String disabler = null;
	String disabler_h = null;

	public static boolean entitytypes(String target) {
		ArrayList<String> entities = new ArrayList<String>();
		for(EntityType type : EntityType.values()) {
			if(type.isAlive()) {
				entities.add(type.toString());
			}
		}
		
		return entities.contains(target);
	}

	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		saveDefaultConfig();
		// config
		getConfig().addDefault("DisableWorld", new ArrayList<String>());
		getConfig().addDefault("DisableEntityType", new ArrayList<String>());
		getConfig().addDefault("DisableUpdateNofitication", false);
		FileConfiguration config = getConfig();
		config.options().copyDefaults(true);
		saveConfig();

		metrics = new Metrics(this);
		getCommand("fishingknockback").setTabCompleter(new TabCompleter());
		if (getServer().getPluginManager().isPluginEnabled("WorldGuard")) {
			worldGuard = true;
		}
	}

	public Metrics getMetrics() {
		return this.metrics;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fishingknockback") && sender.hasPermission("fishingkb.admin")
				&& sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&e&l___________________________________________________"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"\n&e&l          FishingRod Knockback by RED_LIME"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eVersion : " + getServer()
						.getPluginManager().getPlugin("FishingRodKnockback").getDescription().getVersion()));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&a/fishingknockback on/off - &ftoggle knockback"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a/fishingknockback debug"));
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&a/fishingknockback hook - &ftoggle knockback"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&a/fishingknockback reload - &freload config file"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&a/fishingknockback config <entity/world> - \n      &fAdd a knockback to the <entity/world> to handle exceptions"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&e&l___________________________________________________"));
				return false;
			} else if (args.length > 0) {
				if (args[0].equalsIgnoreCase("on")) {
					kbtoggle = true;
					disabler = null;
					p.sendMessage(ChatColor.GREEN + "FishingRod Knockback is now enable.");
					return false;
				}

				if (args[0].equalsIgnoreCase("off")) {
					kbtoggle = false;
					disabler = p.getName();
					p.sendMessage(ChatColor.RED + "FishingRod Knockback is now disable.");
					return false;
				}

				if (args[0].equalsIgnoreCase("help")) {
					p.performCommand("fishingknockback");
					return false;
				}

				if (args[0].equalsIgnoreCase("debug")) {
					if (debug == true) {
						p.sendMessage(ChatColor.GREEN + "DebugMode disable.");
						debug = false;
					} else if (debug == false) {
						p.sendMessage(ChatColor.GREEN + "DebugMode enable.");
						p.sendMessage(ChatColor.GREEN + "worldguard_boolean : " + worldGuard);
						debug = true;
					}
					return false;
				}

				if (args[0].equalsIgnoreCase("hook")) {
					if (endkbtoggle == true) {
						p.sendMessage(ChatColor.GREEN + "Hooking Knockback is disable.");
						disabler_h = p.getName();
						endkbtoggle = false;
					} else if (endkbtoggle == false) {
						p.sendMessage(ChatColor.GREEN
								+ "Hooking Knockback is enable. \n(if you using Viaversion, ProtocolSupport and OldCombatMechanics. Knockback when pulling a fishing rod may not work properly)");
						endkbtoggle = true;

					}
					return false;
				}

				if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("fishingkb.admin")) {
					this.reloadConfig();
					this.getConfig().options().copyDefaults(true);
					this.saveConfig();
					sender.sendMessage(ChatColor.GREEN + "FishingRodKnockback was reloaded!");
					return false;
				}

				if (args[0].equalsIgnoreCase("config") && sender.hasPermission("fishingkb.admin")) {
					if (args.length == 1) {
						sender.sendMessage(ChatColor.RED
								+ "Invalid instruction format. (/fishingknockback config <entity/world>)");
						return false;
					}
					if (args[1].equalsIgnoreCase("world")) {
						if (args.length == 2) {
							sender.sendMessage(ChatColor.RED
									+ "Please put the name of the world. (/fishingknockback config world <Worldname>)");
						}
						if (args.length == 3) {
							if (getConfig().getList("DisableWorld").contains(args[2])) {
								List<String> dworld = getConfig().getStringList("DisableWorld");
								dworld.remove(args[2]);
								getConfig().set("DisableWorld", dworld);
								saveConfig();
								sender.sendMessage(
										ChatColor.GREEN + "Exception knockback world setting successed! (Deleted)");
								return false;
							}
							List<String> dworld = getConfig().getStringList("DisableWorld");
							if (dworld == null)
								dworld = new ArrayList<String>();
							dworld.add(args[2]);
							getConfig().set("DisableWorld", dworld);
							saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Exception knockback world setting successed!");
						}
					}
					if (args[1].equalsIgnoreCase("entity")) {
						if (args.length == 2) {
							sender.sendMessage(ChatColor.RED
									+ "Please put the type of the entity. (/fishingknockback config entity <TYPE>)");
						}
						if (args.length == 3) {
							if (entitytypes(args[2]) == false) {
								sender.sendMessage(
										ChatColor.RED + "Entities that are not originally knocked back can not be set");
								return false;
							}
							if (getConfig().getList("DisableEntityType").contains(args[2])) {
								List<String> dentity = getConfig().getStringList("DisableEntityType");
								dentity.remove(args[2]);
								getConfig().set("DisableEntityType", dentity);
								saveConfig();
								sender.sendMessage(ChatColor.GREEN + "Entity knockback setup succeeded! (Deleted)");
								return false;
							}
							List<String> dentity = getConfig().getStringList("DisableEntityType");
							if (dentity == null)
								dentity = new ArrayList<String>();
							dentity.add(args[2]);
							getConfig().set("DisableEntityType", dentity);
							saveConfig();
							sender.sendMessage(ChatColor.GREEN + "Entity knockback setup succeeded!");
						}
					}
					return false;
				} else {
					p.performCommand("fishingknockback");
					return false;
				}
			}
		}
		return false;
	}

	@EventHandler
    public void onRodLand(ProjectileHitEvent e) {
        if (e.getEntity() == null || e.getEntity().getShooter() == e.getHitEntity() || e.getHitEntity() == null || e == null) {
            return;
        } else {
            if (debug == true) {
                Player player = (Player) e.getEntity().getShooter();
                if (player.hasPermission("fishingkb.admin") == true) {
                    player.sendMessage(ChatColor.YELLOW + "Entity Type : " + e.getHitEntity().getType().toString());
                }
            }
            if (kbtoggle == false || e.getHitEntity() == null) {
                return;
            }
            if (e.getEntityType() != EntityType.FISHING_HOOK) {
                return;
            }
            if (getConfig().getList("DisableWorld").toString().contains(e.getEntity().getLocation().getWorld().getName().toString()) == true) {
                return;
            }
            if (e.getHitEntity().getType() != EntityType.PLAYER && getConfig().getList("DisableEntityType").contains(e.getHitEntity().getType().toString()) == true) {
                return;
            }

            if (entitytypes(e.getHitEntity().getType().toString()) == true) { //�뿏�떚�떚 泥섎━

                FishHook hook = (FishHook) e.getEntity();
                Player hookShooter = (Player) hook.getShooter();
                LivingEntity hitEntity = (LivingEntity) e.getHitEntity();

                double kx = hook.getLocation().getDirection().getX() / 2.5;
                double kz = hook.getLocation().getDirection().getZ() / 2.5;
                kx = kx - kx * 2;
                
                if (worldGuard == true) {
                	RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
                	for(RegionManager rm : container.getLoaded()) {
                        for (ProtectedRegion r : rm.getApplicableRegions(hitEntity.getLocation())) {
                            List<String> flag = new ArrayList<String>();
                            flag.add("StateFlag{name='pvp'}=DENY");
                            flag.add("StateFlag{name='invincible'}=ALLOW");
                            int i;
                            for (i = 0; i < 2; i = i + 1) {
                                if (r.getFlags().toString().contains(flag.get(i)) == true) {
                                    if (debug == true) {
                                        hookShooter.sendMessage("Detected WorldGuard flag!" + r.getFlags().toString());
                                    }
                                    hook.remove();
                                    return;
                                }
                            }
                        }
                	}
                }

                if (debug == true && hookShooter.hasPermission("fishingkb.admin")) {
                    hookShooter.sendMessage("Direction:" + kx + " " + kz);
                }

                if (hitEntity.getNoDamageTicks() >= 6.5) {
                    return;
                } else if (hitEntity.getNoDamageTicks() < 6.5 && hitEntity.getLocation().getWorld().getBlockAt(hitEntity.getLocation()).getType().toString() != "AIR") {
                    hitEntity.setNoDamageTicks(0);
                }

                hitEntity.damage(0.001, hookShooter);
                double upVel = 0.372;
                if (hitEntity.isOnGround() == false) {
                    upVel = 0;
                }

                hitEntity.setVelocity(new Vector(kx, upVel, kz));

                if (endkbtoggle == false) {
                    hook.remove();
                }
                hitEntity.setNoDamageTicks(17);
                return;
            }
        }
    }

	@EventHandler
	public void playerOnJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Server server = getServer();
		if (server.getPluginManager().isPluginEnabled("ViaVersion") == true
				|| server.getPluginManager().isPluginEnabled("ProtocolSupport") == true
				|| server.getPluginManager().isPluginEnabled("OldCombatMechanics") == true) {
			endkbtoggle = false;
		}
		if (kbtoggle == false && p.hasPermission("fishingkb.admin")) {
			p.sendMessage("[notice] " + ChatColor.RED + "Fishing hook knockback was disabled! by " + disabler);
		}
		if (endkbtoggle == false && p.hasPermission("fishingkb.admin") && disabler_h != null) {
			p.sendMessage("[notice] " + ChatColor.RED + "Fishing hooking knockback was disabled! by " + disabler_h);
		}
		if (p.hasPermission("fishingkb.admin") && getConfig().getBoolean("DisableUpdateNofitication") == false) {
			SpigotUpdater updater = new SpigotUpdater(this, 62101);
			try {
				if (updater.checkForUpdates())
					p.sendMessage(ChatColor.YELLOW + "FishingrodKnockback was updated! Please update! "
							+ updater.getResourceURL() + "\n" + ChatColor.GREEN + "new version : "
							+ updater.getLatestVersion() + " / now version : " + getServer().getPluginManager()
									.getPlugin("FishingRodKnockback").getDescription().getVersion()
							+ "\n ");
			} catch (Exception error) {
				p.sendMessage("FishingrodCheck:" + error);
			}
		}
	}
}