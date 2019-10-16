package tterrag.treesimulator;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageBonemealParticles
{
    @SuppressWarnings("null")
    public MessageBonemealParticles()
    {
        this(BlockPos.ZERO);
    }

    @Nonnull
    protected final BlockPos pos;

    public MessageBonemealParticles(@Nonnull BlockPos pos)
    {
        this.pos = pos;
    }

    MessageBonemealParticles(@Nonnull PacketBuffer buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
    }

    void encode(PacketBuffer buf)
    {
        buf.writeLong(pos.toLong());
    }

    void handle(Supplier<NetworkEvent.Context> ctx)
    {
        if (TreeSimulator.COMMON_CONFIGS.showParticles.get()) {
            ctx.get().enqueueWork(() ->
            {
                PlayerEntity entity = DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().player);
                Block block = entity.getEntityWorld().getBlockState(pos).getBlock();
                if (block instanceof SaplingBlock)
                {
                    World entWorld = entity.getEntityWorld();
                    for (int c = 0; c < 20; c++)
                    {
                        entWorld.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + entWorld.rand.nextDouble(), pos.getY() + entWorld.rand.nextDouble(),
                                pos.getZ() + entWorld.rand.nextDouble(), 0f, 0f, 0f);
                    }
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
