package svend.taikon.NPC;

import com.mojang.authlib.properties.Property;
import net.minecraft.advancements.critereon.PlayerPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCFactory {
    private static final Map<Integer, String> npcMap = new HashMap<>(); // Храним ID и имя NPC

    public static ServerPlayer createNPC(String name, String texture, String signature, double x, double y, double z) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel level = server.overworld();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        gameProfile.getProperties().put("textures", new Property("textures", texture, signature));

        ServerPlayer npc = new ServerPlayer(server, level, gameProfile);
        npc.setPos(x, y, z);

        // Добавляем NPC в мир
        //level.addEntity(npc);

        // Добавляем NPC в HashMap для управления
        npcMap.put(npc.getId(), name);

        return npc;
    }

    public static String getNPCName(int entityId) {
        return npcMap.get(entityId);
    }
}
