package com.nflabs.zeppelin.zrt;

public class ZeppelinRuntimeException extends Exception {

	public ZeppelinRuntimeException(String string) {
		super(string);
	}

	public ZeppelinRuntimeException(Exception e) {
		super(e);
	}

}