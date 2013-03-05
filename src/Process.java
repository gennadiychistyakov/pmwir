import java.math.BigInteger;


public class Process implements Runnable {
	private PMwIR owner;
	private int ID;
	private Command[] program;
	private int currentCommand;
	private Registers registers;
	private BigInteger number;
	private Thread thread;
	
	public Process(PMwIR owner, int ID, Command[] program, Registers registers, BigInteger number) {
		this.owner = owner;
		this.ID = ID;
		this.program = program;
		this.registers = registers;
		this.number = number;
		this.currentCommand = 0;
		
		thread = new Thread(this, String.valueOf(this.ID));
		thread.start();
	}
	
	public int getProcessID() {
		return this.ID;
	}

	private void Z(BigInteger number) throws InterruptedException {
		while(!registers.getRegister(number).setValue(BigInteger.valueOf(0), this))
			Thread.sleep(owner.getDelay());
		currentCommand++;
	}
	
	private void S(BigInteger number) throws InterruptedException {
		while(!registers.getRegister(number).setValue(registers.getRegister(number).getValue().add(BigInteger.valueOf(1)), this))
			Thread.sleep(owner.getDelay());
		currentCommand++;
	}
	
	private void T(BigInteger number0, BigInteger number1) throws InterruptedException {
		while(!registers.getRegister(number1).setValue(registers.getRegister(number0).getValue(), this))
			Thread.sleep(owner.getDelay());
		currentCommand++;
	}
	
	private void J(BigInteger number0, BigInteger number1, int newCommand) {
		if(registers.getRegister(number0).getValue().equals(registers.getRegister(number1).getValue()))
			currentCommand = newCommand - 1;
		else
			currentCommand++;
	}
	
	private void O(BigInteger number) {
		if(owner.isPromt())
			owner.write("Value of the register number " + number.toString() + " is ");
		owner.write(registers.getRegister(number).getValue().toString() + "\n");
		currentCommand++;
	}
	
	private void I(BigInteger number) throws InterruptedException {
		if(owner.isPromt())
			owner.write("Enter the new value of the register number " + number.toString() + ": ");
		BigInteger newValue = new BigInteger(owner.read());
		while(!registers.getRegister(number).setValue(newValue, this))
			Thread.sleep(owner.getDelay());
		currentCommand++;
	}
	
	private void G(BigInteger number) throws InterruptedException {
		while(!registers.getRegister(number).changeOwner(this))
			Thread.sleep(owner.getDelay());
		currentCommand++;
	}
	
	private void P(BigInteger number) {
		registers.getRegister(number).free(this);
		currentCommand++;
	}
	
	private void s(int program, BigInteger number) {
		owner.startProcess(program - 1, number);
		currentCommand++;
	}
	
	public void run() {
		try {
			while(!registers.getRegister(number).setValue(BigInteger.valueOf(0), this))
					Thread.sleep(owner.getDelay());
			while(currentCommand >= 0 && currentCommand < program.length) {
				if(owner.isDebug()) 
					owner.write("Process with ID " + ID + " execute command number " + currentCommand + "\n");
				switch(program[currentCommand].getType()) {
					case Command._Z: Z(program[currentCommand].getParam1());
									 break;
					case Command._S: S(program[currentCommand].getParam1());
									 break;
					case Command._T: T(program[currentCommand].getParam1(), program[currentCommand].getParam2());
									 break;
					case Command._J: J(program[currentCommand].getParam1(), program[currentCommand].getParam2(), program[currentCommand].getParam3());
									 break;
					case Command._I: I(program[currentCommand].getParam1());
									 break;
					case Command._O: O(program[currentCommand].getParam1());
									 break;
					case Command._G: G(program[currentCommand].getParam1());
									 break;
					case Command._P: P(program[currentCommand].getParam1());
									 break;
					case Command._s: s(program[currentCommand].getParam3(), program[currentCommand].getParam1());
									 break;
				}
				Thread.sleep(owner.getDelay());
			}
			while(!registers.getRegister(number).setValue(BigInteger.valueOf(1), this))
					Thread.sleep(owner.getDelay());
		} catch (InterruptedException e) {
			owner.write("Undefined error while sleeping thread with ID " + ID + "\n");
		} catch (Exception e) {
			owner.write("Undefined error in thread with ID " + ID + "\n");
		}
	}
}