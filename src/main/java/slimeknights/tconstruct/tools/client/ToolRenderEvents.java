package slimeknights.tconstruct.tools.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.mojang.blaze3d.vertex.MatrixApplyingVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.DrawHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tinkering.IAoeTool;

import java.util.List;

@Mod.EventBusSubscriber(modid = TConstruct.modID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ToolRenderEvents {

  @SubscribeEvent
  static void renderBlockHighlights(DrawHighlightEvent.HighlightBlock event) {
    PlayerEntity player = Minecraft.getInstance().player;
    PlayerController playerController = Minecraft.getInstance().playerController;

    if (player == null) {
      return;
    }

    World world = player.world;

    ItemStack tool = player.getHeldItemMainhand();

    // AOE preview
    if (!tool.isEmpty()) {
      if (tool.getItem() instanceof IAoeTool) {
        ActiveRenderInfo renderInfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
        ImmutableList<BlockPos> extraBlocks = ((IAoeTool) tool.getItem()).getAOEBlocks(tool, world, player, event.getTarget().getPos());

        WorldRenderer worldRender = event.getContext();
        MatrixStack matrix = event.getMatrix();
        IVertexBuilder vertexBuilder = worldRender.renderTypeTextures.getBufferSource().getBuffer(RenderType.getLines());
        Entity viewEntity = renderInfo.getRenderViewEntity();


        Vec3d vec3d = renderInfo.getProjectedView();
        double d0 = vec3d.getX();
        double d1 = vec3d.getY();
        double d2 = vec3d.getZ();

        matrix.push();

        for (BlockPos pos : extraBlocks) {
          if (world.getWorldBorder().contains(pos)) {
            worldRender.drawSelectionBox(matrix, vertexBuilder, viewEntity, d0, d1, d2, pos, world.getBlockState(pos));
          }
        }

        matrix.pop();
      }
    }
  }

  @SubscribeEvent
  static void renderBlockDamageProgress(RenderWorldLastEvent event) {
    PlayerController controller = Minecraft.getInstance().playerController;

    if (controller == null) {
      return;
    }

    PlayerEntity player = Minecraft.getInstance().player;

    if (player == null) {
      return;
    }

    ItemStack tool = player.getHeldItemMainhand();

    if (!tool.isEmpty()) {
      if (tool.getItem() instanceof IAoeTool) {
        Entity renderEntity = Minecraft.getInstance().getRenderViewEntity();

        if (renderEntity == null) {
          return;
        }

        BlockRayTraceResult traceResult = RayTracer.retrace(player, RayTraceContext.FluidMode.NONE);

        if (traceResult.getType() != RayTraceResult.Type.BLOCK) {
          return;
        }

        ImmutableList<BlockPos> extraBlocks = ((IAoeTool) tool.getItem()).getAOEBlocks(tool, player.world, player, traceResult.getPos());

        if (controller.isHittingBlock) {
          drawBlockDamageTexture(event.getContext(), event.getMatrixStack(), Minecraft.getInstance().gameRenderer.getActiveRenderInfo(), player.getEntityWorld(), extraBlocks);
        }
      }
    }
  }

  private static void drawBlockDamageTexture(WorldRenderer worldRender, MatrixStack matrixStackIn, ActiveRenderInfo renderInfo, World world, List<BlockPos> extraBlocks) {
    double d0 = renderInfo.getProjectedView().x;
    double d1 = renderInfo.getProjectedView().y;
    double d2 = renderInfo.getProjectedView().z;

    int progress = (int) (Minecraft.getInstance().playerController.curBlockDamageMP * 10.0F) - 1;

    if (progress < 0) {
      return;
    }

    progress = Math.min(progress, 10); // Ensure that for whatever reason the progress level doesn't go OOB.

    BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
    IVertexBuilder vertexBuilder = worldRender.renderTypeTextures.getCrumblingBufferSource().getBuffer(ModelBakery.DESTROY_RENDER_TYPES.get(progress));

    for (BlockPos pos : extraBlocks) {
      matrixStackIn.push();
      matrixStackIn.translate((double) pos.getX() - d0, (double) pos.getY() - d1, (double) pos.getZ() - d2);
      IVertexBuilder matrixBuilder = new MatrixApplyingVertexBuilder(vertexBuilder, matrixStackIn.getLast());
      dispatcher.renderBlockDamage(world.getBlockState(pos), pos, world, matrixStackIn, matrixBuilder);
      matrixStackIn.pop();
    }
  }
}