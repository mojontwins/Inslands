package net.minecraft.game.world.block.tileentity;

import com.mojang.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.game.world.World;

public class TileEntity {
	private static Map nameToClassMap = new HashMap();
	private static Map classToNameMap = new HashMap();
	public World worldObj;
	public int xCoord;
	public int yCoord;
	public int zCoord;

	private static void addMapping(Class class0, String string1) {
		nameToClassMap.put(string1, class0);
		classToNameMap.put(class0, string1);
	}

	public void readFromNBT(NBTTagCompound nBTTagCompound1) {
		this.xCoord = nBTTagCompound1.getInteger("x");
		this.yCoord = nBTTagCompound1.getInteger("y");
		this.zCoord = nBTTagCompound1.getInteger("z");
	}

	public void writeToNBT(NBTTagCompound nBTTagCompound1) {
		nBTTagCompound1.setString("id", (String)classToNameMap.get(this.getClass()));
		nBTTagCompound1.setInteger("x", this.xCoord);
		nBTTagCompound1.setInteger("y", this.yCoord);
		nBTTagCompound1.setInteger("z", this.zCoord);
	}

	public void updateEntity() {
	}

	public static TileEntity createAndLoadEntity(NBTTagCompound nBTTagCompound0) {
		TileEntity tileEntity1 = null;

		try {
			Class class2;
			if((class2 = (Class)nameToClassMap.get(nBTTagCompound0.getString("id"))) != null) {
				tileEntity1 = (TileEntity)class2.newInstance();
			}
		} catch (Exception exception3) {
			exception3.printStackTrace();
		}

		if(tileEntity1 != null) {
			tileEntity1.readFromNBT(nBTTagCompound0);
		} else {
			System.out.println("Skipping TileEntity with id " + nBTTagCompound0.getString("id"));
		}

		return tileEntity1;
	}

	static {
		addMapping(TileEntityFurnace.class, "Furnace");
		addMapping(TileEntityChest.class, "Chest");
	}
}