package at.tyron.vintagecraft.Network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SoundEffectToServerPacket implements IMessage {
	double x;
	double y;
	double z;
	
	String sound;
	
	float pitch;
	
	
	public SoundEffectToServerPacket() {
	}
	
	public SoundEffectToServerPacket(double x, double y, double z, String sound, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.sound = sound;
		this.pitch = pitch;
	}
	
	


	@Override
	public void fromBytes(ByteBuf buf) {
		/*x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();*/
		int len = buf.readInt();
		sound = buf.readBytes(len).toString();
		pitch = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		/*buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);*/
		buf.writeInt(sound.length());
		buf.writeBytes(sound.getBytes());
		
		buf.writeFloat(pitch);
	}

	
	public static class Handler implements IMessageHandler<SoundEffectToServerPacket, IMessage> {

		@Override
		public IMessage onMessage(SoundEffectToServerPacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) return null;
			
			ctx.getServerHandler().playerEntity.worldObj.playSoundToNearExcept(ctx.getServerHandler().playerEntity, message.sound, 1f, message.pitch);
			return null;
		}
		
	}
	
}
