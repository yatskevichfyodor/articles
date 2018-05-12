
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var updateCommentErrorsSet = new Set();

var updateCommentErrorsMap = {
    1: 'comment_length'
}

var editStarted = false;
var ajaxInProcess = false;

var initialComment;
var comment;

function editComment(commentIdParam) {
    if (editStarted) return;
    editStarted = true;

    initialComment = {
        id: commentIdParam,
        author: $('#comment-author-' + commentIdParam).text(),
        timestamp: $('#comment-timestamp-' + commentIdParam).text(),
        text: $('#comment-text-' + commentIdParam).text()
    }

    comment = initialComment;

    $('#comment-' + commentIdParam).empty();
    $('#comment-' + commentIdParam).append('' +
        '                <div class="col-md-6 mt-2">\n' +
        '                  <textarea class="form-control textarea" rows="3" id="comment-edit-textarea">' + comment.text + '</textarea>' +
        '                </div>' +
        '                <div class="col-md-1 mt-2">' +
        '                  <div class="row">' +
        '                    <button type="submit" class="btn btn-info editable-submit" onclick="editCommentSubmit()">' +
        '                      <i class="fas fa-check-circle"></i>\n' +
        '                    </button>' +
        '                  </div>' +
        '                  <div class="row">' +
        '                    <button type="submit" class="btn editable-cancel" onclick="editCommentCancel()">' +
        '                      <i class="fas fa-minus-circle"></i>\n' +
        '                    </button>' +
        '                  </div>' +
        '                </div>' +
        '                <div class="col-md-5">' +
        '                  <div id="comment-edit-errors" class="col-md-12 errors"></div>' +
        '                </div>');
}


function editCommentSubmit() {
    if (ajaxInProcess) return;

    comment.text = $('#comment-edit-textarea').val();

    validateComment();
    if (updateCommentErrorsSet.size == 0) {
        ajaxInProcess = true;
        ajaxUpdateComment();
    }
}

function editCommentCancel() {
    if (ajaxInProcess) return;

    comment = initialComment;
    closeInput();
}

function updateErrorsArea() {
    $('#comment-edit-errors').empty();
    updateCommentErrorsSet.forEach(function (value) {
        $('#comment-edit-errors').append('<p>' + LANG[updateCommentErrorsMap[value]] + '</p>');
    });
}

function validateComment() {
    var valid = true;

    if (comment.text.length < 1 || comment.text.length > 999) {
        valid = false;
        updateCommentErrorsSet.add(1);
    } else {
        updateCommentErrorsSet.delete(1);
    }

    if (valid) {
        $('#comment-edit-textarea').removeClass('is-invalid').addClass('is-valid');
    } else {
        $('#comment-edit-textarea').removeClass('is-valid').addClass('is-invalid');
    }

    updateErrorsArea();
}

function ajaxUpdateComment() {
    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/comment/edit",
        data: JSON.stringify(comment),
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (commentDto) {
            comment = commentDto;
            ajaxInProcess = false;

            closeInput();
        },
        error: function (errors) {
            ajaxInProcess = false;

            errors.forEach(function (value) {
                updateCommentErrorsSet.add(value);
            });
            updateErrorsArea()
        }
    });
}

function closeInput() {
    editStarted = false;

    $('#comment-' + comment.id).empty();
    $('#comment-' + comment.id).append('\n' +
        '                <div class="col-md-6 border border-left-0 border-right-0">\n' +
        '                    <div class="row mt-1">\n' +
        '                        <div class="col-md-5">\n' +
        '                            <span><b>by</b>\n' +
        '                                <a id="comment-author-' + comment.id + '" href="/user/' + comment.author + '" class="font-weight-bold">' + comment.author + '</a>\n' +
        '                            </span>\n' +
        '                        </div>\n' +
        '                        <span id="comment-timestamp-' + comment.timestamp + '" class="col-md-5">' + comment.timestamp + '</span>\n' +
        '                        <div class="text-right">\n' +
        '                            <span onclick="editComment(' + comment.id + ')" class="clickable-icon" id="btn-edit">\n' +
        '                                <i class="fas fa-edit"></i>\n' +
        '                            </span>\n' +
        '                            <span \n' +
        '                                      onclick="delComment()" class="clickable-icon" id="btn-del"\n' +
        '                                      value="' + comment.id + '">\n' +
        '                                <i class="fas fa-trash-alt  mr-2 mt-2"></i>\n' +
        '                            </span>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                    <div class="pl-3 row">\n' +
        '                        <p class="mt-1" id="comment-text-' + comment.id + '">' + comment.text + '</p>\n' +
        '                    </div>\n' +
        '                </div>');
    ;
}