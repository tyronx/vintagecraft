package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class ChunkPutNbt extends AbstractPacket {
	NBTTagCompound chunknbt;
	long index;
	
	public ChunkPutNbt() {}
	
	public ChunkPutNbt(long index, NBTTagCompound chunknbt) {
		this.chunknbt = chunknbt;
		this.index = index;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		pb.writeLong(index);
		//pb.writeInt(chunkZ);
		//pb.writel
		try {
			pb.writeNBTTagCompoundToBuffer(chunknbt);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		PacketBuffer pb = new PacketBuffer(buffer);
		//chunkX = pb.readInt();
		//chunkZ = pb.readInt();
		index = pb.readLong();
		try {
			chunknbt = pb.readNBTTagCompoundFromBuffer();
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		VintageCraft.proxy.putChunkNbt(index, chunknbt);

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

}
