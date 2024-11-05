package ojmChat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientJoinUI extends JFrame implements ActionListener {
    // 선언부
    JLabel label_id = new JLabel("ID Input");
    JLabel label_pw = new JLabel("PW Input");
    JLabel label_pw_ch = new JLabel("PW Check");
    JTextField txt_f_id = new JTextField("hogi");
    JPasswordField txt_f_pw = new JPasswordField("12345");
    JPasswordField txt_f_pw_ch = new JPasswordField("12346");
    JButton btn_re_join = new JButton("Request Join");


    // 생성자
    public ClientJoinUI() {
        inDisplay();
    }


    // 화면처리부
    public void inDisplay() {
        btn_re_join.addActionListener(this);

        // default layout 해제
        this.setLayout(null);

        // ID & Password
        this.add(label_id);
        this.add(txt_f_id);
        this.add(label_pw);
        this.add(txt_f_pw);
        this.add(label_pw_ch);
        this.add(txt_f_pw_ch);
        label_id.setBounds(35, 100, 185, 40);
        txt_f_id.setBounds(110, 100, 185, 40);
        label_pw.setBounds(35, 150, 185, 40);
        txt_f_pw.setBounds(110, 150, 185, 40);
        label_pw_ch.setBounds(35, 200, 185, 40);
        txt_f_pw_ch.setBounds(110, 200, 185, 40);

        // 버튼 추가
        this.add(btn_re_join);
        btn_re_join.setBounds(90, 250, 150,30);

        // 윈도우 레이아웃 정의
        this.setTitle("Join Page");
        this.setSize(350, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String pw_origin = txt_f_pw.getText();
        String pw_check = txt_f_pw_ch.getText();

        if (obj == btn_re_join && pw_origin.equals(pw_check)) {
            System.out.println(pw_check);
        } else {
            JOptionPane.showMessageDialog(this, "비밀번호가 다릅니다.");
        }
    }
}
