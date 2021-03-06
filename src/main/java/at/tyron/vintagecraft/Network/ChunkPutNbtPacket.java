package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ChunkPutNbtPacket implements IMessage {
	NBTTagCompound chunknbt;
	long index;
	
	public ChunkPutNbtPacket() {}
	
	public ChunkPutNbtPacket(long index, NBTTagCompound chunknbt) {
		this.chunknbt = chunknbt;
		this.index = index;
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		index = buf.readLong();
		chunknbt = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(index);
		ByteBufUtils.writeTag(buf, chunknbt);
	}

	public static class Handler implements IMessageHandler<ChunkPutNbtPacket, IMessage> {

		@Override
		public IMessage onMessage(ChunkPutNbtPacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				VintageCraft.proxy.putChunkNbt(message.index, message.chunknbt);
			}
			return null;
		}
	}
	
}
