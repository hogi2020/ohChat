package ojmChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientLoginUI extends JFrame implements ActionListener {
    // 선언부 | 클래스 선언
    ClientUI cui;
    ClientJoinUI cjoin;

    // 선언부
    JLabel label_id = new JLabel("아이디");
    JLabel label_pw = new JLabel("패스워드");
    JTextField txt_f_id = new JTextField("hogi");
    JPasswordField txt_f_pw = new JPasswordField("12345");
    JButton btn_login = new JButton("로그인");
    JButton btn_join = new JButton("회원가입");


    // 생성자
    public ClientLoginUI() {
        cui = new ClientUI(this);
        cjoin = new ClientJoinUI(cui);
        inDisplay();
    }


    // 화면처리부
    public void inDisplay() {
        btn_login.addActionListener(this);
        btn_join.addActionListener(this);

        // default layout 해제
        this.setLayout(null);

        // ID & Password
        this.add(label_id);
        this.add(txt_f_id);
        this.add(label_pw);
        this.add(txt_f_pw);
        label_id.setBounds(35, 100, 185, 40);
        txt_f_id.setBounds(110, 100, 185, 40);
        label_pw.setBounds(35, 150, 185, 40);
        txt_f_pw.setBounds(110, 150, 185, 40);

        // 버튼 추가
        this.add(btn_join);
        this.add(btn_login);
        btn_join.setBounds(45, 250, 100,30);
        btn_login.setBounds(175, 250, 100,30);

        // 윈도우 레이아웃 정의
        this.setTitle("LogIn Page");
        this.setSize(350, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();

        if (obj == btn_login) {
            cui.cp.sendMsg("LoginCheck#" + txt_f_id.getText() + "/" + txt_f_pw.getText());
        } else if (obj == btn_join) {
            cjoin.setVisible(true);
        }
    }


    public static void main(String[] args) {
        new ClientLoginUI();
    }
}
