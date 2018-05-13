var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

$("#submit").on("click", function () {
    submit();
})

function submit() {
    ajaxSendQuery($('#query').val());
}

function ajaxSendQuery(query) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/sql-request" + "?request=" + query,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        // dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            $('#response').empty();
            $('#response').append(response);
        },
        error: function (e) {
            console.log("ERROR " + e);
        }
    });
}