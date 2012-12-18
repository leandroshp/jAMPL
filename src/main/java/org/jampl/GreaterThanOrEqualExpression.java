package org.jampl;


public class GreaterThanOrEqualExpression extends LogicalExpression{
	
	public GreaterThanOrEqualExpression(Operand LHS) {
		super(LHS);
	}
	
	public GreaterThanOrEqualExpression greaterThanOrEqualTo(Operand RHS){
		setRHS(RHS);
		return this;
	}
	
	@Override
	public String getOperator() {
		return ">=";
	}

}
