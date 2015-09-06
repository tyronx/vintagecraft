package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.World.MechanicalNetwork;
import at.tyron.vintagecraft.World.MechnicalNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MechanicalNetworkNBTPacket implements IMessage {
	NBTTagCompound nbt;
	int networkId;
	
	
	public MechanicalNetworkNBTPacket() {

	}
	
	public MechanicalNetworkNBTPacket(NBTTagCompound nbt, int networkid) {
		this.nbt = nbt;
		this.networkId = networkid;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		nbt = ByteBufUtils.readTag(buf);
		networkId = buf.readInt();
		
	/*	nbt = new NBTTagCompound();
		nbt.setInteger("networkId", 1);
		nbt.setFloat("totalAvailableTorque", 0);
		nbt.setFloat("totalResistance", 1);
		nbt.setFloat("speed", 0);
		nbt.setInteger("direction", 1);
		nbt.setFloat("angle", 1);
		
		nbt.setInteger("firstNodeX", 1);
		nbt.setInteger("firstNodeY", 1);
		nbt.setInteger("firstNodeZ", 1);
*/
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, nbt);
		buf.writeInt(networkId);
	}
	
	
	public static class ClientHandler implements IMessageHandler<MechanicalNetworkNBTPacket, IMessage> {

		@Override
		public IMessage onMessage(MechanicalNetworkNBTPacket message, MessageContext ctx) {
			//System.out.println("got package");
			
			if (VintageCraft.proxy.getClientWorld() == null) {
				System.out.println("packet sent to a null world?!");
				return null;
			}
			
			MechnicalNetworkManager manager = MechnicalNetworkManager.getNetworkManagerForWorld(VintageCraft.proxy.getClientWorld());
			
			if (manager == null) {
				manager = MechnicalNetworkManager.addManager(VintageCraft.proxy.getClientWorld(), null);
			}
			
			MechanicalNetwork network = manager.getNetworkById(message.networkId);
			boolean isnew = network == null;
			
			network = manager.getOrCreateNetwork(message.networkId);
			network.readFromNBT(message.nbt, !isnew);
			
			
			//System.out.println(message.nbt);
			return null;
		}
	}

}
