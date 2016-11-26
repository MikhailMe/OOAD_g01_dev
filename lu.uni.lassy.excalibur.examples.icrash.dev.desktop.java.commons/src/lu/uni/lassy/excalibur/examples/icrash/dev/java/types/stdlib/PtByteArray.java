package lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib;

import java.io.Serializable;

public class PtByteArray implements Serializable {

	private static final long serialVersionUID = 227L;
	
	byte[] value;

	public PtByteArray(byte[] byteArray){
		value = byteArray;
	}

	public byte[] getValue(){
		return value;
	}

}