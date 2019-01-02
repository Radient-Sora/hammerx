package sora.hammerx.utility;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class HammerHelper {

    public static RayTraceResult playerRaytrace(World world, Entity player, boolean par3, double range){

        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;

        if (!world.isRemote && player instanceof EntityPlayer) {
            d1 = d1 + 1.62D;
        }

        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        // Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
        Vec3d vec3 = new Vec3d(d0, d1, d2);

        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;

        if (player instanceof EntityPlayerMP) {
            d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }

        Vec3d vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);

        return world.rayTraceBlocks(vec3, vec31, par3, !par3, par3);
    }

    public static boolean atemptHarvestBlock(World world, IBlockState state, BlockPos pos, EnumFacing side, EntityPlayer player)
    {
        Block block = state.getBlock();

        if (block.isAir(state, world, pos))
        {
            //HammerX.log.log(Level.INFO, "Block @ " + pos.toString() + " is air");
            return false;
        }

        EntityPlayerMP playerMP = null;
        if (player instanceof EntityPlayerMP)
        {
            playerMP = (EntityPlayerMP) player;
        }

        ItemStack item = player.getHeldItemMainhand();

        if(item == null || item.getItem() == null)
        {
            return false;
        }

        if (!(item.getItem().getToolClasses(item).contains(block.getHarvestTool(state)) || item.getItem().getDestroySpeed(item, state) > 1.0f))
        {
            //HammerX.log.log(Level.INFO, "Apparently wrong tool class");
            return false;
        }

        if (!ForgeHooks.canHarvestBlock(block, player, world, pos))
        {

            return false;
        }

        int event = 0;
        if (playerMP != null)
        {
            //HammerX.log.log(Level.INFO, "player isn't null");

            event = ForgeHooks.onBlockBreakEvent(world, world.getWorldInfo().getGameType(), playerMP, pos);
            if (event == -1)
            {
                return false;
            }
        }

        world.playEvent(playerMP, 2001, pos, Block.getStateId(state));


        if (player.capabilities.isCreativeMode)
        {
            if (!world.isRemote)
            {
                block.onBlockHarvested(world, pos, state, player);
            }
            if (block.removedByPlayer(state, world, pos, player, false))
            {
                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            if (!world.isRemote)
            {
                playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
            }
            else
            {
                Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));
            }
            return true;
        }
        if (!world.isRemote)
        {
            block.onBlockHarvested(world, pos, state, player);
            if (block.removedByPlayer(state, world, pos, player, true))
            {
                //HammerX.log.log(Level.INFO, "Block @ " + pos.toString() + " was removed by the player");
                block.onBlockDestroyedByPlayer(world, pos, state);
                block.harvestBlock(world, player, pos, state, null, item);
                block.dropXpOnBlockBreak(world, pos, event);
            }
            playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
        }
        else
        {
            if (block.removedByPlayer(state, world, pos, player, true))
            {
                //HammerX.log.log(Level.INFO, "Block @ " + pos.toString() + " removed by player client");

                block.onBlockDestroyedByPlayer(world, pos, state);
            }
            Minecraft.getMinecraft().getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, side));

        }
        return true;
    }
}
