package com.mavriksc.types;

public class ValPos implements Comparable<ValPos> {
	private Float val;
	private Integer pos;

	public ValPos(Float val, Integer pos) {
		super();
		this.val = val;
		this.pos = pos;
	}

	public Float getVal() {
		return val;
	}

	public void setVal(Float val) {
		this.val = val;
	}

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}

	@Override
	public int compareTo(ValPos o) {
		return o.getVal().compareTo(val);
	}
	
	
}
