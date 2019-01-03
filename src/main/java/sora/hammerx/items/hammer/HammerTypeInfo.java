package sora.hammerx.items.hammer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import teamroots.embers.RegistryManager;

public class HammerTypeInfo {

    public static enum HammerType {
        WOODEN("Wood", Item.ToolMaterial.WOOD, Item.ToolMaterial.WOOD.getRepairItemStack()),
        STONE("Stone", Item.ToolMaterial.STONE, Item.ToolMaterial.STONE.getRepairItemStack()),
        IRON("Iron", Item.ToolMaterial.IRON, Item.ToolMaterial.IRON.getRepairItemStack()),
        GOLD("Gold", Item.ToolMaterial.GOLD,Item.ToolMaterial.GOLD.getRepairItemStack()),
        DIAMOND("Diamond", Item.ToolMaterial.DIAMOND,Item.ToolMaterial.DIAMOND.getRepairItemStack()),
        COPPER("Copper", RegistryManager.tool_mat_copper,RegistryManager.tool_mat_copper.getRepairItemStack());


        String type;
        public Item.ToolMaterial material;
      ItemStack repairType;


        HammerType(String type, Item.ToolMaterial material, ItemStack repairType) {
            this.type = type;
            this.material = material;
            this.repairType = material.getRepairItemStack();



        }




    }


}
