package ojmDB;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MemVO {
    // 선언부
    private String mem_ip;
    private String mem_nick;
    private int mem_pw;
}