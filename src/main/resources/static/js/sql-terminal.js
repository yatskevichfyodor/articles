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
        url: "/sqlRequest" + "?query=" + query,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (response) {
            $('#query').value = response;
        }
    });
}