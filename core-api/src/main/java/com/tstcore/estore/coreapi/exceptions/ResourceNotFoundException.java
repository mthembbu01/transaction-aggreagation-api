package com.tstcore.estore.coreapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ResourceNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -6127088647065852843L;
	private  String resourceName;
	private  String fieldName;
	private  Integer integerField;
	private String stringField;
	private  Long longField;
	private Double doubleField;
	
	/**
	 * 
	 * @param resourceName defines the firstName of the resource to look for in a database
	 * @param fieldName firstName of the field used to retrieve the resource
	 * @param stringField String type defining the resource field
	 */
	public ResourceNotFoundException(final String resourceName, final String fieldName, final String stringField) {
		super(String.format("%s resource with %s: '%s' not found!", resourceName, fieldName, stringField));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.stringField = stringField;
	}
	/**
	 * 
	 * @param resourceName defines the firstName of the resource to look for in a database
	 * @param fieldName firstName of the field used to retrieve the resource
	 * @param integerField Integer type defining the resource field
	 */
	public ResourceNotFoundException(final String resourceName, final String fieldName, final Integer integerField) {
		super(String.format("%s with %s: '%s' not found!", resourceName, fieldName, integerField));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.integerField = integerField;
	}
	/**
	 * 
	 * @param resourceName defines the firstName of the resource to look for in a database
	 * @param fieldName firstName of the field used to retrieve the resource
	 * @param longField Long type defining the resource field
	 */
	public ResourceNotFoundException(final String resourceName, final String fieldName , final Long longField) {
		super(String.format("%s with %s: '%s' not found!", resourceName, fieldName, longField));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.longField = longField;
	}
	/**
	 *
	 * @param resourceName defines the firstName of the resource to look for in a database
	 * @param fieldName firstName of the field used to retrieve the resource
	 * @param doubleField double type defining the resource field
	 */
	public ResourceNotFoundException(final String resourceName, final String fieldName , final Double doubleField) {
		super(String.format("%s with %s: '%s' not found!", resourceName, fieldName, doubleField));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.doubleField = doubleField;
	}
	public ResourceNotFoundException() {
		super();
	}

}
