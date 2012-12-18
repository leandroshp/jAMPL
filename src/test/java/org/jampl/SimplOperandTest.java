package org.jampl;

import org.junit.Test;

import junit.framework.Assert;

public class SimplOperandTest {
	
	@Test
	public void toAmpl(){
		SimpleOperand operand = new SimpleOperand(new Constant(2),new Variable("A"));
		Assert.assertEquals("2*A",operand.toAmpl());
		
		operand = new SimpleOperand(new Constant(0.95),new Variable("A"));
		Assert.assertEquals("0.95*A",operand.toAmpl());
		
		operand = new SimpleOperand(new Variable("A"));
		Assert.assertEquals("A",operand.toAmpl());
		
	}

}
