package me.kurwixon.kits;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;

import java.util.UUID;

public class KitPlugin extends JavaPlugin implements CommandExecutor {

    @Getter private static KitPlugin inst;
    @Getter private static RMap<String, String> kits;
    @Getter private static RedissonClient redissonClient;
    @Getter private static String serverName;
    private RTopic rTopic;

    @Override
    public void onEnable() {
        inst  = this;
        saveDefaultConfig();
        serverName = getConfig().getString("server");
        loadRedisson();
        KitUser.load();
    }

    private void loadRedisson(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redissonClient = Redisson.create(config);

        kits = redissonClient.getMap("kits");

        rTopic = redissonClient.getTopic("kit_topic");
        rTopic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(final CharSequence sequence, final String s) {
                String[] args = s.split("XXX");
                if(args[0].equalsIgnoreCase(serverName)){
                    System.out.println("POMINETO ODEBRANIE PAKIETU!");
                    return;
                }
                UUID uuid = UUID.fromString(args[1]);
                int i = Integer.parseInt(args[2]);
                long l = Long.parseLong(args[3]);

                Kit kit = KitUser.getKit(uuid);
                if(kit == null) {
                    kit = KitUser.create(uuid);
                    System.out.println("UTWORZONO UZYTKOWNIKA OD KITOW");
                }
                if (i == 1) {
                    kit.setKit1(l);
                } else if (i == 2) {
                    kit.setKit2(l);
                } else if (i == 3) {
                    kit.setKit3(l);
                }

                System.out.println("ODEBRANO PAKIET, kit zostal odebrany!");
            }
        });
    }

    public void takeKit(Player player, int k){
        Kit kit = KitUser.getKit(player.getUniqueId());
        long l = System.currentTimeMillis() + (1000 * 60 * 60);
        if(k == 1){
            kit.setKit1(l);
        }else if(k == 2){
            kit.setKit2(l);
        }else if(k == 3){
            kit.setKit3(l);
        }
        kits.put(player.getUniqueId().toString(), kit.toMap());
        rTopic.publishAsync(serverName + "XXX" + player.getUniqueId().toString() + "XXX" + k + "XXX" + l);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(label.equals("kit")){
            if(args.length == 0){
                sender.sendMessage("Podaj kit 1,2,3");
                return false;
            }
            Player player = (Player) sender;
            Kit kit = KitUser.getKit(player.getUniqueId());
            if(kit == null){
                kit = KitUser.create(player.getUniqueId());
            }
            if(args[0].equalsIgnoreCase("1")) {
                if(kit.getKit1() >= System.currentTimeMillis()){
                    sender.sendMessage("Zestaw mozesz odbierac raz na godzine!");
                    return false;
                }
                takeKit(player, 1);
                player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
            }else if(args[0].equalsIgnoreCase("2")) {
                if(kit.getKit2() >= System.currentTimeMillis()){
                    sender.sendMessage("Zestaw mozesz odbierac raz na godzine!");
                    return false;
                }
                takeKit(player, 2);
                player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
            }else if(args[0].equalsIgnoreCase("3")) {
                if(kit.getKit3() >= System.currentTimeMillis()){
                    sender.sendMessage("Zestaw mozesz odbierac raz na godzine!");
                    return false;
                }
                takeKit(player, 3);
                player.getInventory().addItem(new ItemStack(Material.DIAMOND));
            }
            return true;
        }
        return false;
    }
}
