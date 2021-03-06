/*
 * The Seventh
 * see license.txt 
 */
package seventh.client.weapon;

import seventh.client.ClientPlayerEntity;
import seventh.client.gfx.Art;

/**
 * @author Tony
 *
 */
public class ClientKar98 extends ClientWeapon {

	/**
	 * @param owner
	 */
	public ClientKar98(ClientPlayerEntity owner) {
		super(owner);
		
		this.weaponIcon = Art.kar98Icon;
		this.weaponImage = Art.kar98Image;
		this.muzzleFlash = Art.newKar98MuzzleFlash();
	}

}
