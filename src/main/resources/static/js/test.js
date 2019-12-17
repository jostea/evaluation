$(document).ready(function () {
    let url = new URL(window.location.href);
    let param = url.searchParams.get("thd_i8");
    $.ajax({
        method: "POST",
        data: {thd_i8: param},
        url: "/testsrest/testStart",
        success: function (response) {
            console.log(response)
        },
        error: function (response) {
            console.log(response);
        },
    })
});