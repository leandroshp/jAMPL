package org.jampl;

import org.junit.Assert;
import org.junit.Test;

public class ConstantTest {

	@Test
	public void toAmpl(){
		Constant constant = new Constant(1);
		
		Assert.assertEquals("1",constant.toAmpl());
		
		constant = new Constant(2.3);
		
		Assert.assertEquals("2.3",constant.toAmpl());
	}
	
}
