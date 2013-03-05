import java.math.BigInteger;

public class Register {
	private BigInteger value;
	private int owner;
	
	public Register(BigInteger value) {
		this.setValue(value, null);
		this.owner = -1;
	}
	
	public Register(int value) {
		this(BigInteger.valueOf(value));
	}

	public Register(long value) {
		this(BigInteger.valueOf(value));
	}

	public BigInteger getValue() {
		return value;
	}

	public synchronized boolean setValue(BigInteger value, Process process) {
		if(process == null || this.owner == -1 || this.owner == process.getProcessID()) {
			this.value = value;
			return true;
		}
		return false;
	}	
	
	public synchronized boolean changeOwner(Process process) {
		if(this.owner == -1) {
			this.owner = process.getProcessID();
			return true;
		}
		return false;
	}
	
	public synchronized boolean free(Process process) {
		if(this.owner == process.getProcessID()) {
			this.owner = -1;
			return true;
		}
		return false;
	}
}