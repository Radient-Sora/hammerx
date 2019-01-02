package sora.hammerx.items.hammer;

import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class HammerTypeInfo {

    public static enum HammerType {
        WOODEN("Wood", Item.ToolMaterial.WOOD),
        STONE("Stone", Item.ToolMaterial.STONE),
        IRON("Iron", Item.ToolMaterial.IRON),
        GOLD("Gold", Item.ToolMaterial.GOLD),
        DIAMOND("Diamond", Item.ToolMaterial.DIAMOND),
        COPPER("Copper", EnumHelper.addToolMaterial("Copper", 0, 32, 12.0F, 0.0F, 22));


        String type;
        public Item.ToolMaterial material;
        String oreDictName;


        HammerType(String type, Item.ToolMaterial material) {
            this.type = type;
            this.material = material;


        }




    }


}
