// shared.js
if (window.sharedLoaded) {
    console.log('shared.js already loaded, skipping');
} else {
    window.sharedLoaded = true;

    window.currentRole = 'USER';  // Default

    function fetchRole(callback) {
        $.get("/api/current-role")
            .done(function (data) {
                window.currentRole = data.role || 'USER';
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
    $(document).ajaxComplete(function(event, xhr) {
        if (xhr.status === 401 || xhr.status === 403) {
            alert("Session expired or unauthorized. Redirecting to login.");
            window.location.href = "/login";
        }
    });

    function logout() {
        $.post("/logout").always(function () {
            window.location.href = "/login";
        });
    }

    function setupCourseAutocomplete(inputId, hiddenId) {
        $.get('/courses/suggestions', function(courses) {
            const datalistId = 'courseSuggestions';
            let dl = document.getElementById(datalistId);
            if (!dl) {
                dl = document.createElement('datalist');
                dl.id = datalistId;
                document.body.appendChild(dl);
            } else {
                dl.innerHTML = '';
            }
            courses.forEach(c => {
                const opt = document.createElement('option');
                opt.value = c[1]; // name
                opt.setAttribute('data-id', c[0]); // id
                dl.appendChild(opt);
            });
            $(inputId).attr('list', datalistId);
            $(inputId).on('input', function() {
                const val = $(this).val();
                const match = $(`#${datalistId} option[value="${val}"]`);
                $(hiddenId).val(match.length ? match.data('id') : '');
            });
        }).fail(() => alert('Failed to load course suggestions.'));
    }

    function showAlert(message) {
        $('#validationAlertMessage').text(message);
        $('#validationAlert').fadeIn();
    }

    function hideAlert() {
        $('#validationAlert').fadeOut();
    }

    $(document).ready(function() {
        $('#validationAlertClose').click(hideAlert);
    });

    // Append to shared.js (end of file)
    function getQueryParam(name) {
        const params = new URLSearchParams(window.location.search);
        return params.get(name);
    }


    console.log('shared.js loaded successfully');
}