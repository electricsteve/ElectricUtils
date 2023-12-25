package electricsteve.electricutils.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ExampleClientMixin {
	@Inject(at = @At("HEAD"), method = "onPlaySound", cancellable = true)
	private void onPlaySoundMixin(PlaySoundS2CPacket packet, CallbackInfo ci) {
		Boolean bl = (packet.getX() > 1 && packet.getX() < 2);
		System.out.println(packet.getX());
		if (bl) {
			ci.cancel();
		}
	}
}