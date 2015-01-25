package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public class ChunkRemoveNbt extends AbstractPacket {
	long index;
	
	public ChunkRemoveNbt() {}
	
	public ChunkRemoveNbt(long index) {
		this.index = index;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		pb.writeLong(index);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		index = pb.readLong();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		VintageCraft.proxy.removeChunkNbt(index);
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		
	}

}
