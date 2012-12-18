package ampl;

import java.io.IOException;

import junit.framework.Assert;

import org.jampl.Constant;
import org.jampl.EqualExpression;
import org.jampl.Expression;
import org.jampl.GreaterThanOrEqualExpression;
import org.jampl.LessThanOrEqualExpression;
import org.jampl.Log;
import org.jampl.Model;
import org.jampl.Objective;
import org.jampl.SimpleOperand;
import org.jampl.Variable;
import org.jampl.exception.InfeasibleProblemException;
import org.jampl.exception.InvalidModelException;
import org.junit.Test;

public class AMPLTest {
//
	
	@Test
	public void send() throws IOException, InfeasibleProblemException, InvalidModelException{

		Model model = new Model();
		
		Variable A00 = new Variable("A_0_0").asBinary();
		Variable A01 = new Variable("A_0_1").asBinary();

		Variable A10 = new Variable("A_1_0").asBinary();
		Variable A11 = new Variable("A_1_1").asBinary();

		
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


		model.solve();
		
		A00 = model.getVariable("A_0_0");
		Assert.assertEquals(1.0, A00.getValue());
		A01 = model.getVariable("A_0_1");
		Assert.assertEquals(0.0, A01.getValue());
		A10 = model.getVariable("A_1_0");
		Assert.assertEquals(1.0, A10.getValue());
		A11 = model.getVariable("A_1_1");
		Assert.assertEquals(0.0, A11.getValue());
	}
}
