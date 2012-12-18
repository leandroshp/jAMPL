package org.jampl;

import java.io.StringWriter;

public abstract class LogicalExpression {
	
	protected Operand LHS;
	protected Operand RHS;
	
	public LogicalExpression(Operand LHS) {
		if(LHS == null){
			throw new IllegalArgumentException("Left-hand side operand must not be null!");
		}
		this.LHS = LHS;
	}

	public abstract String getOperator();
	
	public String toAmpl(){

		if(RHS == null){
			throw new RuntimeException("Right-hand side operand must not be null!");
		}

		StringWriter writer = new StringWriter();
		writer.append(LHS.toAmpl());
		writer.append(" "+getOperator()+" ");
		writer.append(RHS.toAmpl());
		return writer.toString();
	}

	
	protected void setRHS(Operand RHS){
		if(RHS == null){
			throw new IllegalArgumentException("Right-hand side operand must not be null!");
		}
		this.RHS = RHS;
	}
	
	
	

}
