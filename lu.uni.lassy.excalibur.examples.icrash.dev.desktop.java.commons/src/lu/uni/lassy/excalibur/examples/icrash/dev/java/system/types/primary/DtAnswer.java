package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

public class DtAnswer extends DtEncryptable implements JIntIs{

	private static final long serialVersionUID = 227L;

	public DtAnswer(PtString s) {
		super(s);
	}
	
	private int _minLength = 6;
	
	public PtBoolean is(){
		return new PtBoolean(this.value.getValue().length() >= _minLength);
	}
	
	public PtString getExpectedDataStructure(){
		return new PtString("Expected strucutre of the password is to have a minimum length of " + _minLength); 
	}
}
