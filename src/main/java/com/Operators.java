package com;

/**
 This is just a random assortment of random things, that are always needed

    this class is final instead of abstract, because it has no constructors and therefore cannot be instantiated
    furthermore, you can't extend this class, because it is final
 */
public final class Operators {
	/**
	 Basically adds the ?? operator from php

	 @param obj
	 the object reference to check for nullity
	 @param other
	 the object to use, if the other one is null
	 @param <T>
	 the type of the reference

	 @return {@code obj} if not {@code other}
	 */
	public static <K extends T,T> T ifNullRet(K obj, T other) {
		return (obj == null)? other : obj;
	}

}
