package org.jampl;

public class SimpleOperand implements Operand {

	private Constant coeficient;
	private Variable variable;

	public SimpleOperand(Constant coeficient, Variable variable) {
		if(variable == null){
			throw new IllegalArgumentException("Variable cannot be null");
		}
		
		this.coeficient = coeficient;
		this.variable = variable;
	}
	
	public SimpleOperand(Number coeficient, Variable variable) {
		this(new Constant(coeficient),variable);
	}

	public SimpleOperand(Variable variable) {
		this((Constant)null, variable);
	}

	public String toAmpl() {
		if(coeficient != null){
			return coeficient.toAmpl() + "*" + variable.getName();	
		}
		return variable.getName();	
	}

}
