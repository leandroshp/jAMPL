package org.jampl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Objective {

	private String name;
	private String type;
	
	private Expression expression;
	private List<LogicalExpression> subjects;
	private List<String> subjectsName;
	
	public Objective(String name){
		if(!Util.isAValidIdentifier(name)){
			throw new IllegalArgumentException("Objective name must be a valid Java identifier!");
		}
		this.name = name;
		subjects = new ArrayList<LogicalExpression>();
		subjectsName = new ArrayList<String>();
	}
	
	private void set(String type, Expression expression){
		this.type = type;
		this.expression = expression;
	}
	
	public Objective subjectTo(String name, LogicalExpression logicalExpression){
		if(!Util.isAValidIdentifier(name)){
			throw new IllegalArgumentException("SubjectTo identifier name must be a valid Java identifier!");
		}
		if(logicalExpression == null){
			throw new IllegalArgumentException("Logical expression must not be null!");
		}
		this.subjects.add(logicalExpression);
		this.subjectsName.add(name);
		return this;
	}
	
	public Objective maximize(Expression expression){
		if(expression == null){
			throw new IllegalArgumentException("Maximize expression must not be null!");
		}
		set("maximize", expression);
		return this;
	}
	
	public Objective minimize(Expression expression){
		if(expression == null){
			throw new IllegalArgumentException("Minimize expression must not be null!");
		}
		set("minimize", expression);
		return this;
	}
	
	public String toAmpl(){
		
		if(expression == null){
			throw new RuntimeException("Objective expression to maximize or minimize must not be null!");
		}
		
		StringWriter amplCode = new StringWriter();
		amplCode.append(type).append(" ").append(name).append(": ");
				
		amplCode.append(expression.toAmpl()).append(";");
		
		for(int i = 0;i < subjects.size();i++){
			amplCode.append(System.getProperty("line.separator"));
			amplCode.append("subject to ");
			amplCode.append(subjectsName.get(i));
			amplCode.append(": ");
			amplCode.append(subjects.get(i).toAmpl());
			amplCode.append(";");
		}
		
		return amplCode.toString();
	
	}
}
