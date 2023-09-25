package com.example.prsample.service;

import com.example.prsample.dao.SampleDao;
import com.example.prsample.dto.Day;
import com.example.prsample.dto.Dept;
import com.example.prsample.dto.Working;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class StuService {
    private final SampleDao sampledao;

    @Autowired
    public StuService(SampleDao sampledao) {
     this.sampledao = sampledao;
    }



    public String ischul1(int empno){
        return sampledao.chul1(empno);
    }


    public void updateWork(String cometime,int empno){
        sampledao.updateWork(cometime,empno);
    }

    public void updatego(String gotime, int empno){
        sampledao.updatego(gotime,empno);
    }


    public Day typecheck(int empno, int month){
           return sampledao.typecheck(empno,month);
    }

    public Working getlogininfo(int empno){
        return sampledao.getlogininfo(empno);
    }

    public void resetworking(int empno){
        sampledao.resetworking(empno);
    }

    public List<Dept> deptlist(int deptno){ return sampledao.deptlist(deptno); }

    public void updatelastday(String today, int empno){sampledao.updatelastday(today,empno); }

    public String getdoweek(){
        LocalDate today = LocalDate.now();

        // 요일을 가져와서 문자열로 변환
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        String todayAsString = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN);


        return todayAsString;
    }

    public void updaterest(int empno){sampledao.updaterest(empno);}

    public void finishrest(int empno){sampledao.finishrest(empno);}
 }
