package sora.hammerx.event;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sora.hammerx.Info;
import sora.hammerx.items.ItemHammer;
import sora.hammerx.utility.HammerHelper;

@Mod.EventBusSubscriber(modid = Info.MODID)
public class HammerxEventHandler {
    @SubscribeEvent
    public void onBlockBroken(BlockEvent.BreakEvent event) {
        ItemStack tool = event.getPlayer().getHeldItemMainhand();
        if (tool.getItem() instanceof ItemHammer) {
            BlockPos pos = event.getPos();
            for (BlockPos miningAreaPos : HammerHelper.hammerBlockPos(tool, event.getWorld(), event.getPlayer(), pos, 3, 3)) {
                HammerHelper.breakBlock(tool, event.getWorld(), event.getPlayer(), miningAreaPos);
            }
            HammerHelper.breakBlock(tool, event.getWorld(), event.getPlayer(), pos);
        }

    }
}
