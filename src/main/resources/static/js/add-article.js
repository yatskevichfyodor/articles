
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
var methodOfStoringPictures = $('#methodOfStoringPictures').attr('content');

//////////////////////////
// Validation block
var errorsSet = new Set();

$('#title').change(function () {
    validateTitle();
});

$('#content').change(function () {
    validateContent();
});

function validateTitle() {
    var title = $('#title').val();
    title.trim();

    var valid = true;

    if (title.length < 4 || title.length > 32) {
        valid = false;
        errorsSet.add(LANG.title_length);
    } else {
        errorsSet.delete(LANG.title_length);
    }

    if (valid) {
        $('#title').removeClass('is-invalid').addClass('is-valid');
        ajaxCheckTitle(title);
    } else {
        $('#title').removeClass('is-valid').addClass('is-invalid');
    }

    showErrors();
}
function validateTitleField() { // Check after ajax request. Because it's possible when title length error can occur, but ajax result can pull off invalid state
    if (errorsSet.has(LANG.title_length)) {
        $('#username').removeClass('is-valid').addClass('is-invalid');
    } else {
        $('#username').removeClass('is-invalid').addClass('is-valid');
    }
}

function validateContent() {
    var content = $('#content').val();
    var valid = true;

    if (content.length < 1000 || content.length > 100000) {
        valid = false;
        errorsSet.add(LANG.content_length);
    } else {
        errorsSet.delete(LANG.content_length);
    }

    showErrors();
}

function validateDropzone() {
    var valid = true;

    if ((dropzone.getAcceptedFiles().size = 0) || (dropzone.getRejectedFiles() > 0)) {
        valid = false;
        errorsSet.add(LANG.dropzone_invalid);
    } else {
        errorsSet.delete(LANG.dropzone_invalid);
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
    validateDropzone();
    validateTitle();
    validateContent();
    if (errorsSet.size > 0) {
        event.preventDefault();
        return false
    }

    return true;
}

function ajaxCheckTitle(title) {

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/checkIfTitleNotExists",
        data: title,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (result) {
            if (result) {
                errorsSet.delete(LANG.title_duplicate);
                showErrors();
                validateTitleField();
            } else {
                $('#title').removeClass('is-valid').addClass('is-invalid');
                errorsSet.add(LANG.title_duplicate);

                showErrors();
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            if (xhr.status == 404) {
                console.log(thrownError);
            }
        }
    });
}

$(document).ready(function () {

    $("#submit").click(function () {
        $('#submit').prop("disabled",true);

        if (!validateArticle()) {
            $('#submit').prop("disabled", false);
            return;
        }

        if (methodOfStoringPictures === 'server') {
            uploadImageOnServer();
            return;
        }
        if (methodOfStoringPictures === 'cloud') {
            continueArticleUpload(uploadImageOnCloud());
            return;
        }
        if (methodOfStoringPictures === 'database') {
            continueArticleUpload(dropzone.getAcceptedFiles()[0].dataURL);
            return;
        }
        alert("Error. Unexpected method of storing pictures")
    });
});

function continueArticleUpload(fileSrc) {
    var article = {
        categoryId: $('#category').val(),
        title: $('#title').val(),
        content: $('#content').val(),
        imageData: fileSrc
    }

    ajaxSaveArticle(article);
}

function ajaxSaveArticle(article) {

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/article/add",
        data: JSON.stringify(article),
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (id) {
            window.location.replace('/article/' + id);
        },
        error: function () {
            $('#submit').prop("disabled", false);
            alert("error");
        }
    });
}