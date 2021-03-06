/*
 * see license.txt 
 */
package seventh.game.net;

import harenet.IOBuffer;
import seventh.game.Entity.State;
import seventh.game.Entity.Type;

/**
 * This is a partial message for other entities that are NOT the local
 * player.  
 * 
 * @author Tony
 *
 */
public class NetPlayerPartial extends NetEntity {

	
	public NetPlayerPartial() {
		this.type = Type.PLAYER_PARTIAL.netValue();
	}

	public byte state;
	public byte health;
	public NetWeapon weapon;
	
	public boolean isOperatingVehicle;
	public byte vehicleId;
	
	/* (non-Javadoc)
	 * @see seventh.game.net.NetEntity#read(java.nio.ByteBuffer)
	 */
	@Override
	public void read(IOBuffer buffer) {	
		super.read(buffer);
				
		orientation = buffer.getShort();
		state = buffer.get();
		health = buffer.get();
		
		/* If this player is in a vehicle,
		 * send the vehicle ID in lieu of 
		 * weapon information
		 */
		State aState = State.fromNetValue(state);
		if(aState.isVehicleState()) {
			isOperatingVehicle = true;
			vehicleId = buffer.get();
		}
		else {			
			readWeapon(buffer);
		}
	}
	
	/**
	 * Reads in the {@link NetWeapon}
	 * @param buffer
	 */
	protected void readWeapon(IOBuffer buffer) {
		weapon = new NetWeapon();
		weapon.type = buffer.get();
		weapon.state = buffer.get();
	}
	
	/* (non-Javadoc)
	 * @see seventh.game.net.NetEntity#write(java.nio.ByteBuffer)
	 */
	@Override
	public void write(IOBuffer buffer) {	
		super.write(buffer);
				
		buffer.putShort(orientation);
		buffer.put(state);		
		buffer.put(health);
		

		/* If this player is in a vehicle,
		 * send the vehicle ID in lieu of 
		 * weapon information
		 */
		State aState = State.fromNetValue(state);
		if(aState.isVehicleState()) {
			buffer.put(vehicleId);
		}
		else {			
			writeWeapon(buffer);
		}
	}
	
	/**
	 * Writes out the {@link NetWeapon}
	 * @param buffer
	 */
	protected void writeWeapon(IOBuffer buffer) {
		if(weapon != null) {
			buffer.put(weapon.type);
			buffer.put(weapon.state);
		}
		else {
			buffer.put( (byte)-1);
			buffer.put( (byte)0);
		}
	}
}
