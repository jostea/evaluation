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

function saveAll() {
    saveOneSqlAnswer();
}

function loadSimpleTasks(data) {
    let body = "";
    for (let i = 0; i < data.length; i++) {
        switch (data[i].taskType) {
            case "Multi Choice Question":
                body += "<div class='multiTask' value='multiTask'>";
                body += "<div hidden>" + data[i].id + "</div>";
                body += "<h4>" + data[i].title + "</h4>";
                body += "<h6>" + data[i].taskType + "</h6>";
                body += "<p>" + data[i].description + "</p>";
                for (let j = 0; j < data[i].allAnswerOptions.length; j++) {
                    body += "<div class='checkbox'>";
                    body += "<label>";
                    body += "<div hidden>" + data[i].allAnswerOptions[j].id + "</div>";
                    body += "<input type='checkbox' name='multiCheck'>" + data[i].allAnswerOptions[j].answerOptionValue + "";
                    body += "</label>";
                    body += "</div>";
                }
                body += "</div>";
                break;
            case "Single Choice Question":
                body += "<div class='singleTask' value='singleTask'>";
                body += "<div hidden>" + data[i].id + "</div>";
                body += "<h4>" + data[i].title + "</h4>";
                body += "<h6>" + data[i].taskType + "</h6>";
                body += "<p>" + data[i].description + "</p>";
                for (let j = 0; j < data[i].allAnswerOptions.length; j++) {
                    body += "<div id='singleAO" + j + "' class='checkbox'>";
                    body += "<label>";
                    body += "<div hidden>" + data[i].allAnswerOptions[j].id + "</div>";
                    body += "<input type='checkbox' name='singleCheck" + data[i].id + "' class='singleChecks'>" + data[i].allAnswerOptions[j].answerOptionValue + "";
                    body += "</label>";
                    body += "</div>";
                }
                body += "</div>";
                break;
            case "Custom Question":
                body += "<div class='customTask' value='customQuestion'>";
                body += "<div hidden>" + data[i].id + "</div>";
                body += "<h4>" + data[i].title + "</h4>";
                body += "<h6>" + data[i].taskType + "</h6>";
                body += "<p>" + data[i].description + "</p>";
                body += "<textarea id='customAnswer" + data[i].id + "' class='form-control customAnswers' type='text' placeholder='Your answer'></textarea>";
                body += "</div>";
                break;
        }
    }
    $(".theTest").append(body);
}

$(".theTest").on('click', ".singleChecks", function () {
    let $box = $(this);
    if ($box.is(":checked")) {
        let group = "input:checkbox[name='" + $box.attr("name") + "']";
        $(group).prop("checked", false);
        $box.prop("checked", true);
    } else {
        $box.prop("checked", false);
    }
});

function saveMultiAnswers() {
    $.ajax({
        method: "POST",
        data: JSON.stringify(prepareDataForSavingMultiTask()),
        url: gOptions.aws_path + "/testsrest/saveMultiAnswers",
        contentType: "application/json",
        success: function () {
            console.log("multitask saved");
        },
        error: function (response) {
            console.log(response);
        }
    })
}

function saveSingleAnswers() {
    $.ajax({
        method: "POST",
        data: JSON.stringify(prepareDataForSavingSingleTask()),
        url: gOptions.aws_path + "/testsrest/saveSingleAnswer",
        contentType: "application/json",
        success: function () {
            console.log("single answer saved");
        },
        error: function (response) {
            console.log(response);
        }
    })
}
function saveCustomAnswer() {
    $.ajax({
        method: "POST",
        data: JSON.stringify(prepareDataForSavingCustomTask()),
        url: gOptions.aws_path + "/testsrest/saveCustomAnswer",
        contentType: "application/json",
        success: function () {
            console.log("custom answer saved");
        },
        error: function (response) {
            console.log(response);
        }
    })
}


function prepareDataForSavingMultiTask() {
    let multiSingleAnswers = $('.current input:checked');
    let multiAnswers = [];
    let candidateTaskId = multiSingleAnswers[0].parentNode.parentNode.parentNode.children[0].innerText;
    multiSingleAnswers.each(function () {
        multiAnswers.push(this.parentNode.children[0].innerHTML);
    });
    return {
        candidateTaskId: candidateTaskId,
        multiTaskAnswers: multiAnswers
    }
}

function prepareDataForSavingSingleTask() {
    let singleAnswerList = $('.current input:checked');
    let singleAnswer = singleAnswerList[0].parentNode.children[0].innerHTML;
    let candidateTaskId = singleAnswerList[0].parentNode.parentNode.parentNode.children[0].innerText;
    return {
        candidateTaskId: candidateTaskId,
        singleAnswer: singleAnswer
    }
}

function prepareDataForSavingCustomTask() {
    let singleAnswerList = $('.current');
    let taskId = singleAnswerList[0].children[0].innerText;
    let answerContent = singleAnswerList[0].children[4].value;
    return {
        taskId: taskId,
        answerContent: answerContent
    }
}

function displaySqlTasks(response) {
    response.sqlTasks.forEach(function (sqlTask) {
        let idSqlGroup = sqlTask.sqlGroup.id;
        let callToRestController = gOptions.aws_path + "/testsrest/getSqlImage/" + idSqlGroup;
        let content = `<div class="active" value="sqlTask">
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
                                    <div id="` + sqlTask.id + `" class="col-md-10 result">
<!--                                         <input id="sqlTask` + response.sqlTasks.indexOf(sqlTask) + `"type='hidden' value='\` + sqlTask.id + \`'>-->
<!--                                         <textarea id="sqlResponse` + response.sqlTasks.indexOf(sqlTask) + `"class="form-control" type="text" placeholder="Specify here your answer..."></textarea>                                    -->
                                         <textarea id="` + sqlTask.id + `" class="form-control" type="text" placeholder="Specify here your answer..."></textarea>                                    
                                    </div>
                                </div>
                        </form>
                        </div>`;
        $(".theTest").append(content);
    });
    // document.getElementById("sqltasks").innerHTML += `<div class="form-row row" style="margin-bottom: 100px">
    //     <button id="saveSqlAnswers" type="submit" class="btn btn-primary" onclick="saveSqlAnswers()">Save Sql Answers</button>
    // </div>`;
}

// AUTOSAVE FUNCTIONS

//save one active sql task
function saveOneSqlAnswer() {
    //get sql answer statements and sql task ids
    let answersList = [];

    let select = $(".current[value='sqlTask']").find('*').find('textarea').eq(0);
    let answer = {
        sqlTaskId: select.attr('id'),
        sqlAnswer: select.val()
    }
    answersList.push(answer);
    //get token
    let url = new URL(window.location.href);
    let thd_i8 = url.searchParams.get("thd_i8");

    //create object to post
    let result = {
        token: thd_i8,
        answers: answersList
    }

    //call rest post endpoint
    $.ajax({
        method: "POST",
        data: JSON.stringify(result),
        url: gOptions.aws_path + "/testsrest/saveOneSqlAnswer",
        contentType: "application/json",
        success: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    })
}

//save all sql tasks
function saveSqlAnswers() {
    //get sql answer statements and sql task ids
    let answersList = [];
    $(".current[value='sqlTask']").find('*').find('textarea').each(function () {
        let answer = {
            sqlTaskId: $(this).attr('id'),
            sqlAnswer: $(this).val()
        }
        answersList.push(answer);
    });

    answersList.push(answer);
    //get token
    let url = new URL(window.location.href);
    let thd_i8 = url.searchParams.get("thd_i8");

    //create object to post
    let result = {
        token: thd_i8,
        answers: answersList
    }

    //call rest post endpoint
    $.ajax({
        method: "POST",
        data: JSON.stringify(result),
        url: gOptions.aws_path + "/testsrest/saveSqlAnswers",
        contentType: "application/json",
        success: function (response) {
            console.log(response);
        },
        error: function (response) {
            console.log(response);
        }
    })
}