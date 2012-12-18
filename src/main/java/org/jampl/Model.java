package org.jampl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jampl.exception.InfeasibleProblemException;
import org.jampl.exception.InvalidModelException;

import ampl.Ampl;

public class Model {
	
	private static Ampl ampl;
	
	private Map<String,Variable> variables;
	private List<String> variablesNames;
	
	private Objective objective;

	public Model() {
		variables = new HashMap<String, Variable>();
		variablesNames = new ArrayList<String>();
		if(ampl == null){
			try {
				ampl = new Ampl();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Model addVariable(Variable variable) {
		if (variable == null) {
			throw new IllegalArgumentException("Variable must not be null!");
		}
		variables.put(variable.getName(), variable);
		variablesNames.add(variable.getName());
		return this;
	}

	public Model setObjective(Objective objective) {
		if (objective == null) {
			throw new IllegalArgumentException("Objective must not be null!");
		}
		this.objective = objective;
		return this;
	}

	public String toAmpl() {
		if(objective == null){
			return "";
		}		

		StringWriter amplCode = new StringWriter();
		
		for(int i=0;i<variablesNames.size();i++){
			amplCode.append(variables.get(variablesNames.get(i)).toAmpl());
			amplCode.append(System.getProperty("line.separator"));		
		}	
		
		amplCode.append(objective.toAmpl());

		return amplCode.toString();
	}
	
	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public void solve() throws InfeasibleProblemException, InvalidModelException{
		
		String amplModel = toAmpl();
		
		if(amplModel.isEmpty()){
			System.out.println("No model to solve!");
			return;
		}
		
		StringWriter command = new StringWriter();
		command.append("reset;\n");
		command.append(toAmpl());
		command.append("\nsolve;\n");
		
		
		
		if(!variables.isEmpty()){
			command.append("display ");

			for(int i=0;i<variablesNames.size();i++){
				if (i != 0) {
					command.append(",");
				}
				command.append(variablesNames.get(i));
			}				
			
			command.append(";\n");	
		}
		
		ampl.send(command.toString());
		
		String output = ampl.rcv();
		if(Ampl.isInvalidModel(output)){
			System.out.println(output);
			throw new InvalidModelException();
		}
		if(Ampl.isInfeasibleModel(output)){
			throw new InfeasibleProblemException();
		}

		String[] results = output.split("\n");
		for(int i = results.length - variables.size();i<results.length; i++){
			String[] varNameAndValue = results[i].split("=");
			Variable variable = getVariable(varNameAndValue[0].trim());
			variable.setValue(Double.valueOf(varNameAndValue[1]));
		}	
		
		//ampl.close();

	}
	
	

}
