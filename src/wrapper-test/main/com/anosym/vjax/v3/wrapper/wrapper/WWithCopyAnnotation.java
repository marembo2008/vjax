/**
 *This is a generated artifact for com.anosym.vjax.v3.wrapper.WithCopyAnnotation
 */
package com.anosym.vjax.v3.wrapper.wrapper;

public  class WWithCopyAnnotation implements java.io.Serializable {

	private String name;
	private int id;
	@com.anosym.vjax.annotations.v3.Converter(value = com.anosym.vjax.converter.v3.impl.CalendarConverter.class, params = {})
	private java.util.Calendar time;
	@com.anosym.vjax.v3.wrapper.WithCopyAnnotation.Options(options = {9, 84, 7374, 88384})
	private int options;

	public String getName() {
		return this.name;
	}

	public void  setName(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void  setId(int id) {
		this.id = id;
	}

	public java.util.Calendar getTime() {
		return this.time;
	}

	public void  setTime(java.util.Calendar time) {
		this.time = time;
	}

	public int getOptions() {
		return this.options;
	}

	public void  setOptions(int options) {
		this.options = options;
	}

	@Override
	public int hashCode() {
		int hash = 509;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		final WWithCopyAnnotation other = (WWithCopyAnnotation)obj;
		return true;
	}

}