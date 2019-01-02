package sora.hammerx.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import sora.hammerx.Hammerx;
import sora.hammerx.items.hammer.HammerTypeInfo;
import sora.hammerx.utility.HammerHelper;

import javax.annotation.Nullable;
import java.util.Set;

public class ItemHammer extends ItemTool {
    public static final Set<Block> PICKAXE_EFFECTIVE_ON = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.ANVIL);
    public static final Set<Block> SHOVEL_EFFECTIVE_ON = Sets.newHashSet(Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH, Blocks.CONCRETE_POWDER);



    public ItemHammer(String name, HammerTypeInfo.HammerType type) {
        super(type.material.getAttackDamage(), type.material.getEfficiency(),type.material,PICKAXE_EFFECTIVE_ON);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setCreativeTab(Hammerx.CREATIVE_TABS);
        this.setMaxDamage(type.material.getMaxUses());
        this.setMaxStackSize(1);


    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
       stack.damageItem(1,entityLiving);
    return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
      World world = player.world;
      IBlockState state = world.getBlockState(pos);
      Block block = state.getBlock();

      if(state.getBlockHardness(world,pos) == 0.0F){
          return false;
      }
      if(!canHarvestBlock(state,stack)){
          if(!player.capabilities.isCreativeMode){
              stack.damageItem(1,player);
          }
          return false;
      }
      if(player.isSneaking()){
          if(!player.capabilities.isCreativeMode){
              stack.damageItem(1,player);
          }
          return false;
      }
        RayTraceResult object = HammerHelper.playerRaytrace(world, player, false, 4.5D);

        if (object == null) {
            return super.onBlockDestroyed(stack, world, state, pos, player);
        }

        EnumFacing side = object.sideHit;
        int xmove = 0;
        int ymove = 0;
        int zmove = 0;

        if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
            xmove = 1;
            zmove = 1;
        } else {
            ymove = 1;
            if (side == EnumFacing.WEST || side == EnumFacing.EAST) {
                zmove = 1;
            } else {
                xmove = 1;
            }
        }

        float strength = ForgeHooks.blockStrength(state, player, world, pos);

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = -xmove; i <= xmove; i++) {
            for (int j = -ymove; j <= ymove; j++) {
                for (int k = -zmove; k <= zmove; k++) {
                    if ((x + i) != x || (y + j) != y || (z + k) != z) {
                        checkBlockBreak(world, player, new BlockPos(x + i, y + j, z + k), stack, strength,block, side);
                    }
                }
            }
        }
        return false;

    }

    public void checkBlockBreak(World world, EntityPlayer player, BlockPos pos, ItemStack stack, float strength, Block block, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        Block breakBlock = state.getBlock();
        Material material = block.getMaterial(state);
        if (breakBlock.getMaterial(state) == material && ForgeHooks.canHarvestBlock(breakBlock, player, world, pos)
                && stack.canHarvestBlock(state)) {
            float newStrength = ForgeHooks.blockStrength(state, player, world, pos);
            if (newStrength > 0f && strength / newStrength <= 10f) {
                if ((double) breakBlock.getBlockHardness(state, world, pos) != 0.0D) {
                } else {
                    HammerHelper.atemptHarvestBlock(world, state, pos, side, player);
                }
            }
        }
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        if (!mat.isEmpty() && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
       return toolMaterial.getHarvestLevel();
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return toolMaterial.getHarvestLevel();
    }
}
