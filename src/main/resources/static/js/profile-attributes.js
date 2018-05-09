
var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var attributeErrorsSet = new Set();

var attributeErrorsMap = {
    1: 'attribute_length',
    2: 'attribute_duplicate'
}

function attributeShowInputForm() {
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
    validateAttribute();
    addAttributeUpdateInput();
    updateAttributeErrorsArea();
    if (attributeErrorsSet.size == 0) {
        ajaxSaveAttribute($('#add-attribute-input').val());
    }
}

function addAttributeCancel() {
    attributeCloseInput();
}

function validateAttribute() {
    attributeErrorsSet.clear();

    var attribute = $('#add-attribute-input').val();

    if (attribute.length < 3 || attribute.length > 32) {
        attributeErrorsSet.add(1);
    } else {
        attributeErrorsSet.delete(1);
    }

}

function addAttributeUpdateInput() {
    if (attributeErrorsSet.size == 0) {
        $('#add-attribute-input').removeClass('is-invalid').addClass('is-valid');
    } else {
        $('#add-attribute-input').removeClass('is-valid').addClass('is-invalid');
    }
}

function updateAttributeErrorsArea() {
    $('#attributeErrors').empty();
    attributeErrorsSet.forEach(function (value) {
        $('#attributeErrors').append('<p>' + LANG[attributeErrorsMap[value]] + '</p>');
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
            attributeCloseInput();
        },
        error: function (errors) {
            var errors = errors.responseJSON;
            errors.forEach(function (value) {
                attributeErrorsSet.add(value);
            });
            addAttributeUpdateInput();
            updateAttributeErrorsArea();
        }
    });
}

function displayAttribute(attribute) {
    $('#attribute-param').append('\n' +
        '                    <div class="row" id="attr-param-' + attribute.id + '">\n' +
        '                        <div class="col-md-4">' + attribute.name + '</div>\n' +
        '                        <div class="col-md-8" id="edit-param-area-' + attribute.id + '">\n' +
        '                            <div class="row">\n' +
        '                                <div class="col-md-9"\n' +
        '                                     id="param-label-' + attribute.id + '"></div>\n' +
        '                                <span class="text-right">\n' +
        '                                    <span class="clickable-icon">\n' +
        '                                        <i id="' + attribute.id + '"\n' +
        '                                           onclick="paramShowInputForm(' + attribute.id + ')"\n' +
        '                                           class="fas fa-edit"></i>\n' +
        '                                    </span>\n' +
        '                                    <span class="clickable-icon">\n' +
        '                                        <i id="btn-attr-del-' + attribute.id + '"\n' +
        '                                           onclick="delAttribute(' + attribute.id + ')"\n' +
        '                                           class="fas fa-trash-alt  mr-2 mt-2"></i>\n' +
        '                                    </span>\n' +
        '                                </span>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                    </div>')
}

function attributeCloseInput() {
    $('#add-attribute-area').empty();
    $('#add-attribute-area').append('<button id="add-attribute" class="btn btn-secondary" onclick="attributeShowInputForm()">' + LANG.add_attribute +
        '                        </button>');
}



//////////////// delete attribute area is below ////////////////////

var deletedAttributeId;

function delAttribute(attrId) {
    deletedAttributeId = attrId;
    ajaxDeleteAttribute(attrId);
}

function ajaxDeleteAttribute(attributeId) {
    $.ajax({
        type: "DELETE",
        contentType: 'application/json; charset=utf-8',
        url: "/userAttribute",
        data: JSON.stringify(attributeId),
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function () {
            clearAttrParamRow();
        },
        error: function (e) {
            console.log('ERROR while deleting attribute ' + e);
        }
    });
}

function clearAttrParamRow() {
    $('#attr-param-' + deletedAttributeId).empty();
}