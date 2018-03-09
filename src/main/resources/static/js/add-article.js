Dropzone.autoDiscover = false;

var myDropzone = new Dropzone('#dropzone1', {
    url: "/upload-image",
    paramName: "file", // The name that will be used to transfer the file
    maxFilesize: 2, // MB
    maxFiles: 1,
    addRemoveLinks: true,
    autoProcessQueue: false,
    acceptedFiles: '.jpg,.png,.jpeg,.gif'
});

$(document).ready(function () {

    $("#submit").click(function(){

        var article = {
            category: $('#category').val(),
            title: $('#title').val(),
            content: $('#content').val(),
            picture: myDropzone.getAcceptedFiles()[0].dataURL
        }

        ajaxSaveArticle(article);
    });
});

function ajaxSaveArticle(article){
    var token = $('#_csrf').attr('content');
    var header = $('#_csrf_header').attr('content');

    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/add-article",
        data: JSON.stringify(article),
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function(){
            window.location.replace('/article/' + article.title);
        },
        error: function() {
            alert("error");
        }
    });
}