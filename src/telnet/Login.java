package telnet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Login extends JFrame implements ActionListener{
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	JPanel wPanel, loginPanel;
	JLabel userLabel, passwordLabel, jlbw;
	JButton loginButton, jb2;
	JTextField jtf;
	JPasswordField jpf;
	public Login() {
		
		jlbw = new JLabel("登  录");
		jlbw.setFont(new Font("黑体", Font.BOLD, 50));
		
		userLabel = new JLabel("用户名");
		userLabel.setBounds(200, 130, 100, 30);
		userLabel.setFont(new Font("黑体", Font.BOLD, 30));
		
		passwordLabel = new JLabel("密  码");
		passwordLabel.setBounds(200, 200, 100, 30);
		passwordLabel.setFont(new Font("黑体", Font.BOLD, 30));
		
		jtf = new JTextField(20);
		jtf.setFont(new Font("黑体", Font.BOLD, 30));
		jtf.setBounds(300, 130, 250, 40);
		jpf = new JPasswordField(20);
		jpf.setFont(new Font("serif", Font.BOLD, 30));
		jpf.setBounds(300, 200, 250, 40);
		
		
		loginButton = new JButton("登录");
		loginButton.setFont(new Font("黑体", Font.BOLD, 30));
		loginButton.setBounds(250, 350, 100, 40);
		jb2 = new JButton("取消");
		jb2.setFont(new Font("黑体", Font.BOLD, 30));
		jb2.setBounds(420, 350, 100, 40);
		
		wPanel = new JPanel();
		loginPanel = new JPanel();
		
		loginPanel.setLayout(null);
		
		
		wPanel.add(jlbw);
		loginPanel.add(userLabel);
		loginPanel.add(jtf);
		loginPanel.add(passwordLabel);
		loginPanel.add(jpf);
		loginPanel.add(loginButton);
		loginPanel.add(jb2);
		
		this.getContentPane().add(BorderLayout.NORTH, wPanel);
		this.getContentPane().add(BorderLayout.CENTER, loginPanel);
	
		
		// 设置监听事件
		loginButton.addActionListener(this);
		jb2.addActionListener(this);
	
		this.setTitle("Telnet-Login");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setBounds((width-600) / 2, (height-600) / 2, 800, 600);
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Login();
	}
	public void setUpNetworking() {
		try {
			s = new Socket("localhost", 8888);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "登录") {
			String username = jtf.getText();
			String password = jpf.getText();
			if (username.equals("") || password.equals("")) {
				JOptionPane.showMessageDialog(null, "用户名或密码为空", "Warning~", JOptionPane.WARNING_MESSAGE);
				jtf.setText("");
				jpf.setText("");
			} else if (!username.equals("") && !password.equals("")){
				
				setUpNetworking();
				
				pw.println("auth " + username + " " + password);
				pw.flush();
				String message;
	            try {
	            	// 读取服务器返回的“连接成功”消息
                	message = br.readLine();
                	System.out.println("client read: " + message);
                	// 登录成功
                	message = br.readLine();
                	System.out.println("client read: " + message);
                	if (message.equals("OK")) {
                		JOptionPane.showMessageDialog(null, "登录成功！", "Warning~", JOptionPane.WARNING_MESSAGE);
                		dispose();
                		new Command();
                	// 登录失败
                	} else if (message.equals("WRONG")){
                		JOptionPane.showMessageDialog(null, "用户名或密码错误\n请重新输入", "Warning~", JOptionPane.WARNING_MESSAGE);
        				jtf.setText("");
        				jpf.setText("");
                	}
	                    
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
			}
		} else if (e.getActionCommand() == "取消") {
			dispose();
		}
		
	}

}
