package at.tyron.vintagecraft.TileEntity;

import at.tyron.vintagecraft.VintageCraft;
import at.tyron.vintagecraft.Network.AbstractPacket;
import at.tyron.vintagecraft.Network.DataBlockPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class NetworkTileEntity extends TileEntity {
	public boolean shouldSendInitData = true;
	public EntityPlayer entityplayer;
	protected int broadcastRange = 256;
	
	/**
	 * Create an initialization packet to be sent when the block loads.
	 * @param nbt
	 */
	public abstract void handleInitPacket(NBTTagCompound nbt);
	public void handleDataPacket(NBTTagCompound nbt){}
	public void createDataNBT(NBTTagCompound nbt){}
	public abstract void createInitNBT(NBTTagCompound nbt);

	public DataBlockPacket createDataPacket() {
		return this.createDataPacket(createDataNBT());
	}

	public DataBlockPacket createDataPacket(NBTTagCompound nbt) {
		return new DataBlockPacket(pos.getX(), pos.getY(), pos.getZ(), nbt);
	}

	private NBTTagCompound createDataNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		createDataNBT(nbt);
		return nbt;
	}

	private NBTTagCompound createInitNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		createInitNBT(nbt);
		return nbt;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public double getMaxRenderDistanceSquared() {
		return 1024.0D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bb = AxisAlignedBB.fromBounds(pos.getX(), pos.getY(), pos.getZ(), pos.getX() +1, pos.getY() + 1, pos.getZ() + 1);
		return bb;
	}

	@Override
	public Packet getDescriptionPacket() {
		if(shouldSendInitData)
			return new S35PacketUpdateTileEntity(pos, 1, createInitNBT());
		return null;
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		handleInitPacket(pkt.getNbtCompound());
		worldObj.markBlockForUpdate(pos);
	}

	public void broadcastPacketInRange() {
		int dim = worldObj.provider.getDimensionId();
		if(worldObj.isRemote) {
			VintageCraft.packetPipeline.sendToServer(this.createDataPacket());
		} else {
			VintageCraft.packetPipeline.sendToAllAround(this.createDataPacket(), 
					new TargetPoint(dim, pos.getX(), pos.getY(), pos.getZ(), broadcastRange));
		}
	}

	public void broadcastPacketInRange(AbstractPacket packet) {
		if(worldObj.isRemote) {
			VintageCraft.packetPipeline.sendToServer(packet);
		} else {
			VintageCraft.packetPipeline.sendToAllAround(packet, 
					new TargetPoint(worldObj.provider.getDimensionId(), pos.getX(), pos.getY(), pos.getZ(), broadcastRange));
		}
	}

}
