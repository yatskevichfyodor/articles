
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

function addComment(comment) {
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
