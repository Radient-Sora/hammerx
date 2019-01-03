package sora.hammerx.utility;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import scala.collection.parallel.ParIterableLike;
import sora.hammerx.items.ItemHammer;

import java.util.ArrayList;
import java.util.List;

public class HammerHelper {

    public static void breakBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            return;
        }

        IBlockState state = world.getBlockState(pos);

        if (!world.isRemote) {
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                stack.onBlockDestroyed(world, state, pos, player);
                if (ForgeHooks.onBlockBreakEvent(world, playerMP.interactionManager.getGameType(), playerMP, pos) == -1) {
                    return;
                }
                TileEntity entity = world.getTileEntity(pos);
                if (state.getBlock().removedByPlayer(state, world, pos, player, true)) {
                    state.getBlock().onBlockDestroyedByPlayer(world, pos, state);
                    state.getBlock().harvestBlock(world, player, pos, state, entity, stack);
                    state.getBlock().dropXpOnBlockBreak(world, pos, 1);
                }
                playerMP.connection.sendPacket(new SPacketBlockChange(world, pos));
            }
        } else {
            if (state.getBlock().removedByPlayer(state,world,pos,player,true)){
                state.getBlock().onBlockDestroyedByPlayer(world,pos,state);
            }
            stack.onBlockDestroyed(world,state,pos,player);
            if(stack.getCount() == 0 && stack == player.getHeldItemMainhand()){
                ForgeEventFactory.onPlayerDestroyItem(player,stack,EnumHand.MAIN_HAND);
                player.setHeldItem(EnumHand.MAIN_HAND,ItemStack.EMPTY);
            }
            NetHandlerPlayClient client = Minecraft.getMinecraft().getConnection();
            client.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK,pos, Minecraft.getMinecraft().objectMouseOver.sideHit));
        }
    }

    public static List<BlockPos> hammerBlockPos(ItemStack stack, World world, EntityPlayer player, BlockPos origin, int width, int height) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemHammer)) {
            return null;
        }
        IBlockState state = world.getBlockState(origin);
        if (state.getMaterial() == Material.AIR) {
            return null;
        }
        RayTraceResult rayTrace = ((ItemHammer) stack.getItem()).rayTrace(world, player, true);
        if (rayTrace == null || !origin.equals(rayTrace.getBlockPos())) {
            rayTrace = ((ItemHammer) stack.getItem()).rayTrace(world, player, false);
            if (rayTrace == null || !origin.equals(rayTrace.getBlockPos())) {
                return null;
            }
        }

        int x, y, z;
        BlockPos start = origin;
        switch (rayTrace.sideHit) {
            case DOWN:
            case UP:
                // x y depends on the angle we look?
                Vec3i vec = player.getHorizontalFacing().getDirectionVec();
                x = vec.getX() * height + vec.getZ() * width;
                y = rayTrace.sideHit.getAxisDirection().getOffset() * -1;
                z = vec.getX() * width + vec.getZ() * height;
                start = start.add(-x / 2, 0, -z / 2);
                if (x % 2 == 0) {
                    if (x > 0 && rayTrace.hitVec.x - rayTrace.getBlockPos().getX() > 0.5d) {
                        start = start.add(1, 0, 0);
                    } else if (x < 0 && rayTrace.hitVec.x - rayTrace.getBlockPos().getX() < 0.5d) {
                        start = start.add(-1, 0, 0);
                    }
                }
                if (z % 2 == 0) {
                    if (z > 0 && rayTrace.hitVec.z - rayTrace.getBlockPos().getZ() > 0.5d) {
                        start = start.add(0, 0, 1);
                    } else if (z < 0 && rayTrace.hitVec.z - rayTrace.getBlockPos().getZ() < 0.5d) {
                        start = start.add(0, 0, -1);
                    }
                }
                break;
            case NORTH:
            case SOUTH:
                x = width;
                y = height;
                z = rayTrace.sideHit.getAxisDirection().getOffset() * -1;
                start = start.add(-x / 2, -y / 2, 0);
                if (x % 2 == 0 && rayTrace.hitVec.x - rayTrace.getBlockPos().getX() > 0.5d) {
                    start = start.add(1, 0, 0);
                }
                if (y % 2 == 0 && rayTrace.hitVec.y - rayTrace.getBlockPos().getY() > 0.5d) {
                    start = start.add(0, 1, 0);
                }
                break;
            case WEST:
            case EAST:
                x = rayTrace.sideHit.getAxisDirection().getOffset() * -1;
                y = height;
                z = width;
                start = start.add(-0, -y / 2, -z / 2);
                if (y % 2 == 0 && rayTrace.hitVec.y - rayTrace.getBlockPos().getY() > 0.5d) {
                    start = start.add(0, 1, 0);
                }
                if (z % 2 == 0 && rayTrace.hitVec.z - rayTrace.getBlockPos().getZ() > 0.5d) {
                    start = start.add(0, 0, 1);
                }
                break;
            default:
                x = y = z = 0;
        }

        List<BlockPos> list = new ArrayList<>();
        for (int x1 = start.getX(); x1 != start.getX() + x; x1 += x / MathHelper.abs(x)) {
            for (int y1 = start.getY(); y1 != start.getY() + y; y1 += y / MathHelper.abs(y)) {
                for (int z1 = start.getZ(); z1 != start.getZ() + z; z1 += z / MathHelper.abs(z)) {
                    if (x1 == origin.getX() && y1 == origin.getY() && z1 == origin.getZ()) {
                        continue;
                    }
                    if ((MathHelper.abs(x1 - origin.getX()) + MathHelper.abs(y1 - origin.getY()) + MathHelper.abs(z1 - origin.getZ())) > 1) {
                        continue;
                    }
                    list.add(new BlockPos(x1, y1, z1));
                }
            }
        }
        return list;
    }

}
