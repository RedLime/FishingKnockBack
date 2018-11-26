package redlime.fishing;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


@SuppressWarnings("ALL")
public class Main extends JavaPlugin implements Listener {
    boolean kbtoggle = true;
    boolean debug = false;
    boolean endkbtoggle = true;
    String disabler = null;
    String disabler_h = null;


    public static boolean entitytypes(String target) {
        ArrayList ent = new ArrayList();
        ent.add("BAT");
        ent.add("BLAZE"); ent.add("CAVE_SPIDER"); ent.add("CHICKEN");  ent.add("COW"); ent.add("CREEPER");
        ent.add("ELDER_GUARDIAN"); ent.add("ENDERMAN"); ent.add("EVOKER_FANGS"); ent.add("ENDERMITE"); ent.add("EVOKER");
        ent.add("GHAST"); ent.add("GIANT"); ent.add("HUSK"); ent.add("GUARDIAN"); ent.add("HORSE");
        ent.add("ILLUSIONER"); ent.add("IRON_GOLEM"); ent.add("LLAMA"); ent.add("MAGMA_CUBE"); ent.add("OCELOT");
        ent.add("MULE"); ent.add("MUSHROOM_COW"); ent.add("PIG"); ent.add("PARROT"); ent.add("PIG_ZOMBIE");
        ent.add("POLAR_BEAR"); ent.add("SQUID"); ent.add("RABBIT"); ent.add("SHEEP"); ent.add("SHULKER");
        ent.add("SILVERFISH"); ent.add("SKELETON"); ent.add("SKELETON_HORSE"); ent.add("SLIME"); ent.add("SNOWMAN");
        ent.add("SPIDER"); ent.add("STRAY"); ent.add("VEX"); ent.add("VILLAGER"); ent.add("VINDICATOR"); ent.add("WITCH");
        ent.add("WITHER"); ent.add("WITHER_SKELETON"); ent.add("WOLF"); ent.add("ZOMBIE"); ent.add("ZOMBIE_HORSE");
        ent.add("ZOMBIE_VILLAGER"); ent.add("DONKEY"); ent.add("PLAYER");
        if (ent.contains(target)) {
            return true;
        }
        return false;
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        Server server = (Server) getServer();
        saveDefaultConfig();

        getConfig().addDefault("DisableWorld", new ArrayList<String>());
        getConfig().addDefault("DisableEntityType", new ArrayList<String>());
        getConfig().addDefault("DisableUpdateNofitication", false);
        FileConfiguration config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player p = (Player) sender;

        getCommand("fishingknockback").setTabCompleter(new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
                List<String> list = new ArrayList<>();
                if (command.getName().equalsIgnoreCase("fishingknockback") && strings.length == 0) {
                    list.add("americano");
                    return list;
                }
                return null;
            }});

        if (cmd.getName().equalsIgnoreCase("fishingknockback") && p.hasPermission("fishingkb.admin")) {
            if(args.length == 0) {
                p.sendMessage(ChatColor.GREEN + "\n==FishingRod Knockback by RED_LIME==");
                p.sendMessage(ChatColor.GREEN + "Version : " + getServer().getPluginManager().getPlugin("FishingRodKnockback").getDescription().getVersion());
                p.sendMessage(ChatColor.GREEN + "/fishingknockback on/off - toggle knockback");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback debug");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback hook - toggle hooking knockback");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback reload - reload config file");
                return false;
            }
            else if(args.length > 0) {
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
                    }
                    else if (debug == false) {
                        p.sendMessage(ChatColor.GREEN + "DebugMode enable.");
                        debug = true;
                    }
                    return false;
                }
                if (args[0].equalsIgnoreCase("hook")) {
                    if (endkbtoggle == true) {
                        p.sendMessage(ChatColor.GREEN + "Hooking Knockback is disable.");
                        disabler_h = p.getName();
                        endkbtoggle = false;
                    }
                    else if (endkbtoggle == false) {
                        p.sendMessage(ChatColor.GREEN + "Hooking Knockback is enable. \n(if you using Viaversion, ProtocolSupport and OldCombatMechanics. Knockback when pulling a fishing rod may not work properly)");
                        endkbtoggle = true;

                    }
                    return false;
                }
                if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("fishingkb.admin")) {
                    this.reloadConfig();
                    this.getConfig().options().copyDefaults(true);
                    this.saveConfig();
                    sender.sendMessage(ChatColor.GREEN+"FishingRodKnockback was reloaded!");
                    return false;
                }
                else{
                    p.performCommand("fishingknockback");
                    return false;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onRodLand(ProjectileHitEvent e) {
        if (debug == true) {
            Player player = (Player) e.getEntity().getShooter();
            if (player.isOp() == true) {
            player.sendMessage("entity:"+e.getHitEntity().getType().toString()); }
        }
        if (kbtoggle == false || e.getHitEntity() == null) { //토글 기능 확인
            return;
        }

        if(e.getEntityType() != EntityType.FISHING_HOOK) { //낚싯찌 확인
            return;
        }
        if(getConfig().getList("DisableWorld").toString().contains(e.getEntity().getLocation().getWorld().getName().toString()) == true) {
            return;
        }
        if(e.getHitEntity().getType() != EntityType.PLAYER && getConfig().getList("DisableEntityType").contains(e.getHitEntity().getType().toString()) == true) {
            return;
        }
        if (entitytypes(e.getHitEntity().getType().toString()) == true) { //엔티티 처리
            FishHook hook = (FishHook) e.getEntity();
            Player hookShooter = (Player) hook.getShooter();
            LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
            double kx = hook.getLocation().getDirection().getX() / 2.5;
            double kz = hook.getLocation().getDirection().getZ() / 2.5;
            kx = kx - kx * 2;
            if (debug == true && hookShooter.hasPermission("fishingkb.admin")) {
                hookShooter.sendMessage("Direction:"+kx + " " + kz);
            }
            if (hitEntity.getNoDamageTicks() >= 6.5) {
                hook.remove();
                hookShooter.getItemInHand().setDurability((short) (hookShooter.getItemInHand().getDurability() + 1));
                if (hookShooter.getItemInHand().getDurability() >= 60) {
                    hookShooter.setItemInHand(null);
                }
                return;
            }
            else if (hitEntity.getNoDamageTicks() < 6.5 && hitEntity.getLocation().getWorld().getBlockAt(hitEntity.getLocation()).getType().toString() != "AIR") {
                hitEntity.setNoDamageTicks(0);
            }
            hitEntity.damage(0.001, hookShooter);
            double upVel = 0.37;
            if (hitEntity.isOnGround() == false) { upVel = 0; }
            hitEntity.setVelocity(new Vector(kx, upVel, kz));
            if (endkbtoggle == false) { hook.remove(); }
            hitEntity.setNoDamageTicks(18);
            return;
        }
    }

    @EventHandler
    public void fishinghooking(PlayerFishEvent e) {
        if (e.getCaught() == null) { return; }
        Entity entity = (Entity) e.getCaught();
        if (entity.getType() == EntityType.DROPPED_ITEM) { return; }
        LivingEntity le = (LivingEntity) e.getCaught();
        Player player = e.getPlayer();
        if (entitytypes(entity.getType().toString()) == true) {
            player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 4));
            if (player.getItemInHand().getDurability() >= 60) {
                player.setItemInHand(null);
            }
        }
        if (debug == true && player.isOp() == true) {
            player.sendMessage("Durability:"+String.valueOf(player.getItemInHand().getDurability()));
        }
        return;
    }


    @EventHandler
    public void playerOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Server server = getServer();
        if (server.getPluginManager().isPluginEnabled("ViaVersion") == true || server.getPluginManager().isPluginEnabled("ProtocolSupport") == true || server.getPluginManager().isPluginEnabled("OldCombatMechanics") == true) {
            endkbtoggle = false;
        }
        if (kbtoggle == false && p.hasPermission("fishingkb.admin")) {
            p.sendMessage("[notice] " + ChatColor.RED + "Fishing hook knockback was disabled! by "+disabler);
        }
        if (endkbtoggle == false && p.hasPermission("fishingkb.admin") && disabler_h != null) {
            p.sendMessage("[notice] " + ChatColor.RED + "Fishing hooking knockback was disabled! by " + disabler_h);
        }
        if (p.hasPermission("fishingkb.admin") && getConfig().getBoolean("DisableUpdateNofitication") == false) {
            SpigotUpdater updater = new SpigotUpdater(this, 62101);
            try {
                if (updater.checkForUpdates())
                    p.sendMessage(ChatColor.YELLOW + "FishingrodKnockback was updated! Please update! "+updater.getResourceURL()+"\n" +
                            ChatColor.GREEN+"new version : " + updater.getLatestVersion() +
                            " / now version : " + getServer().getPluginManager().getPlugin("FishingRodKnockback").getDescription().getVersion()+"\n ");
            } catch (Exception error) {
                p.sendMessage("FishingrodCheck:"+error);
            }
        }
    }
}