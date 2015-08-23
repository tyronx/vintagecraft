package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChunkRemoveNbtPacket implements IMessage {
	long index;
	
	public ChunkRemoveNbtPacket() {}
	
	public ChunkRemoveNbtPacket(long index) {
		this.index = index;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readLong();	
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(index);
	}
	
	public static class Handler implements IMessageHandler<ChunkRemoveNbtPacket, IMessage> {
		@Override
		public IMessage onMessage(ChunkRemoveNbtPacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				VintageCraft.proxy.removeChunkNbt(message.index);
			}
			return null;
		}
	}
}
