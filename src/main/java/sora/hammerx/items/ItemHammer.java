package sora.hammerx.items;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sora.hammerx.Hammerx;
import sora.hammerx.items.hammer.HammerTypeInfo;

import javax.annotation.Nullable;

public class ItemHammer extends ItemPickaxe {


    public ItemHammer(String name, HammerTypeInfo.HammerType type) {
        super(type.material);
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setCreativeTab(Hammerx.CREATIVE_TABS);
        this.setMaxDamage(type.material.getMaxUses());
        this.setMaxStackSize(1);


    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        stack.damageItem(1, entityLiving);
        return true;
    }

    @Override
    public RayTraceResult rayTrace(World worldIn, EntityPlayer playerIn, boolean useLiquids) {
        return super.rayTrace(worldIn, playerIn, useLiquids);
    }


    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
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
