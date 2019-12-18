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
            displaySqlTasks(response);
        },
        error: function (response) {
            console.log(response);
        }
    })
});


function displaySqlTasks(response) {
    response.sqlTasks.forEach(function (sqlTask) {

        let idSqlGroup = sqlTask.sqlGroup.id;
        let callToRestController = gOptions.aws_path + "/testsrest/getSqlImage/" + idSqlGroup;
        // $("#sqlGroupImage").attr("src", callToRestController);

        let content = `<input id="sqlTask` + response.sqlTasks.indexOf(sqlTask) + `"type='hidden' value='` + sqlTask.id + `'>
                        <form>
                                <div class="form-row row">
                                        <div class="col-md-6">
                                            <div class="row">
                                                <h4 class="col-form-label">` + sqlTask.title + `</h4>
                                            </div>
                                            <br>
                                            <div class="row">
                                                <label class="col-form-label" style="text-align: justify">` + sqlTask.description + `</label>
                                            </div>
                                        </div>
                                        
                                        <div class="col-md-6">
                                                <img id="sqlGroupImage" src=` + callToRestController + ` style="max-width: 400px; max-width: 100%; max-height: 100%">
                                        </div>
                                </div>
                                <div class="form-row row">
                                    <div class="col-md-2">
                                        <label class="col-form-label">Please specify your SQL statement</label>
                                    </div>
                                    <div class="col-md-10">
                                         <textarea id="sqlResponse` + response.sqlTasks.indexOf(sqlTask) + `"class="form-control" type="text" placeholder="Specify here your answer..."></textarea>                                    
                                    </div>
                                </div>
                        </form>
                        <br>
                        <hr>`;

        document.getElementById("sqltasks").innerHTML += content;


    });
}


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