package ojmChat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientJoinUI extends JFrame implements ActionListener {
    ClientProtocol cp = null;

    // 선언부
    JLabel label_id = new JLabel("ID Input");
    JLabel label_pw = new JLabel("PW Input");
    JLabel label_pw_ch = new JLabel("PW Check");
    JTextField txt_f_id = new JTextField("hogi");
    JPasswordField txt_f_pw = new JPasswordField("12345");
    JPasswordField txt_f_pw_ch = new JPasswordField("12346");
    JButton btn_re_join = new JButton("Request Join");
    JButton btn_upd_acc = new JButton("Update Account");


    // 생성자
    public ClientJoinUI(ClientLoginUI clogin) {
        this.cp = clogin.cp;
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
        this.add(btn_upd_acc);
        btn_re_join.setBounds(35, 270, 110,30);
        btn_upd_acc.setBounds(165, 270, 130, 30);

        // 윈도우 레이아웃 정의
        this.setTitle("Join Page");
        this.setSize(350, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(false);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        String pw_origin = txt_f_pw.getText();
        String pw_check = txt_f_pw_ch.getText();

        if (obj == btn_re_join && pw_origin.equals(pw_check)) {
            System.out.println("insert");

        } else if (obj == btn_upd_acc && pw_origin.equals(pw_check)) {
            System.out.println("update");

        } else {
            JOptionPane.showMessageDialog(this, "비밀번호가 다릅니다.");
        }
    }
}
