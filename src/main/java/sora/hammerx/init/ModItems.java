package sora.hammerx.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemTool;
import sora.hammerx.items.ItemHammer;
import sora.hammerx.items.hammer.HammerTypeInfo;

public class ModItems {
    public static ItemTool WOODEN__HAMMER = new ItemHammer("wooden_hammer", HammerTypeInfo.HammerType.WOODEN);
    public static ItemTool STONE__HAMMER = new ItemHammer("stone_hammer", HammerTypeInfo.HammerType.STONE);
    public static ItemTool IRON_HAMMER = new ItemHammer("iron_hammer", HammerTypeInfo.HammerType.IRON);
    public static ItemTool GOLD_HAMMER = new ItemHammer("gold_hammer", HammerTypeInfo.HammerType.GOLD);
    public static ItemTool DIAMOND_HAMMER = new ItemHammer("diamond_hammer", HammerTypeInfo.HammerType.DIAMOND);

    //ORE DICT ITEMS
    public static Item COPPER_HAMMER = new ItemHammer("copper_hammer", HammerTypeInfo.HammerType.COPPER);
}
