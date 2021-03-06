/*
 *	leola-live 
 *  see license.txt
 */
package seventh.map;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Layer} in a {@link Map}.
 * 
 * @author Tony
 *
 */
public class Layer {

	/**
	 * Underlying layer
	 */
	private List< List<Tile> > layer;
	
	/**
	 * Collidables
	 */
	private boolean canCollide;
	
	/**
	 * If its a foreground layers
	 */
	private boolean isForeground;
	
	/**
	 * Light layer
	 */
	private boolean isLightLayer;
	
	/**
	 * If this is a property layer and 
	 * doesn't have render data
	 */
	private boolean isPropertyLayer;
	
	
	private int index;
	private int heightMask;
	
	/**
	 * Constructs a {@link Layer}.
	 */
	public Layer(boolean collidable, boolean isForeground, boolean isLightLayer, int index, int heightMask) {
		this.layer = new ArrayList<List<Tile>>();
		this.canCollide = collidable;
		this.isForeground = isForeground;
		this.isLightLayer = isLightLayer;
		this.isPropertyLayer = isLightLayer || collidable;
		this.index = index;
		this.heightMask = heightMask;
	}
	
	/**
	 * @return the heightMask
	 */
	public int getHeightMask() {
		return heightMask;
	}
		
	/**
	 * Applies the height mask to this layer
	 */
	public void applyHeightMask() {
		for(List<Tile> row : this.layer) {
			int size = row.size();
			for(int i = 0; i < size;i++) {
				Tile t = row.get(i);
				if(t!=null) {
					t.setHeightMask(heightMask);					
				}
			}
		}
	}
	
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * If the tiles on this layer are collidable.
	 * 
	 * @return
	 */
	public boolean collidable() {
		return this.canCollide;
	}
	
	/**
	 * @return the isForeground
	 */
	public boolean isForeground() {
		return isForeground;
	}
	
	/**
	 * @return the isLightLayer
	 */
	public boolean isLightLayer() {
		return isLightLayer;
	}
	
	/**
	 * @return the isPropertyLayer
	 */
	public boolean isPropertyLayer() {
		return isPropertyLayer;
	}

	/**
	 * @param isForeground the isForeground to set
	 */
	public void setForeground(boolean isForeground) {
		this.isForeground = isForeground;
	}

	/**
	 * The number of rows in this layer.
	 * 
	 * @return
	 */
	public int numberOfRows() {
		return this.layer.size();
	}
	/**
	 * Get a row in the layer.
	 * 
	 * @param i
	 * @return
	 */
	public List<Tile> getRow(int i) {
		return this.layer.get(i);
	}
	
	/**
	 * Add a row.
	 * 
	 * @param row
	 */
	public void addRow(List<Tile> row) {
		this.layer.add(row);
		int size = row.size();
		for(int i = 0; i < size;i++) {
			Tile t = row.get(i);
			if(t!=null) {
				t.setHeightMask(heightMask);
			}
		}
	}
}
