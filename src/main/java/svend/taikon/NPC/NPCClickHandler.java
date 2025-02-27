package svend.taikon.NPC;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import svend.taikon.Menu.BakeryMenu;
import svend.taikon.Menu.DonatMenu;
import svend.taikon.Menu.SellResourceMenu;
import svend.taikon.Menu.ToolsMenu;
import svend.taikon.Taikon;

import static org.bukkit.Bukkit.getServer;

public class NPCClickHandler {
    public static void registerClickHandler(JavaPlugin plugin) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer(); // Получаем текущего игрока

                // Получаем ID сущности, с которой взаимодействовали
                int entityId = packet.getIntegers().read(0);

                // Получаем тип взаимодействия (правая/левая кнопка мыши)
                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();

                // Проверяем, является ли сущность NPC
                String npcName = NPCFactory.getNPCName(entityId);
                if (npcName != null) {
                    getServer().getScheduler().runTask(Taikon.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            switch (npcName){
                                case "Первый":
                                    BakeryMenu bakeryMenu = new BakeryMenu(player);
                                    bakeryMenu.open();
                                    break;
                                case "Второй":
                                    break;
                                case "Третий":
                                    break;
                                case "Четвертый":
                                    ToolsMenu menu = new ToolsMenu(player);
                                    menu.open();
                                    break;
                                case "Донат":
                                    DonatMenu donatMenu = new DonatMenu(player);
                                    donatMenu.open();
                                    break;
                                case "Продажа ресурсов":
                                    SellResourceMenu sellResourceMenu = new SellResourceMenu(player);
                                    sellResourceMenu.open();
                                    break;
                            }
                        }
                    });

                }
            }
        });

    }
}
