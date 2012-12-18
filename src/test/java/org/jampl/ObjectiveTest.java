package org.jampl;

import org.junit.Test;

import junit.framework.Assert;

public class ObjectiveTest {
	
	@Test
	public void toAmpl(){
		
		Variable A = new Variable("A").asBinary();
		Variable B = new Variable("B").asBinary();
		
		Expression expression = new Expression(new SimpleOperand(A)).plus(new SimpleOperand(new Constant(3),B));
		
		Objective objective = new Objective("ResponseTime").minimize(expression);
		objective.subjectTo("AEquals", new EqualExpression(new SimpleOperand(new Constant(2),A)).equalTo(new Constant(10)));
		
		StringBuffer result = new StringBuffer();
		result.append("minimize ResponseTime: A + 3*B;");
		result.append(System.getProperty("line.separator"));
		result.append("subject to AEquals: 2*A = 10;");
		
		Assert.assertEquals(result.toString(),objective.toAmpl());

	}

}
