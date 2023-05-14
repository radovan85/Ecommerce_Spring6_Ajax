package com.radovan.spring.exceptions;

import javax.management.RuntimeErrorException;

public class InsufficientStockException extends RuntimeErrorException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InsufficientStockException(Error e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

}
