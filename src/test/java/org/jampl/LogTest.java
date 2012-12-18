package org.jampl;

import org.junit.Test;

import junit.framework.Assert;

public class LogTest {
	
	@Test
	public void toAmpl(){
		SimpleOperand operand = new SimpleOperand(new Constant(2),new Variable("A"));

		Log log = new Log(operand);
		
		Assert.assertEquals("log(2*A)", log.toAmpl());
		
		Expression expression = new Expression(operand);
		
		SimpleOperand operand2 = new SimpleOperand(new Constant(3),new Variable("B"));
		
		expression.plus(operand2);
		
		log = new Log(expression);
		
		Assert.assertEquals("log(2*A + 3*B)", log.toAmpl());
		
		Expression expression2 = new Expression(expression); 
		
		SimpleOperand operand3 = new SimpleOperand(new Constant(4),new Variable("C"));
		
		expression2.times(operand3);
		
		log = new Log(expression2);
		
		Assert.assertEquals("log((2*A + 3*B) * 4*C)", log.toAmpl());
		
	}

}
