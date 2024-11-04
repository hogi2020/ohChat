package ojmDB;

public class memDTO {
    /// 선언부
    String mem_ip;
    String mem_nick;
    String mem_nick_change;
    int mem_pw;


    /// 생성자 생성
    public memDTO() {}
    public memDTO(String mem_ip, String mem_nick, String mem_nick_change, int mem_pw) {
        this.mem_ip = mem_ip;
        this.mem_nick = mem_nick;
        this.mem_nick_change = mem_nick_change;
        this.mem_pw = mem_pw;
    }


    /// getter & setter 메서드 생성
    public String getMem_ip() {
        return mem_ip;
    }

    public void setMem_ip(String mem_ip) {
        this.mem_ip = mem_ip;
    }

    public int getMem_pw() {
        return mem_pw;
    }

    public void setMem_pw(int mem_pw) {
        this.mem_pw = mem_pw;
    }

    public String getMem_nick() {
        return mem_nick;
    }

    public void setMem_nick(String mem_nick) {
        this.mem_nick = mem_nick;
    }

    public String getMem_nick_change() {
        return mem_nick_change;
    }

    public void setMem_nick_change(String mem_nick_change) {
        this.mem_nick_change = mem_nick_change;
    }
}
