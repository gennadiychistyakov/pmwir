import java.math.BigInteger;
import java.util.*;

public class Registers {
	private TreeMap<BigInteger, Register> data;
	
	public Registers() {
		this.data = new TreeMap<BigInteger, Register>();
	}
	
	public synchronized Register getRegister(BigInteger key) {
		if(!data.containsKey(key)) {
			data.put(key, new Register(0));
		}
		return data.get(key);
	}
}
