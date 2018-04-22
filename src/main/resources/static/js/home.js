var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
var horizontalSize = $('#horizontalSize').attr('content');

$(document).ready(function () {
    $('#category').change(function() {
        var id = $('#category').val();

        ajaxFindArticlesByCategory(id);
    });

    $('#order').change(function() {
        var id = $('#order').val();

        ajaxFindArticlesByOrder(id);
    });
});

function ajaxFindArticlesByCategory(id){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/getArticleMatrixByCategoryId" + "?id=" + id,
        // data: id,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(list){
            $('#articles').empty();
            var str = '';
            $.each(list, function(innerListId, innerList) {
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
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}

function ajaxFindArticlesByOrder(id){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/getArticleMatrixByOrderId" + "?id=" + id,
        // data: id,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        // dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(list){
            $('#articles').empty();
            $.each(list, function(innerLitId, innerList) {
                $('#articles-list').append('<div class="row mb-4" th:each="innerList: ${articlesMatrix}">\n');
                $.each(list, function (index, article) {
                    $('#articles-list').append(
                        '        <div class="col-md-' + 12/innerList.size + '" height="200px">\n' +
                        '            <a href="/article/' + article.id + '">\n' +
                        '                <img class="pb-1" width="100%" height="80%" src="' + article.image.data + '"></img>\n' +
                        '            </a>\n' +
                        '            <span class="mb-3 font-weight-bold header"><a href="article/' + article.id + '"\n' +
                        '                                                          text="' + article.title + '"></a></span>\n' +
                        '        </div>\n'
                    );
                });
                $('#articles-list').append('</div>');
            });
        },
        error : function(e) {
            console.log("ERROR: ", e);
        }
    });
}
