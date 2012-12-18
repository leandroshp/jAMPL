package org.jampl;

import org.junit.Assert;
import org.junit.Test;

public class GreaterThanOrEqualExpressionTest {

	@Test
	public void toAmpl(){
		Constant constant = new Constant(1);
		
		SimpleOperand operand = new SimpleOperand(new Constant(2),new Variable("A"));
		
		Assert.assertEquals("2*A >= 1",new GreaterThanOrEqualExpression(operand).greaterThanOrEqualTo(constant).toAmpl());
	}
	
}
