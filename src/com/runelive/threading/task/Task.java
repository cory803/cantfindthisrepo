package com.runelive.threading.task;

import com.runelive.threading.GameEngine;

public abstract class Task {
	public abstract void execute(GameEngine context);

	public void dispose() {
	}
}
