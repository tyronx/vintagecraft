package at.tyron.vintagecraft.Network;

import at.tyron.vintagecraft.Interfaces.IItemSmithable;
import at.tyron.vintagecraft.Interfaces.IItemWoodWorkable;
import at.tyron.vintagecraft.Inventory.ContainerAnvil;
import at.tyron.vintagecraft.Inventory.ContainerCarpenterTable;
import at.tyron.vintagecraft.World.Crafting.EnumWoodWorkingTechnique;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class CarpentryTechniquePacket implements IMessage {
	EnumWoodWorkingTechnique carpentryTechnique;
	
	public CarpentryTechniquePacket() {
	}
	
	public CarpentryTechniquePacket(EnumWoodWorkingTechnique carpentryTechnique) {
		this.carpentryTechnique = carpentryTechnique;
	}

	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		carpentryTechnique = EnumWoodWorkingTechnique.fromId(buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(carpentryTechnique.getId());
	}
	

    public static class Handler implements IMessageHandler<CarpentryTechniquePacket, IMessage> {
		@Override
		public IMessage onMessage(CarpentryTechniquePacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) return null;
			
			
			EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			
			if (player.openContainer instanceof ContainerCarpenterTable) {
				ContainerCarpenterTable container = (ContainerCarpenterTable) player.openContainer;
    			
    			ItemStack itemstack = container.getSlot(0).getStack();
    			if (itemstack == null || !(itemstack.getItem() instanceof IItemWoodWorkable)) {
    				container.detectAndSendChanges();
    				return null;
    			}
    			
    			IItemWoodWorkable workableItem = (IItemWoodWorkable)itemstack.getItem();	        
    	        
    	        player.addExhaustion(1F); 
    	        
    	        ItemStack carpenterToolBox = container.getSlot(3).getStack();
    	        
    	        
    	        if (carpenterToolBox != null) {
    	        	carpenterToolBox.damageItem(1, player);
    	        	
    	        	if (message.carpentryTechnique == EnumWoodWorkingTechnique.JOIN) {
    	        		carpenterToolBox.damageItem(2, player);
    	        	}

    	        	if (carpenterToolBox.stackSize == 0) {
    	        		container.getSlot(3).putStack(null);
    	        		player.worldObj.playSoundEffect(player.posX, player.posY, player.posZ, "random.break", 1f, 1f);
    	        	}
    	        }
    	        
    	        
    	        if (container.teTable.onTableUse(player)) {
    	        	workableItem.applyTechnique(itemstack, message.carpentryTechnique);
    	        	container.checkCraftable();	
    	        }
    		}
			
			return null;
		}
    }

}
