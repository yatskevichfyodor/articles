
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var articleId = $('#articleId').attr('content');

//////////////////////////
// Validation block
var errorsSet = new Set();

$('#content').change(function () {
    validateContent();
});

function validateContent() {
    var content = $('#content').val();

    if (content.length < 1000 || content.length > 100000) {
        errorsSet.add(LANG.content_length);
    } else {
        errorsSet.delete(LANG.content_length);
    }

    showErrors();
}

function showErrors() {
    $('#errors_list').empty();
    errorsSet.forEach(function (value) {
        $('#errors_list').append('<p>' + value + '</p>');
    });
}

function validateArticle() {
    validateContent();
    if (errorsSet.size > 0) {
        event.preventDefault();
        return false
    }

    return true;
}

$(document).ready(function () {
    $("#submit").click(function () {
        $('#submit').prop("disabled",true);

        if (!validateArticle()) {
            $('#submit').prop("disabled", false);
            return;
        }

        var article = {
            id: articleId,
            categoryId: $('#category').val(),
            content: $('#content').val()
        }

        ajaxEditArticle(article);
    });
});


function ajaxEditArticle(article) {

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/article/edit",
        data: JSON.stringify(article),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function () {
            window.location.replace('/article/' + articleId);
        },
        error: function (e) {
            $('#submit').prop("disabled", false);
            alert("error ");
            console.log("error " + e);
            // window.location.replace('/article/' + articleId);
        }
    });
}