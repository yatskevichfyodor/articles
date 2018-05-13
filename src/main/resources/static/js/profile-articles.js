
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var isProfilePage = $('#isProfilePage').attr('content');
var userId = userId = $('#userId').attr('content');

$(document).ready(function () {
    $('#category').change(function() {
        var id = $('#category').val();

        ajaxFindArticles(id);
    });
});

function ajaxFindArticles(categoryId){
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/findArticlesByCategoryIdAndAuthorId" + "?categoryId=" + categoryId + "&authorId=" + userId,
        // data: id,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(list){
            $('#articles-list').empty();
            list.forEach(function(value) {
                $('#articles-list').append('<li value="' + value.id + '" class="list-group-item"><a href="/article/' + value.id + '">' + value.title + '</a></li>');
            });
            // $.each(list, function(index, value) {
            //     $('#articles-list').append('<li value="' + index + '" class="list-group-item"><a href="/article/' + value + '">' + value + '</a></li>');
            // });
        }
    });
}