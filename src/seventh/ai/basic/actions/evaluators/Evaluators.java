/*
 * see license.txt 
 */
package seventh.ai.basic.actions.evaluators;

import seventh.game.PlayerEntity;
import seventh.game.weapons.Weapon;

/**
 * @author Tony
 *
 */
public class Evaluators {

	/**
	 * Health score between 0 and 1
	 * @param ent
	 * @return
	 */
	public static double healthScore(PlayerEntity ent) {
		return (double) ent.getHealth() / (double) ent.getMaxHealth();
	}
	
	public static double weaponStrengthScore(Weapon weapon) {
		double score = 0;
		if ( weapon != null ) {
			switch(weapon.getType()) {
				case PISTOL:
					score = 0.2;
					break;	
				case ROCKET_LAUNCHER:
					score = 0.5;
					break;	
				case MP40:
					score = 0.7;
					break;
				case THOMPSON:
					score = 0.7;
					break;					
				case KAR98:
					score = 0.8;
					break;
				case M1_GARAND:
					score = 0.8;
					break;				
				case MP44:
					score = 0.8;
					break;				
				case RAILGUN:
					score = 0.8;
					break;
				case RISKER:
					score = 0.8;
					break;				
				case SHOTGUN:
					score = 0.8;
					break;
				case SPRINGFIELD:
					score = 0.8;
					break;						
				default:
					break;		
			}
		}
		return score;
	}
	
	/**
	 * Current weapon score (only accounts for bullets in clip)
	 * @param ent
	 * @return
	 */
	public static double currentWeaponAmmoScore(PlayerEntity ent) {
		
		double score = 0;
		Weapon weapon = ent.getInventory().currentItem();
		score = weaponAmmoScore(weapon);
		
		return score;
	}
	
	/**
	 * Current weapon score (only accounts for bullets in clip)
	 * @param ent
	 * @return
	 */
	public static double weaponAmmoScore(Weapon weapon) {
		
		double score = 0;
		if(weapon != null) {			
			return (double)weapon.getBulletsInClip() / (double)weapon.getClipSize();			
		}
		
		return score;
	}
	
	
	public static double weaponDistanceScore(PlayerEntity ent, PlayerEntity enemy) {
		double score = 0;
		Weapon weapon = ent.getInventory().currentItem();
		if(weapon != null) {
			float distanceAwaySq = ent.distanceFromSq(enemy);
			double bulletRangeSq = ent.getCurrentWeaponDistanceSq();
			if(bulletRangeSq > 0 && distanceAwaySq < bulletRangeSq) {
				score = 0.75;
			
				double distanceScore = distanceAwaySq / bulletRangeSq;
												
				/* lower the score if they are too close */
				if(distanceScore < 0.15) {
					distanceScore *= 0.60;
				}
				
				/* lower the score if they are too far away */
				else if(distanceScore > 0.85) {
					distanceScore *= 0.80;
				}
				
				
				score *= distanceScore;
			}
			
			
		}
		
		return score;
	}
}
