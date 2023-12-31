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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
@Slf4j
public class CommuteController {

    @Autowired
    StuService stuService;

    @GetMapping("/make")
    public String makego(){
        int empno=25;
        stuService.resetworking(empno);
        return "send";
    //초기화임
    }
    @GetMapping("/mail/sendmail/{empno}")
    public String sendmail(@PathVariable int empno, Model model){
        model.addAttribute("empno", empno);

        return "send";
        
        //부서원에게 다이렉트 메일 보내기
        
    }

    @PostMapping("/sendgo")
    public String sendTest(@RequestParam("empno") int empno, @RequestParam("title") String title){
        System.out.println("============================================");

        System.out.println(title);
        System.out.println(empno);
        return "sendgo";
        
        //위에서 보내면 오는 컨트롤러 테스트용
    }


@GetMapping("/start")
public String startpage(Model model){



    int empno=25;  //세션에서 가져올 사원 번호
    int deptno=1100; //마찬가지로 부서 번호 
    Working working=stuService.getlogininfo(empno);  //오늘의 근무 상태를 일단 가져옴 empno로 
    Date last=working.getLastday(); //근무 상태에 있는 마지막으로 로그인 한 날을조회 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String lastday=sdf.format(last);
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
    Date toda=new Date();  //오늘 날짜를 조회 
    
    String today=sdf.format(toda);
    String todaysday=sdf2.format(toda);
    //두 날짜를 데이트포맷으로 깔끔하게 정리함 원활한 비교를 위해 


    if(!lastday.equals(today)){
        stuService.resetworking(empno);
        stuService.updatelastday(today,empno);
    }  
    //마지막 로그인 날자와 오늘 날자가 다르면 working 테이블을 초기화 함
    
    
    String newtoday="day"+todaysday;
    //휴가 여부 조회
    String vacation="";



    try{
        vacation= stuService.checkvacation(newtoday,empno);
        if(vacation.equals("휴가")){
            stuService.updatevacation(empno);
        }
    } catch (Exception e){

    }

    //만약 오늘이 한 달 스케쥴 테이블에 기록된 휴가 날이라면 vacation update로 dept와 working 테이블에 휴가 정보를 업데이트함 


   


    String doweek=stuService.getdoweek();
    //오늘이 무슨 요일인지 받아옴
    
    String result="";
    int month=9;
    Day day=stuService.typecheck(empno,month);

    //오늘 받아온 요일을 9월 스케쥴표에서 검색해서 오늘이 어떤 근무형태인지 switch case로 결정함

    int type=0; //디폴트값

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


    List<Dept> list=stuService.deptlist(deptno);  //부서번호로 전체 부서원 리스트 받아옴
    int numberToRemove = empno;   //전체 부서원 리스트에서 본인을 제외하기 위한 변수

    Iterator<Dept> iterator = list.iterator();
    while (iterator.hasNext()) {
        Dept dept = iterator.next();
        if (numberToRemove == dept.getEmpno()) {
            iterator.remove();
        }
    }// 이터레이터로 반복문을 돌려서 내 부서 리스트에 로그인 한 본인의 정보가 들어가지 않게 함. 본인은 메인페이지에 크게 나오기 때문 

    String deptname=list.get(0).getDeptname();

    model.addAttribute("deptname",deptname);
    model.addAttribute("list",list);
    model.addAttribute("result",result);
    model.addAttribute("working",working);
    model.addAttribute("vacation",vacation);
    return "hi";

}

    @PostMapping("/makecommute")
    @ResponseBody
    public String processData() {

        //출근 버튼 누르면 출근을 기록하기 위함
        
        
        int empno=25;
        LocalDateTime start=LocalDateTime.now();
        String starttime=start.toString();
        String tttime=starttime.substring(11, 16);
        System.out.println("================================"+tttime);

        String responseData=tttime;
        
        String ischul=stuService.ischul1(empno);
        //오늘의 중복 출근 여부를 찾음


        String doweek=stuService.getdoweek();
        //오늘이 무슨 요일인지 받아옴

        String result="";
        int month=9;
        Day day=stuService.typecheck(empno,month);

        //오늘 받아온 요일을 9월 스케쥴표에서 검색해서 오늘이 어떤 근무형태인지 switch case로 결정함

        int type=0; //디폴트값

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


        LocalTime hometime = LocalTime.of(18, 0, 0);

        if(type==1){
            hometime=LocalTime.of(17,0,0);
        } else if (type==2) {
            hometime=LocalTime.of(18,0,0);
        } else if (type==3) {
            hometime=LocalTime.of(19,0,0);
        }

        LocalDateTime now = LocalDateTime.now();
        LocalTime time = now.toLocalTime();

        if(time.isBefore(hometime)){
            stuService.updatetardy(empno,month);
        } //요일에서 출근 시간 받아오고 출근 눌렀을 때의 시간이 출근 시간보다 늦으면 지각 체크


        if (ischul.equals("o")||ischul.equals("oo")){

            responseData="이미출근";
        } else {

            stuService.updateWork(tttime,empno);

        }

        return responseData;
        
        //중복 출근을 했을 경우 이미 출근을 리턴해서 자바스크립트에서 이미 출근했다고 alert 시킴. 그게 아니라면 오늘 출근 날짜를 기록하고 출근했다는 기록을 각 db에 남김
        
    }

    @PostMapping("/takearest")
    @ResponseBody
    public String takeaRest(){
        
        //외출신청
        
        int empno=25;
        String responseData="";
       String  working=stuService.checkisworking(empno);
        //현재 근무 정보를 받아옴
        System.out.println(working+"======================================");


       if(working.equals("퇴근")||working.equals("출근전")){
            responseData="이미";
            return responseData;
        }
        //현재 근무정보가 퇴근이면 외출 버튼 클릭 불가
        //만약 휴가 날이라면 애초에 외출 버튼이 안나옴

        if(!working.equals("외출")){
            try{
                stuService.updaterest(empno);
                responseData="성공";
            } catch (Exception e){
                responseData="오류";
            }  // 현재 외출중, or 퇴근이 아니라면 외출 성공


        } else if(working.equals("외출")){

            try{
                stuService.finishrest(empno);
                responseData="복귀";
            } catch (Exception e){
                responseData="오류";
            } //이미 외출중이라면 외출에서 복귀함
        }

        return responseData;
    }


    @PostMapping("/reset")
    public void reset(){
        int empno=25;
        stuService.resetworking(empno);
        //초기화 버튼에서 돌아가는 초기화
        
    }

    @PostMapping("/quitcommute")
    @ResponseBody
    public String quitcommute(@RequestParam("result") String resultValue){

        int empno=25;
        Working working=stuService.getlogininfo(empno);
        int month=LocalDateTime.now().getMonthValue();
        //퇴근임


        LocalDateTime start=LocalDateTime.now();

        String starttime=start.toString();
        String tttime=starttime.substring(11, 16);

        String responseData=tttime;


        if(working.getIsworking().equals("외출")){
            responseData="외출";
            return responseData;
        }

        //외출중에는 퇴근이 불가능함


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

        //오늘 근무 타입에 따라 time 타입으로 퇴근시간을 잡음. 연산하기 위해서

        boolean canIgoHome=hometime.isBefore(time);

        //hometime(퇴근시간) 이 현재 시각보다 이전이라면 (아직 퇴근시간이 되지 않았다면) 퇴근 로직이 실행되지 않음


        if (ischul.equals("oo")){

            responseData="이미퇴근";
        } else if (!canIgoHome) {
            responseData="아직못감";
        } else {
            String tardy=stuService.istardytoday(empno);

            if(working.getExtrawork().equals("o")){
                int nowtime=time.getHour();
                int homeHour = hometime.getHour();
                int extraworktime=nowtime-homeHour;

                stuService.updateextrawork(extraworktime,empno,month);

            }




            if(!tardy.equals("o")){
                stuService.updateworktime(empno,month);
            } else if(tardy.equals("o")){
                String commutetimestr=working.getCometime();
                LocalTime commutetime=LocalTime.parse(commutetimestr);
                // 시간 정보를 int로 추출
                int commuteHour = commutetime.getHour();
                int homeHour = hometime.getHour();

                // 두 시간 값의 차 계산
                int hourDifference = homeHour - commuteHour;
                if(hourDifference>8){
                    hourDifference=8;
                }
                stuService.updateworkself(hourDifference,empno,month);
            } //해당 if문은 지각을 하지 않았으면 8시간을 근무 기록 하고 오늘 지각을 했으면 퇴근시간-출근시간으로 실제 근무 시간만을 기록함. 
            //지각하고 8시간 넘게 일해도 8시간만 기록됨
            //지각한 날은 연장 근무 기록이 불가능 하게 해둠.
            stuService.updatego(tttime,empno);
        }

        return responseData;

    }


    @GetMapping("/mypage")
    public String mypage(Model model){
        int empno=22;

        String authority=stuService.getAuthority(empno);

        model.addAttribute("authority",authority);
        return "mypage";
    }


}
