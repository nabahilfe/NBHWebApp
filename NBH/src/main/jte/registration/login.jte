@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    isAuthenticated = isAuthenticated,
    username = username,
    fullUsername = fullUsername,
    ctxt = ctxt,
    content = @`

@if(successMessage != null)

    <h3>Du bist angemeldet.</h3>

@else

    <h3>Anmelden</h3>
    <p>Bitte melde dich mit deiner E-Mail und deinem Passwort an.</p>

    <form action="/registration/login" method="post" class="needs-validation" novalidate>
        ${csrfHiddenInput}
        <div class="mb-3">
            <label for="username" class="form-label">E-Mail-Adresse<span class="text-danger">*</span></label>
            <input type="email"
                   class="form-control"
                   id="username"
                   name="username"
                   placeholder="name@example.com"
                   autocomplete="username"
                   required>
            <div class="invalid-feedback">Bitte eine gültige E-Mail-Adresse eingeben.</div>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Passwort<span class="text-danger">*</span></label>
            <input type="password"
                   class="form-control"
                   id="password"
                   name="password"
                   autocomplete="current-password"
                   required>
            <div class="invalid-feedback">Bitte das Passwort eingeben.</div>
        </div>

        <button type="submit" class="btn btn-primary">Anmelden</button>
    </form>

@endif

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        // Bootstrap client-side validation hook
        (function () {
            'use strict';
            const forms = document.querySelectorAll('.needs-validation');
            Array.prototype.slice.call(forms).forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        })();
    </script>

`)