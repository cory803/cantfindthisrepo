package com.runelive.model.actions;

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
	private HashMap<Object, Action> actions = new HashMap<Object, Action>();

	/**
	 * The instance.
	 */
	private static ActionHandler actionHandler = new ActionHandler();

	/**
	 * Submits a new action into the actions map.
	 *
	 * @param key
	 *            the identifier of this action.
	 * @param action
	 *            the action
	 */
	public void submit(Object key, Action action) {
		actions.put(key, action);
	}

	/**
	 * Gets an action from the map.
	 *
	 * @param key
	 * @return
	 */
	public Action getAction(Object key) {
		return actions.get(key);
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
