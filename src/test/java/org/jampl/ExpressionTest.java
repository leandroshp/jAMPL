package org.jampl;

import org.junit.Test;

import junit.framework.Assert;

public class ExpressionTest {
	
	@Test
	public void toAmpl(){
		SimpleOperand operand = new SimpleOperand(new Constant(2),new Variable("A"));

		Expression expression = new Expression(operand);
		
		Assert.assertEquals("2*A", expression.toAmpl());
		
		
		SimpleOperand operand2 = new SimpleOperand(new Constant(3),new Variable("B"));
		
		expression.plus(operand2);
		
		Assert.assertEquals("2*A + 3*B", expression.toAmpl());
		
		Expression expression2 = new Expression(expression); 
		
		SimpleOperand operand3 = new SimpleOperand(new Constant(4),new Variable("C"));
		
		expression2.times(operand3);
		
		Assert.assertEquals("(2*A + 3*B) * 4*C", expression2.toAmpl());
		
	}

}
