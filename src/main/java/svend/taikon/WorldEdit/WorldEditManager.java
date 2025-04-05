package svend.taikon.WorldEdit;


import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WorldEditManager {

    public static void save(Location primary, Location secondary, File schematicFile) {
        if (primary == null || secondary == null || schematicFile == null) {
            throw new IllegalArgumentException("Параметры не могут быть null");
        }

        Region region = new CuboidRegion(
                BukkitAdapter.asBlockVector(primary),
                BukkitAdapter.asBlockVector(secondary)
        );

        try (EditSession editSession = createEditSession(primary.getWorld())) {
            BlockArrayClipboard clipboard = new BlockArrayClipboard(region);

            ForwardExtentCopy copy = new ForwardExtentCopy(
                    editSession, region, clipboard, region.getMinimumPoint()
            );

            Operations.complete(copy);

            schematicFile.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(schematicFile);
                 ClipboardWriter writer = BuiltInClipboardFormat.SPONGE_SCHEMATIC.getWriter(fos)) {
                writer.write(clipboard);
                Bukkit.getLogger().info("Схема успешно сохранена: " + schematicFile.getAbsolutePath());
            }
        } catch (WorldEditException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void paste(Location to,File schematicFile){
        try (EditSession editSession = createEditSession(to.getWorld());) {
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            ClipboardReader reader = format.getReader(new FileInputStream(schematicFile));

            Clipboard schematic = reader.read();

            Operation operation = new ClipboardHolder(schematic)
                    .createPaste(editSession)
                    .to(BukkitAdapter.asBlockVector(to))
                    .build();

            Operations.complete(operation);
        }
        catch (final Throwable t){
            t.printStackTrace();
        }
    }

    private static EditSession createEditSession(org.bukkit.World bukkitWorld){
        final World world = BukkitAdapter.adapt(bukkitWorld);
        final EditSession session = WorldEdit.getInstance().newEditSession(world);

        session.setSideEffectApplier(SideEffectSet.defaults());
        return session;
    }

}
