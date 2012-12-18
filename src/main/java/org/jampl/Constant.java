package org.jampl;

public class Constant implements Operand {

	private Number value;
	
	public Constant(Number value){
		if(value == null){
			throw new IllegalArgumentException("Constant value can not be null");
		}
		this.value = value;
	}
	
	public Number getValue() {
		return value;
	}
	
	public String toAmpl() {
		return value.toString();
	}

}
