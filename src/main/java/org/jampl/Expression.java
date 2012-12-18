package org.jampl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Expression implements Operand {

	private List<Operand> operands;
	private List<String> operators;

	public Expression() {
		operands = new ArrayList<Operand>();
		operators = new ArrayList<String>();
	}

	
	public Expression(Operand operand) {
		this();
		if(operand == null){
			throw new IllegalArgumentException("Operand must not be null!");
		}
		operands.add(operand);
	}
	
	public Expression plus(Operand operand) {
		addOperand(operand, "+");
		return this;
	}
	
	public Expression minus(Variable variable) {
		return minus(new SimpleOperand(variable));
	}
	
	public Expression minus(Operand operand) {
		addOperand(operand, "-");
		return this;
	}
	
	public Expression plus(Variable variable) {
		return plus(new SimpleOperand(variable));
	}
	
	
	public Expression times(Operand operand) {
		addOperand(operand, "*");
		return this;
	}
	
	public Expression times(Variable variable) {
		return times(new SimpleOperand(variable));
	}
	
	public String toAmpl() {
		if(operands.isEmpty()){
			return "";
		}
		StringWriter writer = new StringWriter();

		writer.append(addRoundBracketsIfNeeded(operands.get(0)));
		
		
		for (int i = 1; i < operands.size(); i++) {
			writer.append(" ").append(operators.get(i - 1)).append(" ");
			writer.append(addRoundBracketsIfNeeded(operands.get(i)));
		}

		return writer.toString();
	}
	
	private void addOperand(Operand operand, String operator){
		if(operand == null){
			throw new IllegalArgumentException("Operand must not be null!");
		}
		if(operand instanceof Expression){
			if(((Expression)operand).isEmpty()){
				throw new IllegalArgumentException("Expression must contains at least one operand!");
			}
		}
		if(!operands.isEmpty()){
			operators.add(operator);			
		}
		operands.add(operand);
	}
	
	public boolean isEmpty(){
		return operators.isEmpty();
	}

	private String addRoundBracketsIfNeeded(Operand operand){
		
		String amplCode = operand.toAmpl();
		if(operand instanceof Expression){
			StringWriter writer = new StringWriter();
			writer.append("(").append(amplCode).append(")");
			return writer.toString();
		}
		
		return amplCode;
		
	}
}
