/*
 * The Seventh
 * see license.txt 
 */
package seventh.client.gfx.particle;

import java.util.Random;

import seventh.client.ClientEntity;
import seventh.client.gfx.Art;
import seventh.client.gfx.Camera;
import seventh.client.gfx.Canvas;
import seventh.math.Vector2f;
import seventh.shared.TimeStep;

/**
 * @author Tony
 *
 */
public class ExplosionEmitter extends Emitter {
	
//	private RocketTrailEmitter emitter;
	private Vector2f shrapnelVel;
	private int delayTime;
	
//	private float time;
//	private TextureRegion tex;
	
	public ExplosionEmitter(Vector2f pos, int timeToLive, int timeToNextSpawn) {
		this(pos, timeToLive, timeToNextSpawn, 25);
	}
	
	/**
	 * @param pos
	 * @param timeToLive
	 * @param timeToNextSpawn
	 */
	public ExplosionEmitter(Vector2f pos, int timeToLive, int timeToNextSpawn, int maxParticles) {
		super(pos, timeToLive, timeToNextSpawn);
		this.maxParticles = maxParticles;
		setDieInstantly(false);
		
		shrapnelVel = new Vector2f();
//		emitter = new RocketTrailEmitter(pos.createClone(), 200, 0);
		
		this.delayTime = 90;
		
		Random r = getRandom();
		float x = r.nextFloat() * 40;
		float y = r.nextFloat() * 40;
		
		if(r.nextBoolean()) x=-x;
		if(r.nextBoolean()) y=-y;
		shrapnelVel.set(x,y);			
	}

	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Emitter#attachTo(seventh.client.ClientEntity)
	 */
	@Override
	public void attachTo(ClientEntity ent) {
		super.attachTo(ent);		
	}
	
	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Emitter#update(seventh.shared.TimeStep)
	 */
	@Override
	public void update(TimeStep timeStep) {
		this.delayTime -= timeStep.getDeltaTime();
		
		if(delayTime < 0) {
//			emitter.getPos().x+=shrapnelVel.x;
//			emitter.getPos().y+=shrapnelVel.y;
//			emitter.update(timeStep);
			
			
			super.update(timeStep);
			if(isAlive()) {
				for(int i = 0; i < 5; i++) {
					spawnParticle();
				}
			}
		}
		
		
	}
	
	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Emitter#render(seventh.client.gfx.Canvas, seventh.client.gfx.Camera, long)
	 */
	@Override
	public void render(Canvas canvas, Camera camera, long alpha) {
//		emitter.render(canvas, camera, alpha);
/*		Vector2f pos = getPos();
		Vector2f c = camera.getPosition();
		int rx = (int)(pos.x - c.x);
		int ry = (int)(pos.y - c.y);
		
		time+=Gdx.graphics.getDeltaTime();
		if(tex==null) {
			tex = ScreenUtils.getFrameBufferTexture();
					//getFrameBufferTexture(rx-128, ry-128, 256,256);
			//tex.flip(true, false);
		}
		//tex.flip(true, false);	
		
	//	canvas.begin();
		
		canvas.pushShader(RippleEffectShader.getInstance()
				.setParam("time", time)
				.setParam("resolution", canvas.getWidth(), canvas.getHeight()).getShader());
		
//		canvas.drawImage(tex, rx-128, ry-128, null);
		canvas.drawImage(tex, 0, 0, null);
		canvas.popShader();
	//	canvas.end();
*/
		
		super.render(canvas, camera, alpha);
		//emitter.render(canvas, camera, alpha);
	}
	
	
	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Emitter#newParticle()
	 */
	@Override
	protected Particle newParticle() {
		Random r = getRandom();
		float x = r.nextFloat();
		float y = r.nextFloat();
		
		if(r.nextBoolean()) x=-x;
		if(r.nextBoolean()) y=-y;

		int color = 0x008B8386;
		switch(r.nextInt(4)) {
			case 0: color = 0x00838B8B;
				break;
			case 1: color = 0x00808A87;
				break;
			case 2: color = 0x00838B83;
				break;
			default:
				color = 0x008B8386;
				break;
		}
		

		int timeToLive = 4000;
		
		Vector2f pos = getPos().createClone();

		pos.x += x * r.nextInt(25);
		pos.y += y * r.nextInt(25);
				
		return new SmokeParticle(pos, new Vector2f(x,y), timeToLive, color);
	}

	class SmokeParticle extends Particle {

		private FadeValue alpha;
		private int speed;
		private final int maxSpeed;
		private int color;
		/**
		 * @param pos
		 * @param vel
		 * @param timeToLive
		 */
		public SmokeParticle(Vector2f pos, Vector2f vel, int timeToLive, int color) {
			super(pos, vel, timeToLive);
			alpha = new FadeValue(45, 0, timeToLive-400);
			maxSpeed = 40;
			speed = maxSpeed;
			
			this.color = color;
		}
		
		/* (non-Javadoc)
		 * @see seventh.client.gfx.particle.Particle#update(leola.live.TimeStep)
		 */
		@Override
		public void update(TimeStep timeStep) {		
			super.update(timeStep);
			alpha.update(timeStep);
			
			double dt = timeStep.asFraction();
			int newX = (int)Math.round(pos.x + vel.x * speed * dt);
			int newY = (int)Math.round(pos.y + vel.y * speed * dt);
			
			speed = getRandom().nextInt(maxSpeed);
			
			pos.x = newX;
			pos.y = newY;
		}
		
		/* (non-Javadoc)
		 * @see seventh.client.gfx.particle.Particle#doRender(leola.live.gfx.Canvas, leola.live.gfx.Camera, int, int)
		 */
		@Override
		protected void doRender(Canvas canvas, Camera camera, float renderX,
				float renderY) {
			
			int colorS = (alpha.getCurrentValue() << 24) | color;			
			canvas.setCompositeAlpha(alpha.getCurrentValue()/255.0f);
			canvas.drawImage(Art.smokeImage, renderX, renderY, colorS);
			canvas.setCompositeAlpha(1.0f);			
		}
	}
}
