package org.jampl;

import java.io.StringWriter;
import java.util.List;

public class Max implements Operand {

	private List<Operand> operands;

	public Max(List<Operand> operands) {
		if (operands == null) {
			throw new IllegalArgumentException("Operands cannot be null");
		}
		this.operands = operands;
	}
	
	public Max addOperand(Operand operand){
		if (operand == null) {
			throw new IllegalArgumentException("Operand may not be null");
		}
		operands.add(operand);
		return this;
	}

	public String toAmpl() {
		if(operands.isEmpty()){
			return "";
		}
		//There is no need to insert max
		if(operands.size() == 1){
			return operands.get(0).toAmpl();
		}
		StringWriter writer = new StringWriter();
		writer.append("max(");
		for (int i = 0; i < operands.size(); i++) {
			if (i != 0) {
				writer.append(", ");
			}
			writer.append(operands.get(i).toAmpl());
		}
		writer.append(")");
		return writer.toString();
	}
}
