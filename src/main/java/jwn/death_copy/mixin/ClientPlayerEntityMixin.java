package jwn.death_copy.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Unique
	private boolean hasCopied = false;

	@Inject(method = "updatePostDeath", at = @At("HEAD"))
	private void onPostDeath(CallbackInfo ci) {
		if (!hasCopied) {
			ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;
			Vec3d pos = player.getPos();
			float yaw = player.getYaw();
			float pitch = player.getPitch();

			// 현재 차원 구하기
			RegistryKey<World> dimension = player.getWorld().getRegistryKey();
			String dim = switch (dimension.getValue().getPath()) {
				case "the_nether" -> "minecraft:the_nether";
				case "the_end" -> "minecraft:the_end";
				default -> "minecraft:overworld";
			};

			String cmd = String.format(
					"/execute in %s run tp @s %.3f %.3f %.3f %.1f %.1f",
					dim, pos.x, pos.y, pos.z, yaw, pitch);
			MinecraftClient.getInstance().keyboard.setClipboard(cmd);

			hasCopied = true;
		}
	}

}