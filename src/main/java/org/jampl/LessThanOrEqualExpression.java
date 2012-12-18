package org.jampl;


public class LessThanOrEqualExpression extends LogicalExpression{
	
	public LessThanOrEqualExpression(Operand LHS) {
		super(LHS);
	}
	
	public LessThanOrEqualExpression lessThanOrEqualTo(Operand RHS){
		setRHS(RHS);
		return this;
	}
	
	@Override
	public String getOperator() {
		return "<=";
	}
}
