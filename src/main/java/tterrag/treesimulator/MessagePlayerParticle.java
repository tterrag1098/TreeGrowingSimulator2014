package tterrag.treesimulator;

import java.util.function.Supplier;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class MessagePlayerParticle extends MessageBonemealParticles {

    private final int playerID;
    
    public MessagePlayerParticle() {
        this.playerID = 0;
    }
    
    public MessagePlayerParticle(@Nonnull PlayerEntity player, @Nonnull BlockPos pos) {
        super(pos);
        this.playerID = player.getEntityId();
    }
    
    MessagePlayerParticle(PacketBuffer buf) {
        super(buf);
        this.playerID = buf.readInt();
    }
    
    @Override
    void encode(PacketBuffer buf)
    {
        super.encode(buf);
        buf.writeInt(playerID);
    }
    
    @Override
    void handle(Supplier<Context> ctx)
    {
        if (TreeSimulator.COMMON_CONFIGS.allTheParticles.get()) {
            ctx.get().enqueueWork(() -> {
                PlayerEntity player = (PlayerEntity) DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> Minecraft.getInstance().world.getEntityByID(playerID));
                if (player == null) {
                    return;
                }
                
                World world = player.getEntityWorld();
                
                Vec3d spawnPos = new Vec3d(
                        player.getPosX() + (world.rand.nextGaussian() * 0.25), 
                        player.getPosY() + (world.rand.nextGaussian() * 0.75) + 0.8,
                        player.getPosZ() + (world.rand.nextGaussian() * 0.25)
                    );
                Vec3d endPos = new Vec3d(pos).add((world.rand.nextGaussian() * 0.3) + 0.5, (world.rand.nextGaussian() * 0.25) + 0.3, (world.rand.nextGaussian() * 0.3) + 0.5);
                
                Vec3d vel = spawnPos.subtract(endPos).scale(-0.04);
                
                Particle p = Minecraft.getInstance().worldRenderer.addParticleUnchecked(ParticleTypes.HAPPY_VILLAGER, true, spawnPos.x, spawnPos.y, spawnPos.z, vel.x, vel.y, vel.z);
                p.motionX = vel.x;
                p.motionY = vel.y;
                p.motionZ = vel.z;
                p.canCollide = true;
                p.setMaxAge(25 + world.rand.nextInt(5));
            });
        }
        ctx.get().setPacketHandled(true);
    }
}
