package at.tyron.vintagecraft.Network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class SoundEffectToServerPacket extends AbstractPacket {
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
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		pb.writeDouble(x);
		pb.writeDouble(y);
		pb.writeDouble(z);
		pb.writeString(sound);
		pb.writeFloat(pitch);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		x = pb.readDouble();
		y = pb.readDouble();
		z = pb.readDouble();
		sound = pb.readStringFromBuffer(100);
		pitch = pb.readFloat();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		player.worldObj.playSoundToNearExcept(player, sound, 1f, pitch);
		
		// This works:
		//player.worldObj.playSoundAtEntity(player, sound, 1f, 1f);
	}

}
