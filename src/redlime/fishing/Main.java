package redlime.fishing;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;


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
        ent.add("ZOMBIE_VILLAGER"); ent.add("DONKEY");
        if (ent.contains(target)) {
            return true;
        }
        return false;
    }

    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        Server server = (Server) getServer();
        if (server.getPluginManager().isPluginEnabled("ViaVersion") == true || server.getPluginManager().isPluginEnabled("ProtocolSupport") == true || server.getPluginManager().isPluginEnabled("OldCombatMechanics") == true) {
            endkbtoggle = false;
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) {
        Player p = (Player) sender;
        if (lable.equalsIgnoreCase("fishingknockback") && p.hasPermission("fishingkb.admin")) {
            if(args.length == 0) {
                p.sendMessage(ChatColor.GREEN + "==FishingRod Knockback by RED_LIME==");
                p.sendMessage(ChatColor.GREEN + "version 1.4.0");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback on/off - toggle knockback");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback debug");
                p.sendMessage(ChatColor.GREEN + "/fishingknockback hook - toggle hooking knockback");
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
                        endkbtoggle = false;
                    }
                    else if (endkbtoggle == false) {
                        p.sendMessage(ChatColor.GREEN + "Hooking Knockback is enable. \n(if you using Viaversion, ProtocolSupport and OldCombatMechanics. Knockback when pulling a fishing rod may not work properly)");
                        endkbtoggle = true;
                        disabler_h = p.getName();
                    }
                    return false;
                }
                else{
                    p.sendMessage(ChatColor.GREEN + "==FishingRod Knockback by RED_LIME==");
                    p.sendMessage(ChatColor.GREEN + "version 1.4.0");
                    p.sendMessage(ChatColor.GREEN + "/fishingknockback on/off - toggle knockback");
                    p.sendMessage(ChatColor.GREEN + "/fishingknockback debug");
                    p.sendMessage(ChatColor.GREEN + "/fishingknockback hook - toggle hooking knockback");
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
            player.sendMessage(e.getHitEntity().getType().toString());
        }
        if (kbtoggle == false || e.getHitEntity() == null) { //토글 기능 확인
            return;
        }

        if(e.getEntityType() != EntityType.FISHING_HOOK) { //낚싯찌 확인
            return;
        }

        if (e.getHitEntity().getType() != EntityType.PLAYER && entitytypes(e.getHitEntity().getType().toString()) == true) { //엔티티 처리
            FishHook hook = (FishHook) e.getEntity();
            Player hookShooter = (Player) hook.getShooter();
            LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
            double kx = hook.getLocation().getDirection().getX() / 2.5;
            double kz = hook.getLocation().getDirection().getZ() / 2.5;
            kx = kx - kx * 2;
            if (debug == true && hookShooter.hasPermission("fishingkb.admin")) {
                hookShooter.sendMessage(kx + " " + kz);
                hookShooter.sendMessage(kx + " " + kz);
            }
            if (hitEntity.getNoDamageTicks() >= 8.5) {
                hook.remove();
                return;
            }
            else if (hitEntity.getNoDamageTicks() < 8.5 && hitEntity.getLocation().getWorld().getBlockAt(hitEntity.getLocation()).getType().toString() != "AIR") {
                hitEntity.setNoDamageTicks(0);
            }
            hitEntity.damage(0.01, hookShooter);
            hitEntity.setVelocity(new Vector(kx, 0.375, kz));
            if (endkbtoggle == false) {
                hook.remove();
            }
            hitEntity.setNoDamageTicks(18);
            return;
        }

        if (e.getHitEntity().getType() == EntityType.PLAYER) { //플레이어 처리
            FishHook hook = (FishHook) e.getEntity();
            Player rodder = (Player) hook.getShooter();
            Player player = (Player) e.getHitEntity();
            double kx = hook.getLocation().getDirection().getX() / 2.5;
            double kz = hook.getLocation().getDirection().getZ() / 2.5;
            kx = kx - kx * 2;
            if (debug == true && rodder.hasPermission("fishingkb.admin")) { //디버그 모드
                rodder.sendMessage(kx + " " + kz);
            }
            if (player.getName().equalsIgnoreCase(rodder.getName()) || player.getGameMode() == GameMode.CREATIVE) { //자신이나 크리 떄리기 방지
                hook.remove();
                return;
            }
            if (player.getNoDamageTicks() >= 10) {
                hook.remove();
                return;
            }
            else if (player.getNoDamageTicks() < 10 && player.getLocation().getWorld().getBlockAt(player.getLocation()).getType().toString() != "AIR") {
                player.setNoDamageTicks(0);
            }
            player.damage(0.01, rodder);
            player.setVelocity(new Vector(kx, 0.365, kz));
            if (endkbtoggle == false) {
                hook.remove();
            }
            player.setNoDamageTicks(18);
            return;
        }
        return;
    }


    @EventHandler
    public void playerOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (kbtoggle == false && p.hasPermission("fishingkb.admin")) {
            p.sendMessage("[notice] " + ChatColor.RED + "Fishing hook knockback was disabled! by "+disabler);
        }
        if (endkbtoggle == false && p.hasPermission("fishingkb.admin") && disabler_h == null) {
            p.sendMessage("[notice] " + ChatColor.RED + "Fishing hooking knockback was enabled! by " + disabler_h);
        }
    }
}