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
import svend.taikon.Menu.BuildingsMenu.BakeryMenu;
import svend.taikon.Menu.BuildingsMenu.GardenMenu;
import svend.taikon.Menu.BuildingsMenu.RestaurantMenu;
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
                Player player = event.getPlayer();

                int entityId = packet.getIntegers().read(0);

                EnumWrappers.EntityUseAction action = packet.getEnumEntityUseActions().read(0).getAction();

                String npcName = NPCFactory.getNPCName(entityId);
                if (npcName != null) {
                    getServer().getScheduler().runTask(Taikon.getPlugin(), new Runnable() {
                        @Override
                        public void run() {
                            switch (npcName){
                                case "Пекарня":
                                    BakeryMenu bakeryMenu = new BakeryMenu(player);
                                    bakeryMenu.open();
                                    break;
                                case "Сад":
                                    GardenMenu gardenMenu = new GardenMenu(player);
                                    gardenMenu.open();
                                    break;
                                case "Ресторан":
                                    RestaurantMenu restaurantMenu = new RestaurantMenu(player);
                                    restaurantMenu.open();
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
