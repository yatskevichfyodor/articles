var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');

var ParamsErrorsSet = new Set();

var paramsErrorsMap = {
    1: 'param_length'
}

var editStarted = false;
var attributeId;
var initialParamValue;
var paramValue;
var ajaxInProcess = false;

function paramsShowInputForm(attributeIdParam) {
    if (editStarted) return;
    editStarted = true;

    attributeId = attributeIdParam;

    paramValue = document.getElementById("param-label-" + attributeId).textContent;
    initialParamValue = document.getElementById("param-label-" + attributeId).textContent;

    $('#edit-param-area-' + attributeIdParam).empty();
    $('#edit-param-area-' + attributeIdParam).append('' +
        '<div class="row">' +
        '   <input class="form-control col-md-6" id="update-param-input" value="' + paramValue + '" />' +
        '   <button type="submit" class="btn btn-info editable-submit col-md-2" onclick="addParamSubmit()">' +
        '      <i class="fas fa-check-circle"></i>' +
        '   </button>' + '' +
        '   <button type="button" class="btn editable-cancel col-md-2" onclick="addParamCancel()">' +
        '      <i class="fas fa-minus-circle"></i>' +
        '   </button>' +
        '</div>' +
        '<div class="row">' +
        '   <div id="update-param-errors" class="col-md-12 errors"></div>' +
        '</div>');
}


function addParamSubmit() {
    if (ajaxInProcess) return;

    validateParam();
    if (ParamsErrorsSet.size == 0) {
        var updateUserParamDto = {
            attributeId: attributeId,
            paramValue: $('#update-param-input').val()
        }

        ajaxInProcess = true;
        ajaxUpdateParam(updateUserParamDto);
    }
}

function addParamCancel() {
    if (ajaxInProcess) return;

    paramValue = initialParamValue;
    closeInput();
}

function updateErrorsArea() {
    $('#update-param-errors').empty();
    ParamsErrorsSet.forEach(function (value) {
        $('#update-param-errors').append('<p>' + LANG[paramsErrorsMap[value]] + '</p>');
    });
}

function validateParam() {
    var attribute = $('#update-param-input').val();
    var valid = true;

    if (attribute.length < 3 || attribute.length > 32) {
        valid = false;
        ParamsErrorsSet.add(1);
    } else {
        ParamsErrorsSet.delete(1);
    }

    if (valid) {
        $('#update-param-input').removeClass('is-invalid').addClass('is-valid');
    } else {
        $('#update-param-input').removeClass('is-valid').addClass('is-invalid');
    }

    updateErrorsArea();
}

function ajaxUpdateParam(updateUserParamDto) {
    $.ajax({
        type: "POST",
        contentType: 'application/json; charset=utf-8',
        url: "/updateUserParam",
        data: JSON.stringify(updateUserParamDto),
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        cache: false,
        timeout: 600000,
        success: function (updateUserParamDto) {
            ajaxInProcess = false;

            paramValue = updateUserParamDto.paramValue;
            closeInput();
        },
        error: function (errors) {
            ajaxInProcess = false;

            errors.forEach(function (value) {
                ParamsErrorsSet.add(value);
            });
            updateErrorsArea()
        }
    });
}

function closeInput() {
    editStarted = false;

    $('#edit-param-area-' + attributeId).empty();
    $('#edit-param-area-' + attributeId).append('\n' +
        '                            <div class="row">\n' +
        '                                <div class="col-md-9"  id="param-label-' + attributeId + '">' + paramValue + '</div>\n' +
        '                                <span class="col-md-3 edit-param-btn"\n' +
        '                                      onclick="paramsShowInputForm(' + attributeId + ')" id="' + attributeId + '">\n' +
        '                                <i class="fas fa-edit"></i>\n' +
        '                            </span>\n' +
        '                            </div>');
    ;
}