package com.mojontwins.minecraft.blockmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.github.kasparnagu.json.JSONParser;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public class BlockModel {

	private Map<?, ?> theBlockJson;
	private Map<Integer,Integer> textureMap;
	private List<BlockElement> blockElements;

	public double[] minX = new double[4];
	public double[] maxX = new double[4];
	
	public double minY = 1.0D;
	public double maxY = 0.0D;
	
	public double[] minZ = new double[4];
	public double[] maxZ = new double[4];
	
	public double uvMultiplier = 1.0D;
	
	
	/*
	 * Will parse standard block models that are only made of boxes.
	 */
	
	public BlockModel(String pathSpec) {
		try {
			this.theBlockJson = (Map<?, ?>) JSONParser.parseJSONFile(pathSpec);
			this.setTextureMap(this.parseTextureMap());
			this.setBlockElements(this.parseElements());
		} catch (Exception e) {
			System.out.println("There was shittery parsing json!");
			e.printStackTrace();
		}
	}

	private Map<Integer, Integer> parseTextureMap() {
		Map<Integer,Integer> textureEntries = new HashMap<Integer,Integer> ();
		
		try {
			List<?> textureSize = (List<?>) this.theBlockJson.get("texture_size");
			
			if(textureSize != null) {
				this.uvMultiplier = (Long)textureSize.get(0) / 16.0D;
			}
			
		} catch (Exception e) {
			System.out.println("There was shittery getting texture size!");
			e.printStackTrace();
		}
		
		try {
			Map<?, ?> textures = (Map<?, ?>) this.theBlockJson.get("textures");
			
			Iterator<?> it = textures.keySet().iterator();
			while(it.hasNext()) {
				String index = (String) it.next();
				String value = (String) textures.get(index);
				
				// -1 means "override, -2 means absolute terrain.
				
				if("particle".equals(index)) {
					
				} else if("terrain".equals(value)) {
					textureEntries.put(Integer.parseInt(index), -2);
				} else {
					textureEntries.put(Integer.parseInt(index), Integer.parseInt(value));
				}
				
			}
		} catch (Exception e) {
			System.out.println("There was shittery getting texture indexes!");
			e.printStackTrace();
		}
		
		return textureEntries;
	}

	private List<BlockElement> parseElements() {
		List<BlockElement> blockElements = new ArrayList<BlockElement> ();
		
		try {
			for(int i = 0; i < 4; i ++) {
				minX[i] = 1.0D; maxX[i] = 0.0D;
				minZ[i] = 1.0D; maxZ[i] = 0.0D;
			}
			
			@SuppressWarnings("unchecked")
			List<Map<?, ?>> elements = (List<Map<?, ?>>) this.theBlockJson.get("elements");
			
			for(Map<?, ?> element : elements) {
				BlockElement blockElement = new BlockElement();
				
				blockElement.withFromTo(
						list2Vec3D((List<?>) element.get("from")),
						list2Vec3D((List<?>) element.get("to"))
					);
				
				// Update precalculated block bounds
				if(blockElement.from[0].yCoord < this.minY) this.minY = blockElement.from[0].yCoord;
				if(blockElement.to[0].yCoord > this.maxY) this.maxY = blockElement.to[0].yCoord;
				
				double d;
				for(int i = 0; i < 4; i ++) {
					if((d = blockElement.getMinX(i)) < this.minX[i]) this.minX[i] = d;
					if((d = blockElement.getMinZ(i)) < this.minZ[i]) this.minZ[i] = d;
					if((d = blockElement.getMaxX(i)) > this.maxX[i]) this.maxX[i] = d;
					if((d = blockElement.getMaxZ(i)) > this.maxZ[i]) this.maxZ[i] = d;
				}
				
				Map<?, ?> faces = (Map<?, ?>) element.get("faces");
				
				Iterator<?> it = faces.keySet().iterator();
				while(it.hasNext()) {
					String faceName = (String)it.next();
					
					Map<?, ?> face = (Map<?, ?>) faces.get(faceName);
					List<?> uv = (List<?>) face.get("uv");
					
					int rotation = 0;
					Object r = face.get("rotation");
					if(r != null) rotation = (Integer)r;
					
					BlockFaceUV blockFaceUV = new BlockFaceUV(
							new int[] {
								Integer.valueOf((int) Math.floor(this.getNumericalFrom(uv, 0) * this.uvMultiplier)),
								Integer.valueOf((int) Math.floor(this.getNumericalFrom(uv, 1) * this.uvMultiplier)),
								Integer.valueOf((int) Math.floor(this.getNumericalFrom(uv, 2) * this.uvMultiplier)),
								Integer.valueOf((int) Math.floor(this.getNumericalFrom(uv, 3) * this.uvMultiplier))
							},
							rotation
					);
					
					String textureRef = (String) face.get("texture");
					
					if(textureRef == null || textureRef.equals("missing") || textureRef.equals("#missing")) {
						
					} else {
						int texture = this.resolveTexture(textureRef);
						
						BlockFace blockFace = new BlockFace(blockFaceUV, texture, BlockFace.facing2Index(faceName));	
						blockElement.withFace(faceName, blockFace);
					}
				}
				
				blockElements.add(blockElement);
 			}

		} catch (Exception e) {
			System.out.println ("There as shittery reading the list of elements");
			e.printStackTrace();
		}
		return blockElements;
	}

	private double getNumericalFrom(List<?> list, int i) {
		// list may contain Double or Long
		Object v = list.get(i);
		
		if(v.getClass().isAssignableFrom(Double.class)) return ((Double) v).doubleValue();
		if(v.getClass().isAssignableFrom(Long.class)) return (double) ((Long) v).longValue();
		
		return 0D;
	}

	private int resolveTexture(String tex) {
		int res = -1;
		
		try {
			if(tex.startsWith("#")) tex = tex.substring(1);
			res = this.textureMap.get(Integer.parseInt(tex));
		} catch (Exception e) {
			System.out.println("There was shittery parsing a texture index \"" + tex + "\""); 
			e.printStackTrace();
		}
		
		return res;
	}

	private Vec3D list2Vec3D(List<?> arrayNode) {
		return new Vec3D(	
				Integer.valueOf(((Long)arrayNode.get(0)).intValue()) / 16.0D,
				Integer.valueOf(((Long)arrayNode.get(1)).intValue()) / 16.0D,
				Integer.valueOf(((Long)arrayNode.get(2)).intValue()) / 16.0D
		);
	}

	public Map<Integer,Integer> getTextureMap() {
		return textureMap;
	}

	public void setTextureMap(Map<Integer,Integer> textureMap) {
		this.textureMap = textureMap;
	}

	public List<BlockElement> getBlockElements() {
		return blockElements;
	}

	public void setBlockElements(List<BlockElement> blockElements) {
		this.blockElements = blockElements;
	}
	
	public String toString() {
		String s = "BlockModel\n";
		
		s += "TextureMap (";
		Iterator<Integer> it = this.textureMap.keySet().iterator();
		while(it.hasNext()) {
			int key = it.next();
			s += "[" + key + "=" + this.textureMap.get(key) + "]";
		}

		s += ")\nElements (\n";
		
		Iterator<BlockElement> itE = this.blockElements.iterator();
		while(itE.hasNext()) s += itE.next().toString() + "\n";
		
		s += ")";
		
		return s;
	}
	
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AxisAlignedBB aabb, ArrayList<AxisAlignedBB> collidingBoundingBoxes) {
		Iterator<BlockElement> iterator = this.blockElements.iterator();
		int facing = world.getBlockMetadata(x, y, z);
		
		while(iterator.hasNext()) {
			BlockElement blockElement = iterator.next();
			collidingBoundingBoxes.add(AxisAlignedBB.getBoundingBoxFromPool(
					(double)x + blockElement.getMinX(facing),
					(double)y + blockElement.from[facing].yCoord,
					(double)z + blockElement.getMinZ(facing),
					(double)x + blockElement.getMaxX(facing),
					(double)y + blockElement.to[facing].yCoord,
					(double)z + blockElement.getMaxZ(facing)
			));
		}
	}
	
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) { 
		int facing = world.getBlockMetadata(x, y, z);
		
		return AxisAlignedBB.getBoundingBoxFromPool(
				(double)x + this.minX[facing], (double)y + this.minY, (double)z + this.minZ[facing], 
				(double)x + this.maxX[facing], (double)y + this.maxY, (double)z + this.maxZ[facing]
		);
	}
}
