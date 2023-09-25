package com.example.prsample.controller;

import com.example.prsample.dto.Day;
import com.example.prsample.dto.Dept;
import com.example.prsample.dto.Working;
import com.example.prsample.service.StuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class CommuteController {

    @Autowired
    StuService stuService;

    @GetMapping("/make")
    public void makego(){
        int empno=22;
        stuService.resetworking(empno);
    //초기화임
    }
    @GetMapping("/mail/sendmail/{empno}")
    public String sendmail(@PathVariable int empno, Model model){
        model.addAttribute("empno", empno);

        return "send";
    }

    @PostMapping("/sendgo")
    public String sendTest(@RequestParam("empno") int empno, @RequestParam("title") String title){
        System.out.println("============================================");

        System.out.println(title);
        System.out.println(empno);
        return "sendgo";
    }


@GetMapping("/start")
public String startpage(Model model){


    //이 컨트롤러는 로그인 직후 페이지를 상정. empno는 session에서 사번을 가져올거고
    //해당 사번으로 db에서 일정을 조회하여 오늘의 근무 형태를 검색할 것임. ex > 정상근무 or 반차 or 휴가 or 공휴일 등 
    // 정상 근무라면 아래 코드를 그대로 띄워주고 반차면 4시간으로 줄인 switch & case 돌리기. ,휴가 or 공휴일이면 session에 setssion으로 
    //오늘 근뮤 형태를 넣어줌. 모든 페이지에선 th:if로 세션의 오늘 근무 형태를 따라 내용을 시작하면 될 듯

    int empno=22;
    int deptno=1100;
    Working working=stuService.getlogininfo(empno);
    Date last=working.getLastday();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String lastday=sdf.format(last);

    Date toda=new Date();
    String today=sdf.format(toda);

    System.out.println("============================");
    System.out.println(lastday);
    System.out.println(today);

    if(!lastday.equals(today)){
        stuService.resetworking(empno);
        stuService.updatelastday(today,empno);
    }


    String doweek=stuService.getdoweek();

    String result="";
    int month=9;
    Day day=stuService.typecheck(empno,month);

    int type=0;

    switch (doweek){
        case "월요일": type=day.get월요일();
        break;
        case "화요일": type=day.get화요일();
        break;
        case "수요일": type=day.get수요일();
        break;
        case "목요일": type=day.get목요일();
        break;
        case "금요일": type=day.get금요일();
        break;
    }


    switch (type){
        case 1: result="8시 출근 5시 퇴근";
            break;
        case 2: result="9시 출근 6시 퇴근";
            break;
        case 3: result="10시 출근 7시 퇴근";
            break;
    }


    List<Dept> list=stuService.deptlist(deptno);

    model.addAttribute("list",list);
    model.addAttribute("result",result);
    model.addAttribute("working",working);
    return "hi";

}

    @PostMapping("/makecommute")
    @ResponseBody
    public String processData() {

        int empno=22;
        LocalDateTime start=LocalDateTime.now();
        String starttime=start.toString();
        String tttime=starttime.substring(11, 16);
        System.out.println("================================"+tttime);

        String responseData=tttime;

        String ischul=stuService.ischul1(empno);

        if (ischul.equals("o")||ischul.equals("oo")){

            responseData="이미출근";
        } else {

            stuService.updateWork(tttime,empno);

        }

        return responseData;
    }

    @PostMapping("/takearest")
    @ResponseBody
    public String takeaRest(){
        int empno=22;
        String responseData="";
        Working working=stuService.getlogininfo(empno);

        if(!working.getIsworking().equals("외출")){
            try{
                stuService.updaterest(empno);
                responseData="성공";
            } catch (Exception e){
                responseData="오류";
            }
        } else if(working.getIsworking().equals("외출")){

            try{
                stuService.finishrest(empno);
                responseData="복귀";
            } catch (Exception e){
                responseData="오류";
            }

        }





        return responseData;
    }


    @PostMapping("/reset")
    public void reset(){
        int empno=22;
        stuService.resetworking(empno);
    }

    @PostMapping("/quitcommute")
    @ResponseBody
    public String quitcommute(@RequestParam("result") String resultValue){

        int empno=22;
        Working working=stuService.getlogininfo(empno);




        LocalDateTime start=LocalDateTime.now();

        String starttime=start.toString();
        String tttime=starttime.substring(11, 16);

        String responseData=tttime;


        if(working.getIsworking().equals("외출")){
            responseData="외출";
            return responseData;
        }


        String ischul=stuService.ischul1(empno);
        LocalTime hometime = LocalTime.of(18, 0, 0);
        LocalDateTime now = LocalDateTime.now();
        LocalTime time = now.toLocalTime();

        if(resultValue.equals("9시 출근 6시 퇴근")){
            hometime = LocalTime.of(18, 0, 0);
        } else if (resultValue.equals("10시 출근 7시 퇴근")) {
            hometime=LocalTime.of(19,0,0);
        } else if (resultValue.equals("8시 출근 5시 퇴근")) {
            hometime=LocalTime.of(17,0,0);
        }


        boolean canIgoHome=hometime.isBefore(time);
        if (ischul.equals("oo")){

            responseData="이미퇴근";
        } else if (!canIgoHome) {
            responseData="아직못감";
        } else {
            stuService.updatego(tttime,empno);
        }

        return responseData;

    }


}
