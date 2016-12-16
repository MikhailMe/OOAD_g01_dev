package lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.primary;

import lu.uni.lassy.excalibur.examples.icrash.dev.java.system.types.design.JIntIs;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.DtString;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtBoolean;
import lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib.PtString;

public class DtKey extends DtString implements JIntIs{

	public DtKey(PtString s) {
		super(s);
	}

	private static final long serialVersionUID = -8216349515055453968L;

	@Override
	public PtBoolean is() {
		return null;
	}

}