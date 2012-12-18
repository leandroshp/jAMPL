package org.jampl;

public class Util {
	
	public static boolean isAValidIdentifier(String id){
		if(id == null || id.trim().isEmpty()){
			return false;
		}
		char[] chars = id.toCharArray();
		if(!Character.isJavaIdentifierStart(chars[0])){
			return false;
		}
		for(int i =1;i<chars.length;i++){
			if(!Character.isJavaIdentifierPart(chars[i])){
				return false;
			}
		}
		
		return true;
	}

}
