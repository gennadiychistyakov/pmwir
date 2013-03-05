import java.math.BigInteger;

public class Command {
	
	public static final int _Z = 0;
	public static final int _S = 1;
	public static final int _T = 2;
	public static final int _J = 3;
	public static final int _I = 4;
	public static final int _O = 5;
	public static final int _G = 6;
	public static final int _P = 7;
	public static final int _s = 8;
	
	private int type;
	private BigInteger param1 = null;
	private int r1 = 0;
	private BigInteger param2 = null;
	private int r2 = 0;
	private int param3 = 0;
	
	public Command(int type, BigInteger param1, int r1, BigInteger param2, int r2, int param3) {
		this.type = type;
		if(param1 != null)
			this.param1 = new BigInteger(param1.toString());
		if(param2 != null)
			this.param2 = new BigInteger(param2.toString());
		this.param3 = param3;
		this.r1 = r1;
		this.r2 = r2;
	}
	
	public int getType() {
		return type;
	}

	public BigInteger getParam1() {
		if(param1 != null)
			return new BigInteger(param1.toString());
		else
			return null;
	}

	public int getR1() {
		return r1;
	}

	public BigInteger getParam2() {
		if(param2 != null)
			return new BigInteger(param2.toString());
		else
			return null;
	}

	public int getR2() {
		return r2;
	}

	public int getParam3() {
		return param3;
	}
	
}