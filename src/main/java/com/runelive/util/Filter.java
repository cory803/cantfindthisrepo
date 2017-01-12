package com.runelive.util;

public interface Filter<E> {
	boolean accept(E e);
}
