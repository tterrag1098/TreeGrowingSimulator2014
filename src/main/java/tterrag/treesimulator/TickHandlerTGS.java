package tterrag.treesimulator;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.block.SaplingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class TickHandlerTGS
{
	private Map<UUID, Integer> counters = new HashMap<>();
	private Map<UUID, PlayerState> states = new HashMap<>();

	public enum PlayerState
	{
		CROUCHED, STANDING;

		public static PlayerState getState(boolean bool)
		{
			return bool ? CROUCHED : STANDING;
		};
	};
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
	    Integer temp = counters.get(event.player.getGameProfile().getId());
	    int movementCounter = temp == null ? 0 : temp;
	    
		if (event.phase == Phase.END && !event.player.getEntityWorld().isRemote)
		{
			PlayerEntity player = (PlayerEntity) event.player;
			if (player.getEntityWorld().getGameTime() % 5 == 0)
			{
				List<BlockPos> coords = getNearestBlocks(player.getEntityWorld(), new BlockPos(player));

				if (coords.size() == 0)
				{
					return;
				}

				if (player.isSprinting())
				{
					movementCounter++;
	                sendPlayerPacket(player, coords);
				}
				if (PlayerState.getState(player.isCrouching()) != getState(player))
				{
					movementCounter++;
                    sendPlayerPacket(player, coords);
				}
				if (movementCounter > TreeSimulator.SERVER_CONFIGS.waitTime.get())
				{
					if (coords.size() == 0)
					{
						movementCounter--;
						return;
					}
					
					Collections.shuffle(coords);

					for (BlockPos pos : coords)
					{
						Block block = player.getEntityWorld().getBlockState(pos).getBlock();

						if (block instanceof SaplingBlock)
						{
							BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), player.getEntityWorld(), pos, player);

							if (TreeSimulator.COMMON_CONFIGS.showParticles.get())
								sendBonemealPacket(player, pos);

							break;
						}
					}
					movementCounter = 0;
				}

    			states.put(player.getGameProfile().getId(), PlayerState.getState(player.isCrouching()));
    			counters.put(event.player.getGameProfile().getId(), movementCounter);
			}
		}
	}

    private PlayerState getState(PlayerEntity player) 
	{
	    UUID user = player.getGameProfile().getId();
	    if (!states.containsKey(user)) {
	        states.put(user, PlayerState.getState(player.isCrouching()));
	    }
	    return states.get(user);
	}

	private void sendBonemealPacket(PlayerEntity causedBy, BlockPos pos)
	{
		PacketHandlerTGS.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> causedBy), new MessageBonemealParticles(pos));
	}

    private void sendPlayerPacket(PlayerEntity player, List<BlockPos> coords) {
        if (TreeSimulator.COMMON_CONFIGS.allTheParticles.get()) {
            for (BlockPos pos : coords) {
                PacketHandlerTGS.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player), new MessagePlayerParticle(player, pos));
            }
        }
    }

	private List<BlockPos> getNearestBlocks(World world, BlockPos pos)
    {
        return BlockPos.getAllInBox(pos.add(-5, -2, -5), pos.add(5, 2, 5))
                .filter(p -> world.getBlockState(p).getBlock() instanceof SaplingBlock)
                .map(BlockPos::toImmutable)
                .collect(Collectors.toList());
    }
}
