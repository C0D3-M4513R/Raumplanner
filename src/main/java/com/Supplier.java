package com;

@FunctionalInterface
/**
 This class takes 3 inputs, and produces one output
 */
public interface Supplier<T,U,R,V> {
	V get(T t,U u,R r);
}
