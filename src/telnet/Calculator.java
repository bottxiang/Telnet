package telnet;
import java.util.Stack;

public class Calculator {
	// current用来记录表达式中当前正在读取的字符的索引
	static int current = 0;
	public static char relation[][] = {
			/* "+ - * / ( ) #"间的优先关系 */
			{'>', '>', '<', '<', '<', '>', '>'},
			{'>', '>', '<', '<', '<', '>', '>'},
			{'>', '>', '>', '>', '<', '>', '>'},
			{'>', '>', '>', '>', '<', '>', '>'},
			{'<', '<', '<', '<', '<', '=', '1'},
			{'>', '>', '>', '>', '2', '>', '>'},
			{'<', '<', '<', '<', '<', '3', '='}
		};
	public static void error(char errorCode)
	{
		switch (errorCode) {
			case '1': 
				System.out.printf("缺右括号\")\"\n");
				break;
			case '2':
				System.out.printf("\")\" 和 \"(\"\n");
				break;
			case '3':
				System.out.printf("缺左括号\"(\"\n");
				break;
		}
	}
	public static boolean isDigit(char c)
	{
		if (c >= '0' && c <= '9')
			return true;
		else
			return false;
	}
	public static int getIndex(char c)
	{
		int index = -1;
		switch (c)
		{
		case '+':
			index = 0;
			break;
		case '-':
			index = 1;
			break;
		case '*':
			index = 2;
			break;
		case '/':
			index = 3;
			break;
		case '(':
			index = 4;
			break;
		case ')':
			index = 5;
			break;
		case '#':
			index = 6;
			break;
		}
		return index;
	}
	public static char precede(char a, char b)
	{
		return relation[getIndex(a)][getIndex(b)];
	}
	public static int getToken(String s)
	{
		char c;
		int n = 0;
		int i = current;
		while (s.charAt(i) == ' ')
		{	
			i++;
		}
		c = s.charAt(i);
		while (isDigit(c))
		{
			n = n * 10 + (c - '0');
			i++;
			c = s.charAt(i);
		}
		// 读到的是运算符
		if (n == 0) {
			current = i + 1;
			return (int)c;
		}
		// 读到的是操作数
		else
		{
			current = i;
			return n;
		}
	}

	public static int operate(char c, int num1, int num2)
	{
		int res = 0;
		switch (c)
		{
		case '+':
			res = num1 + num2;
			break;
		case '-':
			res = num1 - num2;
			break;
		case '*':
			res = num1 * num2;
			break;
		case '/':
			res = num1 / num2;
			break;
		}
		return res;
	}
	public static int expression(String expr)
	{

		Stack<Integer> numStack = new Stack<>();
		Stack<Character> optStack = new Stack<>();
		optStack.push('#');
		int c = getToken(expr);
		while (c != '#' || optStack.peek() != '#')
		{
			if (c != '+' && c != '-' && c != '*' && c != '/' && c != '(' && c != ')' && c != '#')
			{
				numStack.push(c);
				c = getToken(expr);
			} 
			else
			{
				switch (precede(optStack.peek().charValue(), (char)c))
				{
				case '<':
					optStack.push((char)c);
					c = getToken(expr);
					break;
				case '>':
					int b = numStack.pop();
					int a = numStack.pop();
					numStack.push(operate(optStack.pop(), a, b));
					break;
				case '=':
					optStack.pop();
					c = getToken(expr);
					break;
				default:
					error((char)c);
				}
			}
		}
		// 本次计算结束,重置用来表示正在读取的表达式中字符的索引的current
		current = 0;
		return numStack.peek();
	}
	public static void main(String[] args) {
		System.out.printf("%d", Calculator.expression("32-5*2#"));
		System.out.printf("%d", Calculator.expression("32-5*2#"));
		System.out.printf("%d", Calculator.expression("32-5*2#"));
	}
}
