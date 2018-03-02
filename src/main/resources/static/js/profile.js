$(document).ready(function () {
    $('#category').change(function() {
        var dropdown = document.getElementById("category");
        var title = dropdown.options[dropdown.selectedIndex].text;

        ajaxFindArticles(title);
    });
});

function ajaxFindArticles(category){
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/findArticlesByCategory",
        data: JSON.stringify(category),
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