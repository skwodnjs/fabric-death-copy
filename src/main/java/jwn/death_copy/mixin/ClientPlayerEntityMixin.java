package jwn.death_copy.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
	@Unique
	private boolean hasCopied = false;

	@Inject(method = "updatePostDeath", at = @At("HEAD"))
	private void onPostDeath(CallbackInfo ci) {
		if (!hasCopied) {
			ClientPlayerEntity player = (ClientPlayerEntity)(Object)this;Vec3d pos = player.getPos();
			String cmd = String.format("/execute in minecraft:overworld run tp @s %.3f %.3f %.3f",
					pos.x, pos.y, pos.z);
			MinecraftClient.getInstance().keyboard.setClipboard(cmd);

			hasCopied = true;
		}
	}
}