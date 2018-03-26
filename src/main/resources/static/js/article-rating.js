
function getArticleId() {
    var url = window.location.pathname;
    var segments = url.split( '/' );
    return segments[2];
}

$('#like').click(function(){
    if ($(this).hasClass("far"))
        sendNewState("LIKE");
    else
        sendNewState("NONE");
});

$('#dislike').click(function(){
    if ($(this).hasClass('far'))
        sendNewState("DISLIKE");
    else
        sendNewState("NONE");
});

function sendNewState(state) {
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        url: '/article/' + getArticleId() + '/changeRating?ratingState=' + state,
        dataType: 'json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function(data) {
            updateRatingState(data);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function updateRatingState(data) {
    $('#like').removeClass('far');
    $('#like').removeClass('fas');
    $('#dislike').removeClass('far');
    $('#dislike').removeClass('fas');
    if (data.value == "LIKE") {
        $('#like').addClass('fas');
        $('#dislike').addClass('far');
    }
    if (data.value == "DISLIKE") {
        $('#dislike').addClass('fas');
        $('#like').addClass('far');
    }
    if (data.value == "NONE") {
        $('#dislike').addClass('far');
        $('#like').addClass('far');
    }
    $('#likes-number').text(data.likesNumber);
    $('#dislikes-number').text(data.dislikesNumber);
}