package org.jampl;


public class EqualExpression extends LogicalExpression{
	
	public EqualExpression(Operand LHS) {
		super(LHS);
	}
	
	public EqualExpression equalTo(Operand RHS){
		setRHS(RHS);
		return this;
	}

	@Override
	public String getOperator() {
		return "=";
	}
}
