package org.jampl;

public enum VariableType {
	BINARY

	{
		@Override
		public String toString() {
			return "binary";
		}
	};

	public abstract String toString();

}
