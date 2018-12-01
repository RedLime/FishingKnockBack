package redlime.fishing;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class TabCompleter implements org.bukkit.command.TabCompleter {

    public static Main plugin = Main.plugin;

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fishingknockback")) {
            if (args.length == 1) {
                List<String> list = new ArrayList<>();

                list.add("reload");
                list.add("on");
                list.add("off");
                list.add("hook");
                list.add("debug");
                list.add("config");
                list.add("help");
                return list;
            }
            if (args[0].equalsIgnoreCase("config")) {
                if (args.length == 2) {
                    List<String> con = new ArrayList<>();
                    con.add("world"); con.add("entity");
                    return con;
                }
                else if (args[1].equalsIgnoreCase("entity") && args.length == 3) {
                    List<String> ent = new ArrayList<>();
                    ent.add("BAT"); ent.add("BLAZE"); ent.add("CAVE_SPIDER"); ent.add("CHICKEN");  ent.add("COW"); ent.add("CREEPER");
                    ent.add("ELDER_GUARDIAN"); ent.add("ENDERMAN"); ent.add("EVOKER_FANGS"); ent.add("ENDERMITE"); ent.add("EVOKER");
                    ent.add("GHAST"); ent.add("GIANT"); ent.add("HUSK"); ent.add("GUARDIAN"); ent.add("HORSE");
                    ent.add("ILLUSIONER"); ent.add("IRON_GOLEM"); ent.add("LLAMA"); ent.add("MAGMA_CUBE"); ent.add("OCELOT");
                    ent.add("MULE"); ent.add("MUSHROOM_COW"); ent.add("PIG"); ent.add("PARROT"); ent.add("PIG_ZOMBIE");
                    ent.add("POLAR_BEAR"); ent.add("SQUID"); ent.add("RABBIT"); ent.add("SHEEP"); ent.add("SHULKER");
                    ent.add("SILVERFISH"); ent.add("SKELETON"); ent.add("SKELETON_HORSE"); ent.add("SLIME"); ent.add("SNOWMAN");
                    ent.add("SPIDER"); ent.add("STRAY"); ent.add("VEX"); ent.add("VILLAGER"); ent.add("VINDICATOR"); ent.add("WITCH");
                    ent.add("WITHER"); ent.add("WITHER_SKELETON"); ent.add("WOLF"); ent.add("ZOMBIE"); ent.add("ZOMBIE_HORSE");
                    ent.add("ZOMBIE_VILLAGER"); ent.add("DONKEY"); ent.add("PLAYER"); ent.add("COD"); ent.add("DROWNED");
                    ent.add("PHANTOM"); ent.add("TNT"); ent.add("PRIMED_TNT"); ent.add("TROPICAL_FISH"); ent.add("TURTLE");
                    ent.add("SALMON"); ent.add("PUFFERFISH");
                    return ent;
                }
                else if (args[1].equalsIgnoreCase("world") && args.length == 3) {
                    List<String> wor = new ArrayList<>();
                    ArrayList<String> worlds = new ArrayList<>();
                    int i;
                    for (i=0; i<Bukkit.getWorlds().size(); i=i+1) {
                        wor.add(Bukkit.getWorlds().get(i).getName());
                    }

                    return wor;
                }
            }

        }
        return null;
    }
}
