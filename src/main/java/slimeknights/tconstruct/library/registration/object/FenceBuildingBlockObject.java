package slimeknights.tconstruct.library.registration.object;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;

public class FenceBuildingBlockObject extends BuildingBlockObject {
  private final BlockItemObject<FenceBlock> fence;

  public FenceBuildingBlockObject(
    BlockItemObject<Block> block,
    BlockItemObject<SlabBlock> slab,
    BlockItemObject<StairsBlock> stairs,
    BlockItemObject<FenceBlock> fence) {
    super(block, slab, stairs);
    this.fence = fence;
  }

  public static FenceBuildingBlockObject fromBlocks(Block block, Block slab, Block stairs, Block fence) {
    return new FenceBuildingBlockObject(
      BlockItemObject.fromBlock(block),
      BlockItemObject.fromBlock((SlabBlock)slab),
      BlockItemObject.fromBlock((StairsBlock)stairs),
      BlockItemObject.fromBlock((FenceBlock) fence)
    );
  }

  /** Gets the fence for this block */
  public FenceBlock getFence() {
    return fence.get();
  }

  /** Gets the fence item for this block */
  public Item getFenceItem() {
    return fence.asItem();
  }
}