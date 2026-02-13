@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3>Anmelden</h3>
    <p>Bitte melde dich mit deiner E-Mail-Adresse und deinem Passwort an.</p>

    <form action="/registration/login" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="email" class="form-label">E-Mail-Adresse<span class="text-danger">*</span></label>
            <input type="email"
                   class="form-control"
                   id="email"
                   name="email"
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