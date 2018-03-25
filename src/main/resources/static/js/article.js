
// $('#btn-comment-add').click(function() {
//     if (isEmpty()) return;
//
//     var comment = {
//         text: $("#textarea-comment").val(),
//         articleId: getId()
//     }
//
//     addComment(comment);
// });
//
//
// function getId() {
//     var url = window.location.pathname;
//     var segments = url.split('/');
//     console.log(segments);
//     return segments[2];
// }
//
// function addComment(comment) {
//     var json = JSON.stringify(comment);
//     $.ajax({
//         type: 'PUT',
//         url: '/comment',
//         data: json,
//         contentType: 'application/json; charset=utf-8',
//         dataType: 'json',
//         success : function(data) {
//             console.log("SUCCESS: ", data);
//             //$("#comment-block").load(location.href+" #comment-block>*","");
//         },
//         error : function(e) {
//             console.log("ERROR: ", e);
//
//         },
//         done : function(e) {
//             console.log("DONE");
//         }
//     });
// }

// //$('#btn-del').click(function() {
// function delComment() {
//     var token = $('#_csrf').attr('content');
//     var header = $('#_csrf_header').attr('content');
//
//     $.ajax({
//         type: 'DELETE',
//         url: '/comment',
//         data: $('#btn-del').attr('value'),
//         contentType: 'application/json; charset=utf-8',
//         dataType: 'json',
//         beforeSend: function(xhr) {
//             xhr.setRequestHeader(header, token);
//         },
//         success : function(data) {
//             console.log("SUCCESS: ", data);
//         },
//         error : function() {
//             console.log("ERROR");
//         }
//     });
// //});
// }
var stompClient = null;

connect();

function connect() {
    var socket = new SockJS('/comment-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/comments', function (comment) {
            showGreeting(JSON.parse(comment.body));
        });
    });
}

$('#btn-comment-add').click(function() {
    if (!$.trim($('#textarea-comment').val())) return;

    var comment = {
        text: $("#textarea-comment").val(),
        articleId: getId()
    }

    sendComment(comment);
});

function getId() {
    var url = window.location.pathname;
    var segments = url.split('/');
    console.log(segments);
    return segments[2];
}

function sendComment(comment) {
    stompClient.send("/comment/save", {}, JSON.stringify(comment));
}

function showGreeting(comment) {
    $("#comments").append('\n' +
        '            <div class="row">\n' +
        '                <div class="col-md-6 border border-left-0 border-right-0">\n' +
        '                    <div class="row mt-1">\n' +
        '                        <div class="col-md-6">\n' +
        '                            <span>by\n' +
        '                                <a href="/user/' + comment.author + '" class="font-weight-bold"\n>' + comment.author +
        '                                </a>\n' +
        '                            </span>\n' +
        '                        </div>\n' +
        '                        <span class="col-md-6">' + comment.timestamp + '</span>\n' +
        '                    </div>\n' +
        '                    <p class="mt-1">' + comment.text + '</p>\n' +
        '                </div>\n' +
        '            </div>')
}

$(function () {
    $( "#send" ).click(function() { sendName(); });
});