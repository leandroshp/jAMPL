package org.jampl;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class MaxTest {
	
	@Test
	public void toAmpl(){
		Operand operand = new SimpleOperand(new Constant(2),new Variable("A"));

		Operand operand2 = new SimpleOperand(new Constant(3),new Variable("B"));

		Operand operand3 = new SimpleOperand(new Constant(4),new Variable("C"));
		
		Max max = new Max(Arrays.asList(operand, operand2, operand3));
		
		Assert.assertEquals("max(2*A, 3*B, 4*C)", max.toAmpl());
		
	}

}
