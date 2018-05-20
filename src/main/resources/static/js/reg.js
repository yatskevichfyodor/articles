$(document).ready(function () {
    var errorsSet = new Set();
    errorsSet.add(LANG.password_not_match);
    var confirmPasswordFieldWasChanged = false;

    $('#username').change(function () {
        // if (isRegistration())
            validateUsername();
    });

    $('#email').change(function () {
        // if (isRegistration())
            validateEmail();
    });

    $('#password').change(function () {
        // if (isRegistration())
            validatePassword();
    });

    $('#confirm_password').change(function () {
        validateConfirmPassword();
    });

    function validateUsername() {
        var username = $('#username').val();
        var valid = true;

        if (username.length < 4 || username.length > 32) {
            valid = false;
            errorsSet.add(LANG.username_length);
        } else {
            errorsSet.delete(LANG.username_length);
        }

        if (username.match(/^[a-zA-Z0-9]+$/)) {
            errorsSet.delete(LANG.username_regex);
        } else {
            valid = false;
            errorsSet.add(LANG.username_regex);
        }

        if (valid) {
            $('#username').removeClass('is-invalid').addClass('is-valid');
            ajaxCheckUsername(username);
        } else {
            $('#username').removeClass('is-valid').addClass('is-invalid');
        }

        showErrors();
    }

    function validateEmail() {
        var email = $('#email').val();
        var valid = true;

        if (email.length < 4 || email.length > 100) {
            valid = false;
            errorsSet.add(LANG.email_length);
        } else {
            errorsSet.delete(LANG.email_length);
        }

        if (email.match(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)) {
            errorsSet.delete(LANG.email_regex);
        } else {
            valid = false;
            errorsSet.add(LANG.email_regex);
        }

        if (valid) {
            $('#email').removeClass('is-invalid').addClass('is-valid');
            ajaxCheckEmail(email);
        } else {
            $('#email').removeClass('is-valid').addClass('is-invalid');
        }

        showErrors();
    }

    function validatePassword() {
        var password = $('#password').val();
        var valid = true;

        if (password.length < 4 || password.length > 32) {
            valid = false;
            errorsSet.add(LANG.password_length);
        } else {
            errorsSet.delete(LANG.password_length);
        }

        if (confirmPasswordFieldWasChanged) {
            if (comparePasswords()) {
                errorsSet.delete(LANG.password_not_match);
                $('#confirm_password').removeClass('is-invalid').addClass('is-valid');
            } else {
                errorsSet.add(LANG.password_not_match);
                $('#confirm_password').removeClass('is-valid').addClass('is-invalid');
            }
        }

        if (valid) {
            $('#password').removeClass('is-invalid').addClass('is-valid');
        } else {
            $('#password').removeClass('is-valid').addClass('is-invalid');
        }

        showErrors();
    }

    function validateConfirmPassword() {
        confirmPasswordFieldWasChanged = true;
        var valid = true;

        if (comparePasswords()) {
            errorsSet.delete(LANG.password_not_match);
        } else {
            valid = false;
            errorsSet.add(LANG.password_not_match);
        }

        if (valid) {
            $('#confirm_password').removeClass('is-invalid').addClass('is-valid');
        } else {
            $('#confirm_password').removeClass('is-valid').addClass('is-invalid');
        }

        showErrors();
    }

    function comparePasswords() {
        if ($('#password').val() == $('#confirm_password').val()) {
            return true;
        }
        return false;
    }

    function showErrors() {
        $('#errors_list').empty();
        errorsSet.forEach(function (value) {
            if (value != LANG.password_not_match || confirmPasswordFieldWasChanged)
                $('#errors_list').append('<p>' + value + '</p>');
        });
    }

    $('#submit').click(function(event) {
        validateUsername();
        validateEmail();
        validatePassword();
        validateConfirmPassword();
        if (errorsSet.size > 0) {
            event.preventDefault();
        }
    })

    function ajaxCheckUsername(username) {
        var token = $('#_csrf').attr('content');
        var header = $('#_csrf_header').attr('content');

        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "/checkIfUsernameNotExists",
            data: username,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (result) {
                if (result) {
                    errorsSet.delete(LANG.username_duplicate);
                    showErrors();
                    validateUsernameField();
                } else {
                    $('#username').removeClass('is-valid').addClass('is-invalid');
                    errorsSet.add(LANG.username_duplicate);

                    showErrors();
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (xhr.status == 404) {
                    console.log(thrownError);
                }
            }
        });
    }

    function ajaxCheckEmail(email) {
        var token = $('#_csrf').attr('content');
        var header = $('#_csrf_header').attr('content');

        $.ajax({
            type: "POST",
            contentType: 'application/json; charset=utf-8',
            url: "/checkIfEmailNotExists",
            data: email,
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            dataType: 'json',
            cache: false,
            timeout: 600000,
            success: function (result) {
                if (result) {
                    errorsSet.delete(LANG.email_duplicate);

                    showErrors();
                    validateEmailField();
                } else {
                    $('#email').removeClass('is-valid').addClass('is-invalid');
                    errorsSet.add(LANG.email_duplicate);

                    showErrors();
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                if (xhr.status == 404) {
                    console.log(thrownError);
                }
            }
        });
    }

    function validateUsernameField() {
        if (errorsSet.has(LANG.username_length) || errorsSet.has(LANG.username_regex) || errorsSet.has(LANG.username_duplicate)) {
            $('#username').removeClass('is-valid').addClass('is-invalid');
        } else {
            $('#username').removeClass('is-invalid').addClass('is-valid');
        }
    }

    function validateEmailField() {
        if (errorsSet.has(LANG.email_length) || errorsSet.has(LANG.email_regex) || errorsSet.has(LANG.email_duplicate)) {
            $('#email').removeClass('is-valid').addClass('is-invalid');
        } else {
            $('#email').removeClass('is-invalid').addClass('is-valid');
        }
    }
});