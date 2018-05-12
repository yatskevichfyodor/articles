
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var addCategoryErrorsSet = new Set();

var addCategoryErrorsMap = {
    1: 'category_length',
    2: 'category_duplicate'
}

function addCategorySubmit() {
    validateAddCategory();
    addCategoryUpdateInput();
    updateAddErrorsArea();
    if (addCategoryErrorsSet.size == 0) {
        var category = {
            parentId: $('#add-category-dropdown').val(),
            name: $('#add-category-input').val()
        }
        ajaxSaveCategory(category);
    }
}

function validateAddCategory() {
    addCategoryErrorsSet.clear();

    var categoryName = $('#add-category-input').val();

    if (categoryName.length < 3 || categoryName.length > 20) {
        addCategoryErrorsSet.add(1);
    } else {
        addCategoryErrorsSet.delete(1);
    }
}

function updateAddErrorsArea() {
    $('#addCategoryErrors').empty();
    addCategoryErrorsSet.forEach(function (value) {
        $('#addCategoryErrors').append('<p>' + LANG[addCategoryErrorsMap[value]] + '</p>');
    });
}

function addCategoryUpdateInput() {
    if (addCategoryErrorsSet.size == 0) {
        $('#add-category-input').removeClass('is-invalid').addClass('is-valid');
    } else {
        $('#add-category-input').removeClass('is-valid').addClass('is-invalid');
    }
}

function ajaxSaveCategory(category) {
    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/category/add",
        data: JSON.stringify(category),
        dataType: 'json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function() {
            window.location.reload(false);
        },
        error: function(errors) {
            var errors = errors.responseJSON;
            errors.forEach(function (value) {
                addCategoryErrorsSet.add(value);
            });
            updateAddErrorsArea();
            addCategoryUpdateInput();
        }
    });
}
