package com.example.prsample.dao;

import com.example.prsample.dto.Day;
import com.example.prsample.dto.Dept;
import com.example.prsample.dto.Working;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SampleDao {

    public void updateWork(String cometime,int empno);
    public void updatego(String gotime,int empno);

    public void resetworking(int empno);
    public String chul1(int empno);

    public Day typecheck(int empno, int month);

    public Working getlogininfo(int empno);

    public List<Dept>deptlist(int deptno);

    public void updatelastday(String today, int empno);

}
