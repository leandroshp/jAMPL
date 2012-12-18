package org.jampl;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

public class ModelTest {
	
	@Test
	public void toAmpl(){
		Model model = new Model();
		
		Variable A = new Variable("A").asBinary();
		Variable B = new Variable("B").asBinary();
		
		model.addVariable(A);
		model.addVariable(B);
		
		Expression expression = new Expression(new SimpleOperand(A)).plus(new SimpleOperand(new Constant(3),B));
		
		
		model.setObjective(new Objective("ResponseTime").minimize(expression));
		
		StringBuffer result = new StringBuffer();
		result.append("var A binary;");
		result.append(System.getProperty("line.separator"));
		result.append("var B binary;");
		result.append(System.getProperty("line.separator"));
		result.append("minimize ResponseTime: A + 3*B;");
		
		Assert.assertEquals(result.toString(),model.toAmpl());
	}
	
	@Test
	public void toAmpl2(){
		Model model = new Model();
		
		Variable A00 = new Variable("A00").asBinary();
		Variable A01 = new Variable("A01").asBinary();

		Variable A10 = new Variable("A10").asBinary();
		Variable A11 = new Variable("A11").asBinary();

		
		model.addVariable(A00);
		model.addVariable(A01);
		model.addVariable(A10);
		model.addVariable(A11);
		
		Expression expressionA0 = new Expression(new SimpleOperand(new Constant(2000), A00)).plus(new SimpleOperand(new Constant(1000), A01));
		Expression expressionA1 = new Expression(new SimpleOperand(new Constant(2000), A10)).plus(new SimpleOperand(new Constant(3000), A11));
		
		Expression responseTimeExpression = new Expression(expressionA0).plus(expressionA1);
		
		
		Objective objective = new Objective("ResponseTime").minimize(responseTimeExpression);
		
		Expression bindingA0 = new Expression(new SimpleOperand(A00)).plus(new SimpleOperand(A01));
		Expression bindingA1 = new Expression(new SimpleOperand(A10)).plus(new SimpleOperand(A11));		
		
		
		Expression rlA00 = new Expression(new SimpleOperand(A00)).times(new Log(new Constant(0.99)));
		Expression rlA01 = new Expression(new SimpleOperand(A01)).times(new Log(new Constant(0.95)));
		Expression rlA10 = new Expression(new SimpleOperand(A10)).times(new Log(new Constant(0.99)));
		Expression rlA11 = new Expression(new SimpleOperand(A11)).times(new Log(new Constant(0.91)));		
		
		Expression desiredReliability = new Expression(rlA00).plus(rlA01).plus(rlA10).plus(rlA11);
		
		
		objective.subjectTo("a0OneBinding", new EqualExpression(bindingA0).equalTo(new Constant(1)));
		objective.subjectTo("a1OneBinding", new EqualExpression(bindingA1).equalTo(new Constant(1)));
		objective.subjectTo("DesiredResponseTime", new LessThanOrEqualExpression(responseTimeExpression).lessThanOrEqualTo(new Constant(4000)));
		objective.subjectTo("DesiredReliability", new GreaterThanOrEqualExpression(desiredReliability).greaterThanOrEqualTo(new Log(new Constant(0.95))));
		
		
		model.setObjective(objective);
		
		System.out.println(model.toAmpl());
	}

	@Test
	public void toAmpl3(){
		Model model = new Model();
		
		Variable A00 = new Variable("A00").asBinary();
		Variable A01 = new Variable("A01").asBinary();

		Variable A10 = new Variable("A10").asBinary();
		Variable A11 = new Variable("A11").asBinary();

		Variable A20 = new Variable("A20").asBinary();
		Variable A21 = new Variable("A21").asBinary();

		
		model.addVariable(A00);
		model.addVariable(A01);
		model.addVariable(A10);
		model.addVariable(A11);
		model.addVariable(A20);
		model.addVariable(A21);
		
		Expression expressionA0 = new Expression(new SimpleOperand(new Constant(2000), A00)).plus(new SimpleOperand(new Constant(1000), A01));
		
		Operand expressionA1 = new Expression(new SimpleOperand(new Constant(2000), A10)).plus(new SimpleOperand(new Constant(3000), A11));
		Operand expressionA2 = new Expression(new SimpleOperand(new Constant(2000), A20)).plus(new SimpleOperand(new Constant(1000), A21));
		
		
		Expression responseTimeExpression = new Expression(expressionA0).plus(new Max(Arrays.asList(expressionA1,expressionA2)));
		
		
		Objective objective = new Objective("ResponseTime").minimize(responseTimeExpression);
		
		Expression bindingA0 = new Expression(new SimpleOperand(A00)).plus(new SimpleOperand(A01));
		Expression bindingA1 = new Expression(new SimpleOperand(A10)).plus(new SimpleOperand(A11));
		Expression bindingA2 = new Expression(new SimpleOperand(A20)).plus(new SimpleOperand(A21));		
		
		
		Expression rlA00 = new Expression(new SimpleOperand(A00)).times(new Log(new Constant(0.99)));
		Expression rlA01 = new Expression(new SimpleOperand(A01)).times(new Log(new Constant(0.95)));
		Expression rlA10 = new Expression(new SimpleOperand(A10)).times(new Log(new Constant(0.99)));
		Expression rlA11 = new Expression(new SimpleOperand(A11)).times(new Log(new Constant(0.91)));
		Expression rlA20 = new Expression(new SimpleOperand(A20)).times(new Log(new Constant(0.99)));
		Expression rlA21 = new Expression(new SimpleOperand(A21)).times(new Log(new Constant(0.91)));		
		
		Expression desiredReliability = new Expression(rlA00).plus(rlA01).plus(rlA10).plus(rlA11).plus(rlA20).plus(rlA21);
		
		
		objective.subjectTo("a0OneBinding", new EqualExpression(bindingA0).equalTo(new Constant(1)));
		objective.subjectTo("a1OneBinding", new EqualExpression(bindingA1).equalTo(new Constant(1)));
		objective.subjectTo("a2OneBinding", new EqualExpression(bindingA2).equalTo(new Constant(1)));		
		
		
		objective.subjectTo("DesiredResponseTime", new LessThanOrEqualExpression(responseTimeExpression).lessThanOrEqualTo(new Constant(4000)));
		objective.subjectTo("DesiredReliability", new GreaterThanOrEqualExpression(desiredReliability).greaterThanOrEqualTo(new Log(new Constant(0.95))));
		
		
		model.setObjective(objective);
		
		System.out.println("\n\n\n ================ MODEL WITH MAX =============== \n\n\n");
		System.out.println(model.toAmpl());
	}

	
}
