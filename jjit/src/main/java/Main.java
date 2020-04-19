public class Main {
	public static void main(String[] args) {
		int s = add(1, 2);
		System.out.printf("sum: %d%n", s);
		
		int fn = fib(7);
		System.out.printf("fn = %d%n", fn);
	}
	
	private static int add(int a, int b) {
		return a + b;
	}
	
	private static int fib(int n) {
		if (n < 2) {
			return n;
		}
		
		return fib(n-1) + fib(n-2);
	}
}
