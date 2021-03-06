/**
 * 
 */
package seventh.ai.basic.commands;

import java.util.HashMap;
import java.util.Map;

import seventh.ai.AICommand;
import seventh.ai.basic.DefaultAISystem;
import seventh.ai.basic.actions.Action;
import seventh.ai.basic.actions.CoverEntityAction;
import seventh.ai.basic.actions.Goals;
import seventh.game.GameInfo;
import seventh.game.PlayerInfo;
import seventh.math.Vector2f;
import seventh.shared.Cons;

/**
 * Converts the {@link AICommand} to an {@link Action} that will be
 * delegated to a bot.
 * 
 * @author Tony
 *
 */
public class AICommands {
	interface Command {
		Action parse(String ... args);
	}
	
	private Goals goals;
	private GameInfo game;
	
	
	private Map<String, Command> aiCommands;
	
	/**
	 * @param aiSystem
	 */
	public AICommands(DefaultAISystem aiSystem) {
		this.goals = aiSystem.getGoals();
		this.game = aiSystem.getGame();
		this.aiCommands = new HashMap<String, Command>();
		this.aiCommands.put("plant", new Command() {

			@Override
			public Action parse(String... args) {
				return goals.plantBomb();
			}
			
		});
		
		this.aiCommands.put("defuse", new Command() {

			@Override
			public Action parse(String... args) {
				return goals.defuseBomb();
			}
			
		});
		
		this.aiCommands.put("followMe", new Command() {

			@Override
			public Action parse(String... args) {
				if(args.length > 0) {
					String pid = args[0];					
					PlayerInfo player = game.getPlayerById(Integer.parseInt(pid));
					if(player.isAlive()) {
						return new CoverEntityAction(player.getEntity());
					}
					
				}
				return null;
			}
			
		});
		
		this.aiCommands.put("takeCover", new Command() {
			
			@Override
			public Action parse(String... args) {
				Vector2f attackDir = new Vector2f();
				if(args.length > 1) {
					int x = Integer.parseInt(args[0]);
					int y = Integer.parseInt(args[1]);
					attackDir.set(x, y);
				}
				
				Action action = goals.takeCover(attackDir);
				return action;
			}
		});
		
		this.aiCommands.put("action", new Command() {
			
			@Override
			public Action parse(String... args) {
				if(args.length > 0) {
					Action action = goals.getScriptedAction(args[0]);
					return action;
				}
				return null;
			}
		});
	}
	
	/**
	 * Compiles the {@link AICommand} into a {@link Action}
	 * @param cmd
	 * @return the {@link Action} is parsed successfully, otherwise false
	 */
	public Action compile(AICommand cmd) {
		Action result = null;
		String message = cmd.getMessage();
		if(message != null && !"".equals(message)) {
			String[] msgs = message.split(",");
			try {
				Command command = this.aiCommands.get(msgs[0]);
				if(command != null) {
					String[] args = new String[msgs.length -1];
					System.arraycopy(msgs, 1, args, 0, args.length);
					result = command.parse(args);
				}
			}
			catch(Exception e) {
				Cons.println("Error parsing AICommand: " + e);
			}
		}
		
		return result;
	}
}
