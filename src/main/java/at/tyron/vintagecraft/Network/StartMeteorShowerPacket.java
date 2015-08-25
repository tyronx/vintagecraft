package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class StartMeteorShowerPacket implements IMessage {

	int duration;
	
	
	public StartMeteorShowerPacket() {
	}
	
	public StartMeteorShowerPacket(int duration) {
		this.duration = duration;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		duration = buf.readInt();;

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(duration);
	}
	
	public static class ClientHandler implements IMessageHandler<StartMeteorShowerPacket, IMessage> {

		@Override
		public IMessage onMessage(StartMeteorShowerPacket message, MessageContext ctx) {
			VintageCraft.proxy.meteorShowerDuration = message.duration;
			return null;
		}
	}

}
