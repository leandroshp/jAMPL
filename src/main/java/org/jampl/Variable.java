package org.jampl;

public class Variable {

	private String name;
	private VariableType type;
	private double value; 
	
	public Variable(String name){
		if(!Util.isAValidIdentifier(name)){
			throw new IllegalArgumentException("Variable name must be a valid Java identifier!");
		}
		this.name = name;
	}

	public Variable asBinary(){
		this.type = VariableType.BINARY;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public VariableType getType() {
		return type;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public String toAmpl(){
		return "var "+name+" "+(type==null?"":type.toString())+";";
	}

}
