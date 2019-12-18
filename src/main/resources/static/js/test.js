$(document).ready(function () {
    let url = new URL(window.location.href);
    let param = url.searchParams.get("thd_i8");
    $.ajax({
        method: "POST",
        data: {thd_i8: param},
        url: gOptions.aws_path + "/testsrest/testStart",
        success: function (response) {
            console.log(response);
            loadSimpleTasks(response.tasks);
        },
        error: function (response) {
            console.log(response);
        }
    })
});

function loadSimpleTasks(data) {
    let multiTasks = "";
    let singleTasks = "";
    let customTasks = "";
    for (let i = 0; i < data.length; i++) {
        switch (data[i].taskType) {
            case "Multi Choice Question":
                multiTasks+=drawMultiTask(data[i]);
                break;
            case "Single Choice Question":
                singleTasks+=drawSingleTask(data[i]);
                break;
            case "Custom Question":
                customTasks+=drawCustomTask(data[i]);
                break;
        }
    }
    $("#multiTasks").html(multiTasks);
    $("#singleTasks").html(singleTasks);
    $("#customTasks").html(customTasks);
}

function drawMultiTask(task) {
    let body = "";
    body+="<div class='multiTask'>";
    body+="<div hidden>"+ task.id +"</div>";
    body+="<h2>"+ task.title +"</h2>";
    body+="<h6>"+ task.taskType +"</h6>";
    body+="<p>"+ task.description +"</p>";
    for (let i=0; i<task.allAnswerOptions.length; i++) {
        body+="<div class='checkbox'>";
        body+="<label>";
        body+="<input type='checkbox'>" + task.allAnswerOptions[i].answerOptionValue + "";
        body+="</label>";
        body+="</div>";
    }
    body+="</div>";
    return body;
}

function drawSingleTask(task) {
    let body = "";
    body+="<div class='singleTask'>";
    body+="<div hidden>"+ task.id +"</div>";
    body+="<h2>"+ task.title +"</h2>";
    body+="<h6>"+ task.taskType +"</h6>";
    body+="<p>"+ task.description +"</p>";
    for (let i=0; i<task.allAnswerOptions.length; i++) {
        body+="<div id='singleAO"+ i +"' class='checkbox'>";
        body+="<label>";
        body+="<input type='checkbox' name='singleChecks"+ task.id +"' class='singleChecks'>" + task.allAnswerOptions[i].answerOptionValue + "";
        body+="</label>";
        body+="</div>";
    }
    body+="</div>";
    return body;
}

function drawCustomTask(task) {
    let body = "";
    body+="<div class='customTask'>";
    body+="<div hidden>"+ task.id +"</div>";
    body+="<h2>"+ task.title +"</h2>";
    body+="<h6>"+ task.taskType +"</h6>";
    body+="<p>"+ task.description +"</p>";
    body+="<textarea id='customAnswer"+ task.id +"' class='form-control' type='text' placeholder='Your answer'></textarea>";
    body+="</div>";
    return body;
}

$("#singleTasks").on('click', ".singleChecks", function() {
    let $box = $(this);
    if ($box.is(":checked")) {
        let group = "input:checkbox[name='" + $box.attr("name") + "']";
        $(group).prop("checked", false);
        $box.prop("checked", true);
    } else {
        $box.prop("checked", false);
    }
});