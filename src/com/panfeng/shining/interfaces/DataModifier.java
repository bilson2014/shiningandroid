package com.panfeng.shining.interfaces;

public interface DataModifier<E> {
	public void refresh(E data);
	public void append(E data);
}
