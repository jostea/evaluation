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

    let previousButton = `<button class="btn btn-sm btn-primary" id="previousTaskPage" onclick="previousTask()">Previous</button>`;

    let nextButton = `<button class="btn btn-sm btn-primary" id="nextTaskPage" onclick="nextTask()">Next</button>`;
    nextButton += `<button id="saveActiveTasks" type="submit" class="btn btn-sm btn-success" onclick="finishTest()">Submit</button>`;

    let buttonsOfPagination = ``;
    let open = true;
    test.each(function (index, el) {
        if (index%4===0) {
            if (open) {
                buttonsOfPagination+="<tr>";
                open = false;
            } else {
                buttonsOfPagination+="</tr>";
                open = true;
            }
        }
        buttonsOfPagination+="<td><button class='btn btn-default paginationButton' id='button"+ index +"' onclick='rebaseOnSpecifiedPage(&apos;page&apos;+"+ index +","+ index +")'>"+ (index+1) +"</button></td>";
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
            $('#button' + index).addClass('btn-endava');
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