function nextTask() {
    var test = $(".theTest").children();
    test.each(function (index, el) {
        if ($(this).hasClass("current") && $(this).next() !== null) {
            if (index < test.length - 1) {
                saveCurrentTask($(this));
                $(this).removeClass("current");
                test.eq(index + 1).addClass("current");
                return false;
            }
        } else {
            $(this).hide();
        }
    });
    viewCurrentTask();

}

function setMarker() {
    $(".theTest").children().each(function (index, el) {
        if ($(this).hasClass("current")) {
            $('#button' + index).addClass('btn-primary');
        } else {
            $('#button' + index).removeClass('btn-primary');
        }
    });
}

function previousTask() {
    var test = $(".theTest").children();
    test.each(function (index, el) {
        if ($(this).hasClass("current") && $(this).prev() !== null) {
            if (index > 0) {
                saveCurrentTask($(this));
                $(this).removeClass("current");
                test.eq(index - 1).addClass("current");
                return false;
            }
        } else {
            $(this).hide();
        }
    });
    viewCurrentTask();
}

function addNumberOfPage() {
    var test = $(".theTest").children();
    test.each(function (index, el) {
        $(this).addClass('page' + index);
    });

    let previousButton = `<button class="btn" id="previousTaskPage" onclick="previousTask()">Previous</button>`;

    let nextButton = `
                <button class="btn" id="nextTaskPage" onclick="nextTask()">Next</button>`;
    nextButton += `<button id="saveActiveTasks" type="submit" class="btn btn-primary" onclick="saveCurrentTask()">Submit Test</button> `;

    let buttonsOfPagination = ``;
    test.each(function (index, el) {
        buttonsOfPagination += `<li class="paginationButtons">
                <button class="btn" id="button${index}" onclick="rebaseOnSpecifiedPage('page'+${index},${index})">${index + 1}</button>
            </li>`;
    });

    $('#numbersOfPage').html(buttonsOfPagination);
    $('#divForNextButton').html(nextButton);
    $('#divForPreviousButton').html(previousButton);
}

function rebaseOnSpecifiedPage(numberOfPage, i) {
    $(".theTest").children().each(function () {
        if ($(this).hasClass('current')) {
            saveCurrentTask($(this));
        }
    });

    var test = $(".theTest").children();
    test.each(function (index, el) {
        if ($(this).hasClass(numberOfPage)) {
            $(this).addClass("current");
        } else {
            $(this).removeClass("current");
            $(this).hide();
        }
    });

    viewCurrentTask();

    $('#button' + i).addClass('btn-primary');
}

function viewCurrentTask() {
    var test = $(".theTest");
    test.children().each(function () {
        if ($(this).hasClass("current")) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });
    addNumberOfPage();

    $(".theTest").children();
    test.each(function (index, el) {
        if ($(this).hasClass("current")) {
            $('#button' + index).addClass('btn-primary');
        }
    });

    if (test.children().first().hasClass("current")) {
        $("#previousTaskPage").hide();
    } else {
        $("#previousTaskPage").show();
    }

    if ($('.theTest').children().last().hasClass("current")) {
        $("#nextTaskPage").hide();
        $("#saveActiveTasks").show();
    } else {
        $("#nextTaskPage").show();
        $("#saveActiveTasks").hide();
    }
    setMarker();
}