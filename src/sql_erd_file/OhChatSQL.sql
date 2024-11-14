
/***********************
***** DB 조회 쿼리문 *****
**********************/

select * from talk_room;

select * from member;

select * from room_member;

select * from message;



/**********************************************
***** ProjectDAO 쿼리문 (for connect Java) *****
*********************************************/

-- member 테이블 CRUD 퀴리문
-------------------------
insert into member values (?,?,?,?);

delete from member where mem_nick = ? and mem_pw = ?;

update member set mem_nick_change = mem_nick, mem_nick = ? 
where mem_ip = ? and mem_pw = ?;



-- talk_room CRUD 쿼리문
-----------------------
select count(*) from talk_room where talk_room_name = ?;

insert into talk_room values (talk_room_seq.nextval,?);



-- message 테이블 CRUD 쿼리문
--------------------------
insert into message 
values (seq_msg_no.nextval, ?, to_char(SYSDATE, 'yyyy-mm-dd hh24:mi'), ?, 
(select talk_room_id from talk_room where talk_room_name = ?));

select /*+ INDEX(mg idx_message_date_time_msg_id) */ mg.msg 
from message mg 
join talk_room tr on mg.talk_room_id = tr.talk_room_id 
where tr.talk_room_name = ? 
ORDER BY mg.date_time, mg.msg_id;



-- room_member 테이블 CRUD 쿼리문
------------------------------
insert into room_member 
values ((select mem_ip from member where mem_nick = ?), 
(select talk_room_id from talk_room where talk_room_name = ?));

select mem_nick from room_member rm 
join talk_room tr on rm.talk_room_id = tr.talk_room_id 
join member mem on rm.mem_ip = mem.mem_ip 
where tr.talk_room_name = ?;



/********************************
***** Procedure & Sequence *****
*******************************/

-- Procedure
--------------
CREATE OR REPLACE PROCEDURE SCOTT.chat_join (
    mem_nick_in IN MEMBER.MEM_NICK%TYPE,
    mem_pw_in IN MEMBER.MEM_PW%TYPE,
    is_true OUT NUMBER
) IS
BEGIN
    -- 사용자의 닉네임과 비밀번호가 일치하는 레코드가 있는지 확인하고 is_true에 반영
    SELECT CASE 
               WHEN COUNT(*) > 0 THEN 1 
               ELSE 0 
           END 
    INTO is_true
    FROM MEMBER
    WHERE MEM_NICK = mem_nick_in
      AND MEM_PW = mem_pw_in;
END;
/


-- Sequence
-------------
CREATE SEQUENCE SCOTT.TALK_ROOM_SEQ
  START WITH 21
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
CREATE SEQUENCE SCOTT.SEQ_MSG_NO
  START WITH 41
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;