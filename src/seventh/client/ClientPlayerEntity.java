/*
 * see license.txt 
 */
package seventh.client;

import seventh.client.gfx.Art;
import seventh.client.gfx.Camera;
import seventh.client.gfx.Canvas;
import seventh.client.gfx.Light;
import seventh.client.gfx.LightSystem;
import seventh.client.gfx.PlayerSprite;
import seventh.client.gfx.RenderFont;
import seventh.client.gfx.particle.BloodEmitter;
import seventh.client.sfx.Sounds;
import seventh.client.weapon.ClientKar98;
import seventh.client.weapon.ClientM1Garand;
import seventh.client.weapon.ClientMP40;
import seventh.client.weapon.ClientMP44;
import seventh.client.weapon.ClientPistol;
import seventh.client.weapon.ClientRisker;
import seventh.client.weapon.ClientRocketLauncher;
import seventh.client.weapon.ClientShotgun;
import seventh.client.weapon.ClientSpringfield;
import seventh.client.weapon.ClientThompson;
import seventh.client.weapon.ClientWeapon;
import seventh.game.Entity.State;
import seventh.game.Entity.Type;
import seventh.game.PlayerEntity;
import seventh.game.net.NetEntity;
import seventh.game.net.NetPlayer;
import seventh.game.net.NetPlayerPartial;
import seventh.game.net.NetWeapon;
import seventh.math.Vector2f;
import seventh.shared.TimeStep;
import seventh.shared.WeaponConstants;

/**
 * @author Tony
 *
 */
public class ClientPlayerEntity extends ClientControllableEntity {
	
	private final ClientWeapon[] WEAPONS = new ClientWeapon[10];
	
	private int health;	
	private int damageDelta;
	private int stamina;
		
	private long invinceableTime;
			
	private PlayerSprite sprite;
		
	private int fadeAlphaColor;
	private int teamColor;
	
	private ClientPlayer player;
	private ClientWeapon weapon;
	private byte numberOfGrenades;
		
	private int weaponWeight;
	
	private BloodEmitter bloodEmitter;
		
	private Light mussleFlash;
	
	
	/**
	 * 
	 */
	public ClientPlayerEntity(ClientGame game, ClientPlayer player, Vector2f pos) {
		super(game, pos);
		
		type = Type.PLAYER;
		changeTeam(ClientTeam.NONE);
			
		this.currentState = State.IDLE;	
		this.invinceableTime = 0;		
		this.lineOfSight = WeaponConstants.DEFAULT_LINE_OF_SIGHT;
		
		this.bloodEmitter = new BloodEmitter(new Vector2f());//, 3, 300000, -50001);
		this.bloodEmitter.stop();
				
		this.bounds.width = 24;//16;
		this.bounds.height = 24;
		
		LightSystem lightSystem = game.getLightSystem();				
		this.mussleFlash = lightSystem.newPointLight();
		this.mussleFlash.setTexture(Art.fireWeaponLight);
		this.mussleFlash.setColor(0.5f,0.5f,0.5f);
								
		setPlayer(player);
				
		setOnRemove(new OnRemove() {
			
			@Override
			public void onRemove(ClientEntity me, ClientGame game) {
				LightSystem lightSystem = game.getLightSystem();				
				lightSystem.removeLight(mussleFlash);
			}
		});
	}
	
	public void spawned() {
		this.invinceableTime = 2000;
	}
	
	/* (non-Javadoc)
	 * @see seventh.client.ClientEntity#killIfOutdated(long)
	 */
	@Override
	public boolean killIfOutdated(long gameClock) {	
		return false;
	}

	
	/**
	 * @param player the player to set
	 */
	public void setPlayer(ClientPlayer player) {
		this.player = player;
		if(player != null) {			
			WEAPONS[0] = new ClientThompson(this);
			WEAPONS[1] = new ClientShotgun(this);
			WEAPONS[2] = new ClientRocketLauncher(this);
			WEAPONS[3] = new ClientSpringfield(this);
			WEAPONS[4] = new ClientM1Garand(this);
			WEAPONS[5] = new ClientKar98(this);
			WEAPONS[6] = new ClientMP44(this);
			WEAPONS[7] = new ClientMP40(this);
			WEAPONS[8] = new ClientPistol(this);
			WEAPONS[9] = new ClientRisker(this);
			
			player.setEntity(this);
		}
	}
	
		
	/**
	 * @return the player
	 */
	public ClientPlayer getPlayer() {
		return player;
	}
	
	/**
	 * @return the mussleFlash
	 */
	public Light getMussleFlash() {
		return mussleFlash;
	}
	
	/**
	 * @return the bloodEmitter
	 */
	public BloodEmitter getBloodEmitter() {
		return bloodEmitter;
	}	
	
		
	/**
	 * @return the Players ID
	 */
	public int getPlayerId() {
		return this.player.getId();
	}
	
	/**
	 * @return the team
	 */
	public ClientTeam getTeam() {
		return this.player.getTeam();
	}
	
	/**
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}
	
	
	/**
	 * @return the weapon
	 */
	public ClientWeapon getWeapon() {
		return weapon;
	}
	
	public byte getNumberOfGrenades() {
		return this.numberOfGrenades;
	}
	
	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return stamina;
	}
	
	/**
	 * @param team
	 */
	public void changeTeam(ClientTeam team) {		
		switch(team) {			
			case AXIS: {
				this.sprite = new PlayerSprite(this, Art.axisBodyModel, Art.axisWalkModel, Art.axisCrouchLegs, Art.axisSprintModel);
				break;
			}
			case ALLIES:
			default: {
				this.sprite = new PlayerSprite(this, Art.alliedBodyModel, Art.alliedWalkModel, Art.alliedCrouchLegs, Art.alliedSprintModel);				
			}
		}
		
		this.teamColor = team.getColor();
	}
	
	
	
	/**
	 * @return calculates the movement speed based on
	 * state + current weapon + stamina
	 */
	@Override
	protected int calculateMovementSpeed() {
		int speed = PlayerEntity.PLAYER_SPEED;
		int mSpeed = speed;
		if(currentState==State.WALKING) {
			mSpeed = (int)( (float)speed * PlayerEntity.WALK_SPEED_FACTOR);
		}
		else if(currentState == State.SPRINTING) {			
			mSpeed = (int)( (float)speed * PlayerEntity.SPRINT_SPEED_FACTOR); // 1.35		
		}
				
		mSpeed -= weaponWeight;
				
		if(mSpeed < PlayerEntity.PLAYER_MIN_SPEED) {
			mSpeed = PlayerEntity.PLAYER_MIN_SPEED;
		}
		
		return mSpeed;
	}
	
	/* (non-Javadoc)
	 * @see palisma.client.ClientEntity#netUpdate(palisma.game.net.NetEntity)
	 */
	@Override
	public void updateState(NetEntity state, long time) {
		super.updateState(state, time);		
		
		/*
		 * There a full and partial NetPlayer updates.  It
		 * would have been nice to have NetPlayer inherit
		 * from NetPlayerPartial but due to some bit optimizations
		 * it made it a bit awkward, so we live with this
		 * redundant code
		 * 
		 * The Partial updates are for the entities that the
		 * local player doesn't control (they don't need to 
		 * know their stamina, ammo, etc. so we can save some 
		 * bytes by not sending them)
		 */
		if(state instanceof NetPlayer) {
			NetPlayer ps = (NetPlayer) state;
			
			this.currentState = State.fromNetValue(ps.state);
			int newHealth = ps.health;
			this.damageDelta = newHealth - this.health;
			this.health = ps.health;
			this.stamina = ps.stamina;
			
			this.numberOfGrenades = ps.grenades;
			
			if(ps.isOperatingVehicle) {
				if(this.vehicle == null || this.vehicle.getId() != ps.vehicleId) {
					this.vehicle = game.getVehicleById(ps.vehicleId);
				}
			}
			else {
				updateWeaponState(ps.weapon, time);
			}
		}
		else {
			NetPlayerPartial ps = (NetPlayerPartial) state;
			this.currentState = State.fromNetValue(ps.state);
			int newHealth = ps.health;
			this.damageDelta = newHealth - this.health;
			this.health = ps.health;
			
			if(ps.isOperatingVehicle) {
				if(this.vehicle == null || this.vehicle.getId() != ps.vehicleId) {
					this.vehicle = game.getVehicleById(ps.vehicleId);
				}
			}
			else {
				updateWeaponState(ps.weapon, time);
			}
		}
	}

	/**
	 * Updates the weapon state
	 * 
	 * @param netWeapon
	 * @param time
	 */
	protected void updateWeaponState(NetWeapon netWeapon, long time) {
		if(netWeapon != null){
			lineOfSight = WeaponConstants.DEFAULT_LINE_OF_SIGHT;
			
			Type type = Type.fromNet(netWeapon.type); 
			switch(type) {
				case THOMPSON: {
					weapon = WEAPONS[0];
					lineOfSight = WeaponConstants.THOMPSON_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.THOMPSON_WEIGHT;
					break;
				}
				case SHOTGUN: {
					weapon = WEAPONS[1];
					lineOfSight = WeaponConstants.SHOTGUN_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.SHOTGUN_WEIGHT;
					break;
				}
				case ROCKET_LAUNCHER: {
					weapon = WEAPONS[2];
					lineOfSight = WeaponConstants.RPG_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.RPG_WEIGHT;
					break;
				}
				case SPRINGFIELD: {
					weapon = WEAPONS[3];
					lineOfSight = WeaponConstants.SPRINGFIELD_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.SPRINGFIELD_WEIGHT;
					break;
				}	
				case M1_GARAND: {
					weapon = WEAPONS[4];
					lineOfSight = WeaponConstants.M1GARAND_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.M1GARAND_WEIGHT;
					break;
				}
				case KAR98: {
					weapon = WEAPONS[5];
					lineOfSight = WeaponConstants.KAR98_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.KAR98_WEIGHT;
					break;
				}
				case MP44: {
					weapon = WEAPONS[6];
					lineOfSight = WeaponConstants.MP44_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.MP44_WEIGHT;
					break;
				}
				case MP40: {
					weapon = WEAPONS[7];
					lineOfSight = WeaponConstants.MP40_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.MP40_WEIGHT;
					break;
				}
				case PISTOL: {
					weapon = WEAPONS[8];
					lineOfSight = WeaponConstants.PISTOL_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.PISTOL_WEIGHT;
					break;
				}
				case RISKER: {
					weapon = WEAPONS[9];
					lineOfSight = WeaponConstants.RISKER_LINE_OF_SIGHT;
					weaponWeight = WeaponConstants.RISKER_WEIGHT;
					break;
				}
				default: {					
					weapon = null;
					weaponWeight = 0;
				}
			}
			
			if(weapon!=null) {
				weapon.updateState(netWeapon, time);				
			}
		}
		else {
			weapon = null;
			weaponWeight = 0;
		}
	}
	
	/* (non-Javadoc)
	 * @see palisma.client.ClientEntity#update(leola.live.TimeStep)
	 */
	@Override
	public void update(TimeStep timeStep) {		
		if(this.invinceableTime > 0 ) {
			this.invinceableTime -= timeStep.getDeltaTime();
		}
						
		long clockTime = timeStep.getGameClock();
		// 
		if(weapon != null && (isControlledByLocalPlayer() || (lastUpdate+500) >= clockTime)) {
			weapon.update(timeStep);
		}
		
		this.sprite.update(timeStep);
		
		
		if ((lastUpdate+150) < clockTime && !isControlledByLocalPlayer()) {
			fadeAlphaColor = 255 - ((int)(clockTime-lastUpdate)/3);
			if (fadeAlphaColor < 0) fadeAlphaColor = 0;						
		}
		else {
			fadeAlphaColor = 255;
		}	
		
		if(currentState.isVehicleState()) {
			if(this.vehicle != null) {
				this.vehicle.setOperator(this);
			}
			
			fadeAlphaColor = 0;
		}
		
		
		Vector2f pos = getCenterPos();		
		mussleFlash.setPos(pos);
		Vector2f.Vector2fMA(mussleFlash.getPos(), getFacing(), 40.0f, mussleFlash.getPos());
		mussleFlash.setOrientation(getOrientation());
		mussleFlash.setLuminacity((fadeAlphaColor-150)/255.0f);
		mussleFlash.setColor(0.7f,.7f,0.5f);
		
		if(this.damageDelta < 0 && (lastUpdate+400) >= clockTime) {
			onDamage();
		}		
		
		this.bloodEmitter.update(timeStep);
		
		super.update(timeStep);		
	}

	
	protected void onDamage() {
		this.bloodEmitter.resetTimeToLive();
		this.bloodEmitter.setPos(getPos());			
		this.bloodEmitter.start();
		
//		this.effects.addEffect(bloodEmitter);
		Sounds.playSound(Sounds.hit, getId(), getCenterPos());
	}
	
	/* (non-Javadoc)
	 * @see palisma.client.ClientEntity#render(leola.live.gfx.Canvas, leola.live.gfx.Camera)
	 */
	@Override
	public void render(Canvas canvas, Camera camera, long alpha) {
		canvas.setCompositeAlpha(fadeAlphaColor/255.0f);
		canvas.setColor(teamColor, fadeAlphaColor);	
		
		//states[currentState].render(canvas, camera)
		Vector2f c = camera.getPosition();
		Vector2f pos=getRenderPos();
		int rx = (int)(pos.x - c.x);
		int ry = (int)(pos.y - c.y);

		canvas.setFont("Consola", 14);
		canvas.boldFont();
		if(fadeAlphaColor > 0) {
			RenderFont.drawShadedString(canvas, player.getName(), rx - (bounds.width/2), ry + (bounds.height/2) + 40, teamColor );
		
			if (invinceableTime > 0) {
				canvas.setColor(teamColor, 122);
				canvas.fillCircle(20, rx - (bounds.width/2), ry - (bounds.height/2), null);
				canvas.drawCircle(21, rx - (bounds.width/2)-1, ry - (bounds.height/2)-1, 0xff000000);
			}				
		}
		bloodEmitter.render(canvas, camera, alpha);
		
		sprite.render(canvas, camera, alpha);
		canvas.setCompositeAlpha(1.0f);

	}

}
