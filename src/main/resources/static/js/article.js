
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var articleId = $('#articleId').attr('content');

$('#btn-article-delete').click(function(){
    ajaxDeleteArticle();
});

function ajaxDeleteArticle() {
    $.ajax({
        type: 'DELETE',
        url: '/article/delete',
        data: JSON.stringify(articleId),
        contentType: 'application/json; charset=utf-8',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success : function() {
            window.location.replace('/home');
        },
        error : function(e) {
            alert("Article deletion error");
            console.log("ERROR: ", e);
        }
    });
}