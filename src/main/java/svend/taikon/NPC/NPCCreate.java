package svend.taikon.NPC;

import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class NPCCreate {
    public static ServerPlayer[] npcs;

    public static ServerPlayer[] getNpcs() {
        return npcs;
    }

    public static void Create(){
        //TODO: Сделать удаление нпс когда человек выходит

        npcs = new ServerPlayer[6];

        String texture1 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature1 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc1 = NPCFactory.createNPC("Пекарня", texture1, signature1,109,99,136);

        String texture2 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature2 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc2 = NPCFactory.createNPC("Сад", texture2, signature2,97,99,136);

        String texture3 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature3 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc3 = NPCFactory.createNPC("Ресторан", texture3, signature3,84,99,136);

        String texture4 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature4 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc4 = NPCFactory.createNPC("Четвертый", texture4, signature4,84,99,153);

        String texture5 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature5 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc5 = NPCFactory.createNPC("Донат", texture5, signature5,121,99,140);

        String texture6 = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzI2Mzg5NjIyNSwKICAicHJvZmlsZUlkIiA6ICIwNjlhNzlmNDQ0ZTk0NzI2YTViZWZjYTkwZTM4YWFmNSIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb3RjaCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8yOTIwMDlhNDkyNWI1OGYwMmM3N2RhZGMzZWNlZjA3ZWE0Yzc0NzJmNjRlMGZkYzMyY2U1NTIyNDg5MzYyNjgwIgogICAgfQogIH0KfQ==";
        String signature6 = "ArwoD4sGhthC32Qaq1oSwNOWPciJN54mLj+Tq0tZBUMCaw7Gnpj6W9HJhLrax6gVs8X3O5cWUrgLbAIF8uelb5jLdUpm9ZFsAFUo/MtE3oqCXBjoXw8+Wn8y8WR1UAXwv0ts+C6OSyOfLGk0tR7Jmkac6G7bUKYOAMFtCGcppdmoxvhALHPkcsPmdlE8SsHhOVDBp+SE9SBA0V5Z2YDTua34bLdCh4jHibb9x6D8yLxos5ksqcUzsLW9HZ6gqt29GqRD3+M2q1VyXyOjQCR1MD/5A0WfFAFBtExWPRn4V8Fl8a6+814a84H6apaoIN0e6rZHC9ArLEbfSStS54YbjFZ5jfUHx4jkyg0n16B14Z7KLVRmWJjUPtICWaW7zlOOzzq+ZkV1fckVmXEA0Ri349DnWMSGU44nkgPsjD5PL9PLdDqhWqXQGL9f3C+XmUC+5WWdE1cA2W+ZrTN0mZajlkmcwYL0priAZZfzubhVV6PqWAaM9phgaoK7s5oQc6ruaXObauGZvxZ2p+LDx8A+AKnpxSPvjE+fVoOZUAvzVIhwXkFo8Y7+lJi29GjNS8f+fZctPivnABnK2oHXVapvdWlOfpTg/Y8cgc+GHhsvY82f9p7tyFAjV59Ps2G3TDjNbxm7iRaNs4MBUf2e8+mQFt/MbbblCfDBMUOprV0vjks=";
        ServerPlayer npc6 = NPCFactory.createNPC("Продажа ресурсов", texture6, signature6,117,99,161);

        npcs[0] = npc1;
        npcs[1] = npc2;
        npcs[2] = npc3;
        npcs[3] = npc4;
        npcs[4] = npc5;
        npcs[5] = npc6;
    }

    public static void sendNPCsToPlayers( Player player) {
        CraftPlayer craftOnlinePlayer = (CraftPlayer) player;
        ServerGamePacketListenerImpl connection = craftOnlinePlayer.getHandle().connection;

        for (ServerPlayer npc : npcs) {
            if (npc != null) {
                connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
                connection.send(new ClientboundAddPlayerPacket(npc));
            }
        }
    }

}
