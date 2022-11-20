package me.kurwixon.kits;

import lombok.Getter;
import org.redisson.api.RMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitUser {

    @Getter private static Map<UUID, Kit> KITS = new HashMap<>();

    public static Kit create(UUID uuid){
        Kit kit = new Kit(uuid);
        getKITS().put(uuid, kit);
        return kit;
    }

    public static Kit getKit(UUID uuid){
        return getKITS().getOrDefault(uuid, null);
    }

    public static void load(){
        for(RMap.Entry<String, String> entry : KitPlugin.getKits().entrySet()){
            UUID uuid = UUID.fromString(entry.getKey());
            String[] k = entry.getValue().split("YYY");
            long kit1 = Long.parseLong(k[0]);
            long kit2 = Long.parseLong(k[1]);
            long kit3 = Long.parseLong(k[2]);
            getKITS().put(uuid, new Kit(uuid, kit1, kit2, kit3));
        }
    }
}
