package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class WorldDataPacket implements IMessage {
	long seed;
	
	public WorldDataPacket() {
	}
	
	public WorldDataPacket(long seed) {
		this.seed = seed;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		seed = buf.readLong();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeLong(seed);

	}
	
	public static class ClientHandler implements IMessageHandler<WorldDataPacket, IMessage> {

		@Override
		public IMessage onMessage(WorldDataPacket message, MessageContext ctx) {
			VintageCraft.proxy.worldSeed = message.seed;
			return null;
		}
	}



}
