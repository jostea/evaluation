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

function viewCurrentTask() {
    var test = $(".theTest");
    test.children().each(function () {
        if ($(this).hasClass("current")) {
            $(this).show();
        } else {
            $(this).hide();
        }
    });

    if ($("#skillsDivFragment").hasClass("current")) {
        $("#previousTaskPage").hide();
    } else {
        $("#previousTaskPage").show();
    }
    var test = $(".theTest").children();
    test.each(function (index, el) {
        if ($(this).hasClass("current") && $(this).next() !== null) {
            if (index === test.length - 1) {
                $("#nextTaskPage").hide();
            } else {
                $("#nextTaskPage").show();
            }
        }
    });
}