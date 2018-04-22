$(document).ready(function () {
    $('#category').change(function() {
        var id = $('#category').val();

        ajaxFindArticles(id);
    });
});

function ajaxFindArticles(id){
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/findArticlesByCategory" + "?id=" + id,
        // data: id,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(list){
            $('#articles-list').empty();
            $.each(list, function(index, value) {
                $('#articles-list').append('<li value="' + index + '" class="list-group-item"><a href="/article/' + value + '">' + value + '</a></li>');
            });
        }
    });
}