
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var attributesErrorsSet = new Set();

var attributesErrorsMap = {
    1: 'attribute_length',
    2: 'attribute_duplicate'
}

$(document).ready(function () {
    // $('#add-attribute').click(function () {
    //     attributesShowInputForm();
    // })
});

function attributesShowInputForm() {
    $('#add-attribute-area').empty();
    $('#add-attribute-area').append('' +
        '<div class="row">' +
        '   <input class="form-control col-md-6" id="add-attribute-input" />' +
        '   <button type="submit" class="btn btn-info editable-submit col-md-2" onclick="addAttributeSubmit()" id="add-attribute-submit">' +
        '      <i class="fas fa-check-circle"></i>' +
        '   </button>' + '' +
        '   <button type="button" class="btn editable-cancel col-md-2" onclick="addAttributeCancel()" id="add-attribute-cancel">' +
        '      <i class="fas fa-minus-circle"></i>' +
        '   </button>' +
        '</div>' +
        '<div class="row">' +
        '   <div id="attributeErrors" class="col-md-12 errors"></div>' +
        '</div>');
}

function addAttributeSubmit() {
// $('#add-attribute-submit').click(function () {
    validateAttribute();
    if (attributesErrorsSet.size == 0) {
        ajaxSaveAttribute($('#add-attribute-input').val());
    }
}

function addAttributeCancel() {
// $('#add-attribute-cancel').click(function () {
    attributesCloseInput();
}

// }

function validateAttribute() {
    var attribute = $('#add-attribute-input').val();
    var valid = true;

    if (attribute.length < 3 || attribute.length > 32) {
        valid = false;
        attributesErrorsSet.add(1);
    } else {
        attributesErrorsSet.delete(1);
    }

    if (valid) {
        $('#add-attribute-input').removeClass('is-invalid').addClass('is-valid');
    } else {
        $('#add-attribute-input').removeClass('is-valid').addClass('is-invalid');
    }

    updateErrorsArea();
}

function updateErrorsArea() {
    $('#attributeErrors').empty();
    attributesErrorsSet.forEach(function (value) {
        $('#attributeErrors').append('<p>' + LANG[attributesErrorsMap[value]] + '</p>');
    });
}

function ajaxSaveAttribute(attribute) {
    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/add-attribute",
        data: attribute,
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (attribute) {
            displayAttribute(attribute);
            attributesCloseInput();
        },
        error: function (errors) {
            errors.forEach(function (value) {
                attributesErrorsSet.add(value);
            });
            updateErrorsArea()
        }
    });
}

function displayAttribute(attribute) {
    $('#attribute-param').append('\n' +
        '                    <div class="row">\n' +
        '                        <div class="col-md-5">' + attribute.name + '</div>\n' +
        '                        <div class="col-md-7"></div>\n' +
        '                    </div>')
}

function attributesCloseInput() {
    $('#add-attribute-area').empty();
    $('#add-attribute-area').append('<button id="add-attribute" class="btn btn-secondary" onclick="attributesShowInputForm()">' + LANG.add_attribute +
        '                        </button>');
}