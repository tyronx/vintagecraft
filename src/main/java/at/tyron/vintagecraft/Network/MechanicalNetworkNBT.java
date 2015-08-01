package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MechanicalNetworkNBT implements IMessage {
	NBTTagCompound nbt;
	int networkId;
	
	
	public MechanicalNetworkNBT() {

	}
	
	public MechanicalNetworkNBT(NBTTagCompound nbt, int networkid) {
		this.nbt = nbt;
		this.networkId = networkid;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
		networkId = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
		buf.writeInt(networkId);
	}
	
	
	public static class ClientHandler implements IMessageHandler<MechanicalNetworkNBT, IMessage> {

		@Override
		public IMessage onMessage(MechanicalNetworkNBT message, MessageContext ctx) {
			//System.out.println("got package");
			MechanicalNetwork network = MechanicalNetwork.getOrCreateNetwork(message.networkId);
			network.readFromNBT(message.nbt);
			return null;
		}
	}

}
