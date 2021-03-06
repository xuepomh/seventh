/*
 * The Seventh
 * see license.txt 
 */
package seventh.client.gfx.particle;

import seventh.client.gfx.Art;
import seventh.client.gfx.Camera;
import seventh.client.gfx.Canvas;
import seventh.math.Vector2f;
import seventh.shared.TimeStep;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author Tony
 *
 */
public class BloodParticle extends Particle {

	private TextureRegion image;
	private Sprite sprite;
	private int speed;
	private float angle;
	
	private FadeValue alphaFade;
	/**
	 * @param pos
	 * @param vel
	 */
	public BloodParticle(Vector2f pos, Vector2f vel, int angle, int timeToLive) {
		super(pos, vel, timeToLive);
		
		this.speed = 10;
		this.angle = (float)angle;
		this.image = Art.randomBloodspat();
		this.sprite = new Sprite(image);
		this.sprite.flip(false, true);
		
		this.alphaFade = new FadeValue(255, 0, timeToLive + 1000);
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(TextureRegion image) {
		this.image = image;
		this.sprite.setRegion(image);
	}
	
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Particle#update(leola.live.TimeStep)
	 */
	@Override
	public void update(TimeStep timeStep) {	
		super.update(timeStep);
		
		alphaFade.update(timeStep);
		
		double dt = timeStep.asFraction();
		int newX = (int)Math.round(pos.x + vel.x * speed * dt);
		int newY = (int)Math.round(pos.y + vel.y * speed * dt);
		
		speed-=2;
		if(speed < 0) {
			speed = 0;
		}
		
		pos.x = newX;
		pos.y = newY;
	}
		
	/* (non-Javadoc)
	 * @see seventh.client.gfx.particle.Particle#doRender(leola.live.gfx.Canvas, leola.live.gfx.Camera, int, int)
	 */
	@Override
	protected void doRender(Canvas canvas, Camera camera, float rx,
			float ry) {
//		canvas.clearTransform();
		float priorAlpha = canvas.getCompositeAlpha();
//		canvas.drawString(this.alphaFade.getCurrentValue()+"", rx, ry, 0xff00ff00);
//		canvas.setCompositeAlpha( (float)this.alphaFade.getCurrentValue() / 255.0f );
//		canvas.rotate(angle, rx, ry);
//		canvas.drawScaledImage(image, rx, ry, 32, 32, null);		
//		canvas.rotate(-angle, rx, ry);
//		canvas.setCompositeAlpha(priorAlpha);
//		
//		canvas.clearTransform();
		
		float alpha = (float)this.alphaFade.getCurrentValue() / 255.0f;
		canvas.setCompositeAlpha(alpha);
		sprite.setPosition(rx, ry);
		sprite.setSize(32, 32);
		sprite.setRotation(angle);
		//sprite.setColor(1, 1, 1, alpha);
		
		canvas.drawSprite(sprite);
		canvas.setCompositeAlpha(priorAlpha);
	}

}
