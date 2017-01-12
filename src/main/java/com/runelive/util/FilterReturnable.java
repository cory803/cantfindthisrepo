package com.runelive.util;

public abstract class FilterReturnable<E> {
	public boolean accept(E e) {
		return true;
	}

	public abstract void execute(E e);

	public abstract E returnValue();
}
