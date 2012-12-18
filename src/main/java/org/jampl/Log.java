package org.jampl;

import java.io.StringWriter;

public class Log implements Operand {

	private Operand operand;

	public Log(Operand operand) {
		if(operand == null){
			throw new IllegalArgumentException("Operand cannot be null!");
		}
		this.operand = operand;
	}

	public String toAmpl() {
		StringWriter writer = new StringWriter();
		writer.append("log(");
		writer.append(operand.toAmpl());
		writer.append(")");
		return writer.toString();
	}
}
