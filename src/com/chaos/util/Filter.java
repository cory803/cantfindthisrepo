package com.chaos.util;

public interface Filter<E> {
	boolean accept(E e);
}
