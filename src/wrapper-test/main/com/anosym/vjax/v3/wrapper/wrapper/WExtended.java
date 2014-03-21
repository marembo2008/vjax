/**
 *This is a generated artifact for com.anosym.vjax.v3.wrapper.Extended
 */
package com.anosym.vjax.v3.wrapper.wrapper;

public  class WExtended implements java.io.Serializable {

	private java.math.BigDecimal score;

	public java.math.BigDecimal getScore() {
		return this.score;
	}

	public void  setScore(java.math.BigDecimal score) {
		this.score = score;
	}

	@Override
	public int hashCode() {
		int hash = 892;
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
		final WExtended other = (WExtended)obj;
		return true;
	}

}