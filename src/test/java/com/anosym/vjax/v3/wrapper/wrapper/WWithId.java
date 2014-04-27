/**
 *This is a generated artifact for com.anosym.vjax.v3.wrapper.WithId
 */
package com.anosym.vjax.v3.wrapper.wrapper;

public  class WWithId implements java.io.Serializable {

	private Long id;
	private String value;

	public Long getId() {
		return this.id;
	}

	public void  setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void  setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		int hash = 124;
		hash = 50 * hash + (this.id != null ? this.id.hashCode() : 0);
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
		final WWithId other = (WWithId)obj;
		if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

}