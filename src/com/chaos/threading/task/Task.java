package com.chaos.threading.task;

import com.chaos.threading.GameEngine;

public abstract class Task {
	public abstract void execute(GameEngine context);

	public void dispose() {
	}
}
