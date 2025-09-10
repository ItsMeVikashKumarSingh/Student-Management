if (window.sharedLoaded) {
    console.log('shared.js already loaded, skipping');
} else {
    window.sharedLoaded = true;

    window.currentRole = 'USER';  // Default

    function fetchRole(callback) {
        $.get("/api/current-role")
            .done(function (data) {
                window.currentRole = data.role || 'USER';
                console.log('Fetched role:', window.currentRole);
                if (window.currentRole === 'ADMIN') {
                    $('.admin-only').show();
                }
                if (callback) callback();
            })
            .fail(function (xhr) {
                if (xhr.status == 401 || xhr.status == 403 || isLoginRedirect(xhr.responseText)) {
                    alert("Session expired. Redirecting to login...");
                    window.location.href = "/login";
                } else {
                    alert("Error loading data.");
                }
            });
    }

    function isLoginRedirect(response) {
        return typeof response === 'string' && response.includes('<html') && response.includes('login');
    }

// Global AJAX error handler for session expiry
    $(document).ajaxComplete(function(event, xhr, settings) {
        if (xhr.status === 401 || xhr.status === 403) {
            alert("Session expired or unauthorized. Redirecting to login.");
            window.location.href = "/login";
        }
    });

    $('#validationAlertClose').click(function() {
        $('#validationAlert').hide();
    });

    // Updated validateForm to use showAlert and stop after first error (no repeats)
    function validateForm(fields) {
        let isValid = true;
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        for (let i = 0; i < fields.length; i++) {
            const field = fields[i];
            const value = $(field.id).val().trim();
            if (value === '') {
                showAlert(`${field.name} is required.`);
                isValid = false;
                break;  // Stop after first error to avoid multiple dialogs
            } else if (field.isEmail && !emailRegex.test(value)) {
                showAlert(`${field.name} must be a valid email address (e.g., example@domain.com).`);
                isValid = false;
                break;  // Stop after first error
            }
        }

        return isValid;
    }

// Dialog show/hide functions (add at end of shared.js)
    function showAlert(message) {
        $('#validationAlertMessage').text(message);
        $('#validationAlert').fadeIn();
    }

    function hideAlert() {
        $('#validationAlert').fadeOut();
    }

// Bind close button (add at end of shared.js, inside $(document).ready if not already)
    $(document).ready(function() {
        $('#validationAlertClose').click(hideAlert);
    });


    function logout() {
        $.post("/logout").always(function () {
            window.location.href = "/login";
        });
    }

    console.log('shared.js loaded successfully');
}
