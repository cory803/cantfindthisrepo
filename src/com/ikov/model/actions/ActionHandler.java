package com.ikov.model.actions;

import java.util.HashMap;

/**
 * A handler for different actions.
 * 
 * @author Blake
 *
 */
public class ActionHandler {

	/**
	 * A map containing all the available actions.
	 */
	private HashMap<Integer, ButtonAction> actions = new HashMap<Integer, ButtonAction>();

	/**
	 * The instance.
	 */
	private static ActionHandler actionHandler = new ActionHandler();

	/**
	 * Submits a new action into the actions map.
	 * 
	 * @param key
	 *            the id of the button
	 * @param action
	 *            the action
	 */
	public void submit(int id, ButtonAction action) {
		actions.put(id, action);
	}

	/**
	 * Gets an action from the map.
	 * 
	 * @param buttonId
	 * @return
	 */
	public ButtonAction getAction(int buttonId) {
		return actions.get(buttonId);
	}

	/**
	 * Gets the instance.
	 * 
	 * @return the instance
	 */
	public static ActionHandler getActionHandler() {
		return actionHandler;
	}

}
