/*
 * see license.txt 
 */
package seventh.client.weapon;

import seventh.client.ClientPlayerEntity;
import seventh.client.gfx.Art;

/**
 * @author Tony
 *
 */
public class ClientRocketLauncher extends ClientWeapon {

	/**
	 * @param ownerId
	 */
	public ClientRocketLauncher(ClientPlayerEntity owner) {
		super(owner);
	
		this.weaponIcon = Art.rocketIcon;
		this.weaponImage = Art.rpgImage;
	//	this.muzzleFlash = Art.newRocketMuzzleFlash();
		
		this.endFireKick = 280;		
	}

}
