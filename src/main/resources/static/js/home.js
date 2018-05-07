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
        url: "/getArticleMatrixByCategoryIdAndOrderId" + "?categoryId=" + categoryId + "&orderId=" + orderId,
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

function displayMatrix(matrix) {
    $('#articles').empty();
    var str = '';
    $.each(matrix, function(innerListId, innerList) {
        str += '<div class="row mt-4">\n';
        $.each(innerList, function (index, article) {
            str += '' +
                '        <div class="col-md-' + 12/horizontalSize + '" height="200px">\n' +
                '            <a href="/article/' + article.id + '">\n' +
                '                <img class="pb-1" width="320px" height="180px" src="' + article.imageData + '"></img>\n' +
                '            </a>\n' +
                '            <br />' +
                '            <span class="mb-3 font-weight-bold header"><a href="article/' + article.id + '">' + article.title + '</a></span>\n' +
                '        </div>\n';
        });
        str += '</div>';
    });
    $('#articles').append(str);
}
