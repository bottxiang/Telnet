package telnet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.*; 
import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class Server extends JFrame{
	BufferedReader in;
    PrintWriter out;
    Socket cs;
    String str;
	JLabel displayLabel;
	JTextArea display;
	Container panel; 
	public class ClientHandler implements Runnable {
        
        public ClientHandler(Socket cs) {
            try {
            	in = new BufferedReader(new InputStreamReader(cs.getInputStream())); //创建接收缓冲区
                out = new PrintWriter(cs.getOutputStream());//创建发送缓冲区    
                out.println("connect to server successfully!");
            } catch (Exception ex) { 
            	ex.printStackTrace(); 
            }
        }
        
        public void run() {
            String message;
            try {
            	while ((str = in.readLine()) != null) {
                	display.append("server read: " + str + "\n");
                	if (str.indexOf("auth") == 0) {
                		
                		String user_pass = str.substring("auth".length() + 1);
                		String[] arr1 = str.split(" ");
                		FileReader fr = new FileReader("users.txt");
                		BufferedReader br = new BufferedReader(fr);
                		String line;
                		while ((line=br.readLine()) != null) {
                			if (line.equals(user_pass)) {
                				display.append(line + "\n");
                				display.append("server read: login success\n");
                				out.println("OK");
                				out.flush();
                				cs.close();
                				break;
                			}
                		}
                		if (line == null) {
                			out.println("WRONG");
                			out.flush();
                		}
                	}else if (str.indexOf("open") == 0) {
                		
                		String cmd = str.substring("open".length()+1);
                		Process p = Runtime.getRuntime().exec(cmd);
                		out.println("open " + cmd + " successfully!");
                		out.flush();
                		
                	} else if (str.indexOf("calculate") == 0) {
                		
                		String expr = str.substring("calculate".length()+1);
                		//display.append(expr + "\n");
                		if (!expr.contentEquals("")) {
	                		int result = Calculator.expression(expr + "#");
	                		/*## */
	                		//display.append("result " + result + "\n");
	                		out.println("result " + result);
	                		out.flush();
                		}
                	}
                }
            } catch (Exception ex) { 
            	ex.printStackTrace(); 
            }
        }
    }
	public Server()throws IOException {
		
		JPanel panel = new JPanel();
		
		displayLabel = new JLabel("服务器端");
		displayLabel.setBounds(400, 50, 200, 30);
		displayLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
		
		display = new JTextArea(5, 1);
		display.setFont(new Font("微软雅黑", Font.BOLD, 28));
		display.setBounds(200, 100, 600, 600);
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		display.setEditable(false);
		
		
		panel.add(displayLabel);
		panel.add(display);
		
		panel.setLayout(null);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setBounds((width-1040) / 2, (height-800) / 2, 1040, 800);
		this.setTitle("Telnet-Server");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1040, 800);
		this.setVisible(true);
		
		ServerSocket ss = new ServerSocket(8888);//在端口8888创建监听socket
        while(true) {
        	cs = ss.accept();
            
            Thread t = new Thread(new ClientHandler(cs));
            t.start();
        }
	}
    public static void main(String argv[]) throws Exception 
    {
    	new Server();
    } 
} 
