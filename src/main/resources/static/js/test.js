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
            displayCodeTasks(response.codeTasks);
            getSqlAnswers();
            getTestTime();
            viewCurrentTask();
        },
        error: function (response) {
            console.log(response);
        }
    });
});

function saveCurrentTask(currentDiv) {
    switch (currentDiv.attr("value")) {
        case "skills":
            updateSkills();
            break;
        case "multiTask":
            saveMultiAnswers();
            break;
        case "singleTask":
            saveSingleAnswers();
            break;
        case "customQuestion":
            saveCustomAnswer();
            break;
        case "sqlTask":
            saveOneSqlAnswer();
            break;
        case "codeTask":
            saveCodeAnswer();
            break;
    }
}

function getTestTime() {
    let url = new URL(window.location.href);
    let param = url.searchParams.get("thd_i8");
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/getLeftTime/" + param,
        success: function (response) {
            $("#timerTimeId").html(`
                    <h3 class="title-with-time" id="head">Time Left</h3>
                    <ul>
                        <li class="line-with-time"><span id="hours"></span>Hours</li>
                        <li class="line-with-time"><span id="minutes"></span>Minutes</li>
                        <li class="line-with-time"><span id="seconds"></span>Seconds</li>
                     </ul>`);
            let interval = setInterval(function () {
                if (response >= 0) {
                    document.getElementById('hours').innerText = Math.floor(response / 3600) + "";
                    document.getElementById('minutes').innerText = Math.floor(response % 3600 / 60) + "";
                    document.getElementById('seconds').innerText = Math.floor(response % 60) + "";
                    response--;
                } else {
                    clearInterval(interval);
                    clearInterval(saveInterval);
                    window.location.reload();
                }
            }, 1000);
        }
    });
}

let saveInterval = setInterval(function () {
    $(".theTest").children().each(function () {
        if ($(this).hasClass("current")) {
            saveCurrentTask($(this));
            return false;
        }
    });
}, 5000);

function loadSimpleTasks(data) {
    let body = "";

    function candidateChecked(answerOption, idx) {
        if (data[idx].taskType === "Multi Choice Question" || data[idx].taskType === "Single Choice Question") {
            for (let j = 0; j < data[idx].candidateAnswers.length; j++) {
                if (answerOption.id === data[idx].candidateAnswers[j].id) {
                    return true;
                }
            }
            return false;
        }
    }

    for (let i = 0; i < data.length; i++) {
        switch (data[i].taskType) {
            case "Multi Choice Question":
                body += "<div class='multiTask' value='multiTask'>";
                body += "<div hidden>" + data[i].id + "</div>";
                body += "<h4>" + data[i].title + "</h4>";
                body += "<h6>" + data[i].taskType + "</h6>";
                body += "<p> " + data[i].description + "</p>";
                for (let j = 0; j < data[i].allAnswerOptions.length; j++) {
                    body += "<div class='checkbox'>";
                    body += "<label>";
                    body += "<div hidden>" + data[i].allAnswerOptions[j].id + "</div>";
                    if (candidateChecked(data[i].allAnswerOptions[j], i)) {
                        body += "<input type='checkbox' name='multiCheck' checked='true'>" + data[i].allAnswerOptions[j].answerOptionValue + "";
                    } else {
                        body += "<input type='checkbox' name='multiCheck'>" + data[i].allAnswerOptions[j].answerOptionValue + "";
                    }
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
                body += "<p> " + data[i].description + "</p>";
                for (let j = 0; j < data[i].allAnswerOptions.length; j++) {
                    body += "<div id='singleAO" + j + "' class='checkbox'>";
                    body += "<label>";
                    body += "<div hidden>" + data[i].allAnswerOptions[j].id + "</div>";
                    if (candidateChecked(data[i].allAnswerOptions[j], i)) {
                        body += "<input type='radio' name='singleCheck" + data[i].id + "' class='singleChecks' checked='true'> " + data[i].allAnswerOptions[j].answerOptionValue + "";
                    } else {
                        body += "<input type='radio' name='singleCheck" + data[i].id + "' class='singleChecks'> " + data[i].allAnswerOptions[j].answerOptionValue + "";
                    }
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
                body += "<p> " + data[i].description + "</p>";
                if (data[i].candidateAnswers.length > 0) {
                    body += "<textarea id='customAnswer" + data[i].id + "' class='form-control customAnswers' type='text' placeholder='Your answer'>" + data[i].candidateAnswers[0].answerOptionValue + "</textarea>";
                } else {
                    body += "<textarea id='customAnswer" + data[i].id + "' class='form-control customAnswers' type='text' placeholder='Your answer'></textarea>";
                }
                body += "</div>";
                break;
        }
    }
    $(".theTest").append(body);
}

function displayCodeTasks(tasks) {
    let body = "";
    for (let i = 0; i < tasks.length; i++) {
        body += "<div class='codeTask' value='codeTask'>";
        body += "<div hidden>" + tasks[i].id + "</div>";
        body += "<h4>" + tasks[i].title + "</h4>";
        body += "<p>" + tasks[i].description + "</p>";
        if (tasks[i].codeProvided) {
            body += "<textarea class='form-control codeAnswer' type='text' rows='15'>" + tasks[i].codeProvided + "</textarea>";
        } else {
            body += "<textarea class='form-control codeAnswer' type='text' rows='15'>" + tasks[i].signature + "</textarea>";
        }
        body += "</div>";
    }
    $(".theTest").append(body);
}

function finishTest() {
    $(".theTest").children().each(function () {
        if ($(this).hasClass("current")) {
            saveCurrentTask($(this));
        }
    });
    $('#confirmSubmit').modal('show');
};

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

$('#confirmSubmit').on('click', '#comfirmSubmitTest', function (e) {
        // $form.trigger('submit');
        let url_current = new URL(window.location.href);
        let token = url_current.searchParams.get("thd_i8").valueOf();
        $.ajax({
            method: "POST",
            // data: JSON.stringify(prepareDataForSavingMultiTask()),
            url: gOptions.aws_path + "/testsrest/testFinish",
            data: {thd_i8: token},
            success: function () {
                window.location.href = gOptions.aws_path + "/finish/" + token;
            },
            error: function (response) {
                window.location.href = gOptions.aws_path + "/finish/" + token;
            }
        })
    }
)

function saveMultiAnswers() {
    if (prepareDataForSavingMultiTask()) {
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
}

function saveSingleAnswers() {
    if (prepareDataForSavingSingleTask()) {
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
}

function saveCustomAnswer() {
    if (prepareDataForSavingCustomTask()) {
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
}

function saveCodeAnswer() {
    if (prepareDataForSavingCodeTask()) {
        $.ajax({
            method: "POST",
            data: JSON.stringify(prepareDataForSavingCodeTask()),
            url: gOptions.aws_path + "/testsrest/saveCodeAnswer",
            contentType: "application/json",
            success: function () {
            },
            error: function (response) {
                console.log(response);
            }
        })
    }
}


function prepareDataForSavingMultiTask() {
    let multiSingleAnswers = $('.current input:checked');
    if (multiSingleAnswers.length > 0) {
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
}

function prepareDataForSavingSingleTask() {
    let singleAnswerList = $('.current input:checked');
    if (singleAnswerList.length > 0) {
        let singleAnswer = singleAnswerList[0].parentNode.children[0].innerHTML;
        let candidateTaskId = singleAnswerList[0].parentNode.parentNode.parentNode.children[0].innerText;
        return {
            candidateTaskId: candidateTaskId,
            singleAnswer: singleAnswer
        }
    }
}

function prepareDataForSavingCustomTask() {
    let singleAnswerList = $('.current');
    if (singleAnswerList.length > 0) {
        let taskId = singleAnswerList[0].children[0].innerText;
        let answerContent = singleAnswerList[0].children[4].value;
        return {
            taskId: taskId,
            answerContent: answerContent
        }
    }
}

function prepareDataForSavingCodeTask() {
    let codeAnswer = $('.current');
    let url = new URL(window.location.href);
    let token = url.searchParams.get("thd_i8");
    if (codeAnswer.length > 0) {
        let taskId = codeAnswer[0].children[0].innerText;
        let answerContent = codeAnswer[0].children[3].value;
        return {
            id: taskId,
            code: answerContent,
            token: token
        }
    }
}

function displaySqlTasks(response) {
    response.sqlTasks.forEach(function (sqlTask) {
        let idSqlGroup = sqlTask.sqlGroup.id;
        let callToRestController = gOptions.aws_path + "/testsrest/getSqlImage/" + idSqlGroup;
        let content = `<div class="" value="sqlTask">
                        <form>
                                <div class="form-row row">
                                        <div>
                                            <div class="row">
                                                <h4 class="col-form-label">` + sqlTask.title + `</h4>
                                            </div>
                                            <br>
                                            <div class="row">
                                                <label class="col-form-label" style="text-align: justify">` + sqlTask.description + `</label>
                                            </div>
                                        </div>
                                        <div>
                                             <img id="sqlGroupImage" src=` + callToRestController + ` style="max-width: 400px; max-width: 100%; max-height: 100%">
                                        </div>
                                </div>
                                <div class="form-row row">
                                    <div class="col-md-2">
                                        <label class="col-form-label">Please specify your SQL statement</label>
                                    </div>
                                    <div id="` + sqlTask.id + `" class="col-md-10 result">
                                         <textarea id="` + sqlTask.id + `" class="form-control" type="text" placeholder="Specify here your answer..."></textarea>                                    
                                    </div>
                                </div>
                        </form>
                        </div>`;
        $(".theTest").append(content);
    });
}

function displaySqlAnswers(data) {
    $("[value='sqlTask']").find('*').find('textarea').each(function () {
        for (let i = 0; i < data.length; i++) {
            let id = parseInt($(this).attr("id"), 10);
            if (id == data[i].sqlTaskId && data[i].sqlAnswer) {
                $(this).val(data[i].sqlAnswer);
            }
        }
    })
}

function getSqlAnswers() {
    let url_current = new URL(window.location.href);
    let token = url_current.searchParams.get("thd_i8").valueOf();
    $.ajax({
        method: "GET",
        url: gOptions.aws_path + "/testsrest/getSqlAnswers/" + token,
        success: function (response) {
            console.log(response);
            displaySqlAnswers(response);
        },
        error: function (response) {
            console.log(response);
        }
    });

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
    let url_current = new URL(window.location.href);
    let thd_i8 = url_current.searchParams.get("thd_i8");

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
    };

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