var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
var horizontalSize = $('#horizontalSize').attr('content');

$(document).ready(function () {
    $('#category').change(function() {
        getArticleMatrix();
    });

    $('#order').change(function() {
        getArticleMatrix();
    });
});

function getArticleMatrix() {
    var categoryId = $('#category').val();
    var orderId = $('#order').val();

    ajaxGetArticleMatrixByCategoryIdAndOrderId(categoryId, orderId);
}

function ajaxGetArticleMatrixByCategoryIdAndOrderId(categoryId, orderId) {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/getArticlesByCategoryIdAndOrderId" + "?categoryId=" + categoryId + "&orderId=" + orderId,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(matrix){
            displayMatrix(matrix);
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function displayMatrix(articles) {
    $('#articles').empty();
    var str = '<div class="mt-4 mb-4 row justify-content-center" id="articles">\n';
    $.each(articles, function(i, article) {
        str += '\n' +
            '        <div class="col-xs-4 mb-3 mt-3">\n' +
            '            <a href="/article/' + article.id + '">\n' +
            '                <img class="pb-1" width="320px" height="180px" src="' + article.imageData +'"></img>\n' +
            '            </a>\n' +
            '            <br/>\n' +
            '            <span class="mb-3 font-weight-bold header">' +
            '                <a href="/article/' + article.id + '">' + article.title + '</a></span>\n' +
            '        </div>';
    });
    str += '</div>';
    $('#articles').append(str);
}
