package org.jampl;

import org.junit.Test;

import junit.framework.Assert;

public class VariableTest {

	@Test
	public void createBinary(){
		Variable variable = new Variable("A1").asBinary();
		Assert.assertEquals("A1", variable.getName());
		Assert.assertEquals(VariableType.BINARY, variable.getType());
	}
	
	@Test
	public void toAmpl(){
		Variable variable = new Variable("A1").asBinary();
		Assert.assertEquals("var A1 binary;", variable.toAmpl());
	}

	
}
