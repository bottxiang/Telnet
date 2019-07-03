package telnet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;


public class Command extends JFrame implements ActionListener{
	
	Socket s;
	BufferedReader br;
	PrintWriter pw;
	StringBuilder cmd = new StringBuilder();
	
	JPanel panel;
	JLabel exLabel, displayLabel, resultLabel;
	JButton computeButton, openNotepadButton, openMspaintButton, clear;
	JTextField input, result;
	JTextArea display;
	JPasswordField jpf;
	public Command() {
		
		panel = new JPanel();
		
		exLabel = new JLabel("输入表达式：");
		exLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
		exLabel.setBounds(30, 530, 300, 30);
		displayLabel = new JLabel("服务器回显");
		displayLabel.setBounds(400, 50, 200, 30);
		displayLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
		resultLabel = new JLabel("结果：");
		resultLabel.setFont(new Font("微软雅黑", Font.BOLD, 30));
		resultLabel.setBounds(50, 580, 300, 30);
		
		input = new JTextField(50);
		input.setFont(new Font("微软雅黑", Font.BOLD, 30));
		input.setBounds(200, 530, 450, 40);
		input.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		result = new JTextField(50);
		result.setFont(new Font("微软雅黑", Font.BOLD, 30));
		result.setBounds(200, 580, 100, 40);
		result.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		result.setBackground(Color.WHITE);
		result.setEditable(false);
		
		display = new JTextArea(5, 1);
		display.setFont(new Font("微软雅黑", Font.BOLD, 28));
		display.setBounds(200, 100, 600, 400);
		display.setLineWrap(true);
		display.setWrapStyleWord(true);
		display.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3));
		display.setEditable(false);
		
		computeButton = new JButton("计算");
		computeButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
		computeButton.setBounds(700, 530, 100, 40);
		clear = new JButton("清空");
		clear.setFont(new Font("微软雅黑", Font.BOLD, 30));
		clear.setBounds(750, 530, 100, 40);
		openNotepadButton = new JButton("记事本");
		openNotepadButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
		openNotepadButton.setBounds(700, 580, 150, 40);
		openMspaintButton = new JButton("画图");
		openMspaintButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
		openMspaintButton.setBounds(700, 630, 100, 40);
		panel.setLayout(null);
		
		panel.add(exLabel);
		panel.add(displayLabel);
		panel.add(resultLabel);
		panel.add(input);
		panel.add(result);
		panel.add(display);
		panel.add(computeButton);
		panel.add(openNotepadButton);
		panel.add(openMspaintButton);
	
		this.getContentPane().add(panel, BorderLayout.CENTER);
		
		//this.add(panel);
		// 建立Socket连接到服务器
		setUpNetworking();
		
		Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
		
		// 设置监听事件
		computeButton.addActionListener(this);
		openMspaintButton.addActionListener(this);
		openNotepadButton.addActionListener(this);
		
		// 屏幕显示器的宽和高
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setBounds((width-1040) / 2, (height-800) / 2, 1040, 800);
		this.setTitle("Telnet-Client");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1040, 800);
		this.setVisible(true);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Command();

	}
	public void setUpNetworking() {
		try {
			s = new Socket("localhost", 8888);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			pw = new PrintWriter(s.getOutputStream());
	        //pw.println("open calc");
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = br.readLine()) != null) {
                	if (message.indexOf("result") == 0) {
                		result.setText(message.substring("result".length()+1));
                	}
                    System.out.println("client read: " + message);
                    display.append(message + "\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand() == "计算") {
			pw.println("calculate " + input.getText());
			pw.flush();
		} else if (e.getActionCommand() == "清空") {
			input.setText("");
		} else if (e.getActionCommand() == "记事本") {
			pw.println("open notepad");
			pw.flush();
		} else if (e.getActionCommand() == "画图") {
			pw.println("open mspaint");
			pw.flush();
		}
		
	}

}
