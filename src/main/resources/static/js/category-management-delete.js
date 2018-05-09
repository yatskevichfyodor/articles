
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var deleteCategoryErrorsSet = new Set();

var deleteCategoryErrorsMap = {
    1: 'category_has_subcategories',
    2: 'category_not_selected',
    3: 'category_has_articles'
}

function deleteCategorySubmit() {
    validateDeleteCategory();
    updateDeleteErrorsArea();
    if (deleteCategoryErrorsSet.size == 0) {
        ajaxDeleteCategory($('#delete-category-dropdown').val());
    }

}

function validateDeleteCategory() {
    deleteCategoryErrorsSet.clear();

    var categoryId = $('#delete-category-dropdown').val();

    if (categoryId == 0) {
        deleteCategoryErrorsSet.add(2);
    } else {
        deleteCategoryErrorsSet.delete(2);
    }
}

function updateDeleteErrorsArea() {
    $('#deleteCategoryErrors').empty();
    deleteCategoryErrorsSet.forEach(function (value) {
        $('#deleteCategoryErrors').append('<p>' + LANG[deleteCategoryErrorsMap[value]] + '</p>');
    });
}

function ajaxDeleteCategory(categoryId) {
    $.ajax({
        type: "DELETE",
        contentType: 'application/json; charset=utf-8',
        url: "/category",
        data: categoryId,
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function () {
            window.location.reload(false);
        },
        error: function (errors) {
            var errors = errors.responseJSON;
            errors.forEach(function (value) {
                deleteCategoryErrorsSet.add(value);
            });
            updateDeleteErrorsArea();
        }
    });
}
