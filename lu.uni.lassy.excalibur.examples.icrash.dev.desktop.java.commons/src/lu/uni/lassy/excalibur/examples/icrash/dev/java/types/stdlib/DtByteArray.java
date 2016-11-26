package lu.uni.lassy.excalibur.examples.icrash.dev.java.types.stdlib;

import java.io.Serializable;


public class DtByteArray implements Serializable {

		private static final long serialVersionUID = 227L;
		
		public byte[] value;
		
		public DtByteArray(byte[] byteArray){
			value = byteArray;
		}

		public PtBoolean eq(byte[] byteArray){
			String a = new String(byteArray);
			String b = new String(this.value);
			return new PtBoolean(a.equals(b));
		}
		
		public String toString(){
			return value.toString();
		}
}