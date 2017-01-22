package com.runelive.util;

public interface FilterExecutable<E> {

	void execute(E e);

	default boolean accept(E e) {
		return true;
	}

}
