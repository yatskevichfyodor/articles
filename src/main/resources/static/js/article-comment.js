
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var stompClient = null;

connect();

function connect() {
    var socket = new SockJS('/comment-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/comments', function (comment) {
            addComment(JSON.parse(comment.body));
            $('#textarea-comment').html("");
            $('#btn-comment-add').prop('disabled', false);
        });
    });
}

$('#btn-comment-add').click(function() {
    $(this).prop('disabled', true);

    if (!$.trim($('#textarea-comment').val())) return;

    var comment = {
        text: $("#textarea-comment").val(),
        articleId: getId()
    }

    stompSaveComment(comment);
});

//$('#btn-del').click(function() {
function delComment() {
    var requestBody = {
        id : $('#btn-del').attr('value')
    }

    $.ajax({
        type: 'DELETE',
        url: '/comment',
        data: JSON.stringify(requestBody),
        contentType: 'application/json; charset=utf-8',
        dataType: 'json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(id) {
            $('#comment' + id).empty();
            console.log("Comment was deleted successfully");
        },
        error : function(e) {
            alert("Comment deletion error");
            console.log("ERROR: ", e);
        }
    });
//});
}

function getId() {
    var url = window.location.pathname;
    var segments = url.split('/');
    console.log(segments);
    return segments[2];
}

function stompSaveComment(comment) {
    stompClient.send("/comment/save", {}, JSON.stringify(comment));
}

function addComment(comment) {
    $("#comments").append('\n' +
        '            <div class="row" id="comment' + comment.id + '">\n' +
        '                <div class="col-md-6 border border-left-0 border-right-0">\n' +
        '                    <div class="row mt-1">\n' +
        '                        <div class="col-md-5">\n' +
        '                            <span><b>by</b>\n' +
        '                                <a href="/user/' + comment.author + '" class="font-weight-bold"\n>' + comment.author +
        '                                </a>\n' +
        '                            </span>\n' +
        '                        </div>\n' +
        '                        <span class="col-md-5">' + comment.timestamp + '</span>\n\n' +
        '                        <div>\n' +
        '                            <div class="col-md-2">\n' +
        '                                <span onclick="delComment();" class="clickable-icon" id="btn-del"\n' +
        '                                      value="' + comment.id + '">\n' +
        '                                    <i class="far fa-trash-alt  mr-2 mt-2"></i>\n' +
        '                                </span>\n' +
        '                            </div>\n' +
        '                        </div>' +
        '                    </div>\n' +
        '                    <p class="mt-1">' + comment.text + '</p>\n' +
        '                </div>\n' +
        '            </div>');

    // $("#textarea-comment").value = '';
    document.getElementById('textarea-comment').value = "";
}
