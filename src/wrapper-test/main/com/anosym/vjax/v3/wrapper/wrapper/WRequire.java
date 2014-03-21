/**
 *This is a generated artifact for com.anosym.vjax.v3.wrapper.Require
 */
package com.anosym.vjax.v3.wrapper.wrapper;

public  class WRequire extends com.anosym.vjax.v3.wrapper.wrapper.WExtended implements java.io.Serializable {

	private String name;
	private int index;
	private com.anosym.vjax.v3.wrapper.wrapper.WOption[] options;

	public String getName() {
		return this.name;
	}

	public void  setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return this.index;
	}

	public void  setIndex(int index) {
		this.index = index;
	}

	public com.anosym.vjax.v3.wrapper.wrapper.WOption[] getOptions() {
		return this.options;
	}

	public void  setOptions(com.anosym.vjax.v3.wrapper.wrapper.WOption[] options) {
		this.options = options;
	}

	@Override
	public int hashCode() {
		int hash = 20;
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
		final WRequire other = (WRequire)obj;
		return true;
	}

}