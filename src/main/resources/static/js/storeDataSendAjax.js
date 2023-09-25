function makecommute() {
    $.ajax({
        url: "/makecommute",
        type: "POST",
        dataType: "text"

    }).done(function (response) {
        if (response === "이미출근") {
            // 이미 출근 처리가 된 경우 처리
            alert("이미 출근 처리가 되었습니다.");
        } else {
            // 출근 처리 결과를 표시
            alert("출근하셨습니다.");
            $("#nowtext").text("현재 출근 상태: 출근");
            $("#text").text("출근 시각 "+response);
        }
    }).fail(function (error) {
        $("#nowtext").replaceWith(error);
    });
}

function takearest() {
    $.ajax({
        url: "/takearest",
        type: "POST",
        dataType: "text"

    }).done(function (response) {
        if (response === "오류") {
            // 이미 출근 처리가 된 경우 처리
            alert("처리 오류로 신청이 반려되었습니다. 반복될 경우 관리자에게 문의하세요.");
        } else if(response==="성공") {
            // 출근 처리 결과를 표시
            alert("외출이 승인되었습니다.");
            $("#nowtext").text("현재 출근 상태: 외출");
            $("#takearestbutton").val("복귀");
            $("#takearestbutton1").val("복귀");
        } else if(response==="복귀"){
            alert("복귀하셨습니다.")
            $("#nowtext").text("현재 출근 상태: 출근");
            $("#takearestbutton").val("외출");
            $("#takearestbutton1").val("외출");
        } else if(response==="이미"){
            alert("출근 전, 퇴근 후에는 외출 신청이 불가능합니다.")
        }
    }).fail(function (error) {
        $("#nowtext").replaceWith(error);
    });
}

function quitcommute() {
    $.ajax({
        url: "/quitcommute",
        type: "POST",
        data: { result: resultValue }, // 데이터 전송
        dataType: "text"
    }).done(function (response) {
        if (response === "이미퇴근") {
            // 이미 출근 처리가 된 경우 처리
            alert("이미 퇴근 처리가 되었습니다.");
        } else if(response === "아직못감"){
            alert("퇴근 시간 전입니다.");
        } else if(response==="외출"){
            alert("외출 중엔 퇴근 할 수 업습니다.")
        } else {
            // 출근 처리 결과를 표시
            alert("퇴근하셨습니다");
            $("#nowtext").text("현재 출근 상태: 퇴근");
            $("#text2").text("퇴근 시각 "+response);
        }
    }).fail(function (error) {
        $("#nowtext").replaceWith(error);
    });
}


function reset() {
    $.ajax({
        url: "/reset",
        type: "POST",
        dataType: "text"
    }).done(function () {
       alert("초기화완료")
    }).fail(function (error) {
    });
}
