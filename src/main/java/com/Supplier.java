package com;

@FunctionalInterface
public interface Supplier<T,U,R,V> {
	V get(T t,U u,R r);
}
