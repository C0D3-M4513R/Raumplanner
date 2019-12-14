package com;

/**
 This is just a random assortment of random things, that are always needed
 */
public final class Operators {
	/**
	 * Basically adds the ?? operator from php
	 *
	 * @param obj the object reference to check for nullity
	 * @param other the object to use, if the other one is null
	 * @param <T> the type of the reference
	 * @return {@code obj} if not {@code other}
	 */
	public static <T> T ifNullRet(T obj, T other){
		return obj==null?other:obj;
	}

}
