/*
 * see license.txt 
 */
package seventh.client;

import seventh.client.gfx.AnimatedImage;
import seventh.client.gfx.Art;
import seventh.client.gfx.Camera;
import seventh.client.gfx.Canvas;
import seventh.math.Vector2f;
import seventh.shared.TimeStep;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * @author Tony
 *
 */
public class ClientRocket extends ClientEntity {

	private AnimatedImage anim;
	private Sprite sprite;
	/**
	 * 
	 */
	public ClientRocket(ClientGame game, Vector2f pos) {
		super(game, pos);
		anim = Art.newMissileAnim();
		sprite = new Sprite(anim.getCurrentImage());
		sprite.flip(false, true);
		
		bounds.width = 8;
		bounds.height = 10;
	}
	
	/* (non-Javadoc)
	 * @see palisma.client.ClientEntity#update(leola.live.TimeStep)
	 */
	@Override
	public void update(TimeStep timeStep) {	
		super.update(timeStep);
		anim.update(timeStep);
	}

	/* (non-Javadoc)
	 * @see leola.live.gfx.Renderable#render(leola.live.gfx.Canvas, leola.live.gfx.Camera, long)
	 */
	@Override
	public void render(Canvas canvas, Camera camera, long alpha) {
		
//		TextureRegion image = anim.getCurrentImage();
//		int hw = image.getRegionWidth()/2;
//		int hh = image.getRegionHeight()/2;
//		
		
		Vector2f cameraPos = camera.getPosition();
		int x = (int)(pos.x - cameraPos.x) + bounds.width/2;
		int y = (int)(pos.y - cameraPos.y) + bounds.height/2;				
		
		double d = Math.toDegrees(orientation) + 90;
//		canvas.rotate(d, x, y);
//		canvas.drawImage(anim.getCurrentImage(), x-hw, y-hh, null);
//		canvas.rotate(-d, x, y);
		
		sprite.setRotation( (float)d );
		sprite.setPosition(x, y);
		canvas.drawSprite(sprite);
		
	}

}
