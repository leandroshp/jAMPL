package org.jampl;

import junit.framework.Assert;

import org.junit.Test;

public class UtilTest {

	@Test
	public void isAValidIdentifier(){
		Assert.assertTrue(Util.isAValidIdentifier("leandro"));
		Assert.assertTrue(!Util.isAValidIdentifier(null));
		Assert.assertTrue(!Util.isAValidIdentifier("3leandro"));
		Assert.assertTrue(!Util.isAValidIdentifier("3lea?ndro"));
	}
	
	
}
