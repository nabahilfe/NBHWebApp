@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput

@param eu.nabahilfe.webapp.registration.RegisterConfirmForm form = null

@template.layout.main(content = @`

    <h3>Registrieren</h3>
    <p>Bitte gib deinen Code und ein sicheres Passwort ein, um dein Konto zu aktivieren.</p>

    <form action="/registration/confirm" method="post" class="needs-validation" novalidate>
        ${csrfHiddenInput}
        <div class="mb-3">
            <label for="username" class="form-label">Deine E-Mail Adresse</label>
            <input type="email"
                   class="form-control"
                   id="username"
                   name="username"
                   value="${form.getEmail()}"
                   required>
            <div class="invalid-feedback">Bitte eine gültige E-Mail Adresse eingeben.</div>
        </div>

        <div class="mb-3">
            <label for="code" class="form-label">6-stelliger Code<span class="text-danger">*</span></label>
            <input type="text"
                   class="form-control"
                   id="code"
                   name="code"
                   pattern="[0-9]{6}"
                   inputmode="numeric"
                   minlength="6"
                   maxlength="6"
                   value="${form.getCode()}"
                  required>
            <div class="form-text">Gib genau die 6 Ziffern ein, die dir zugesendet wurden.</div>
            <div class="invalid-feedback">Gib genau die 6 Ziffern ein, die dir zugesendet wurden.</div>
        </div>

        <div class="mb-3">
            <label for="password" class="form-label">Passwort<span class="text-danger">*</span></label>
            <input type="password"
                   class="form-control"
                   id="password"
                   name="password"
                   minlength="12"
                   pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{12,}$"
                   title="Mindestens 12 Zeichen, je mindestens 1 Großbuchstabe, 1 Kleinbuchstabe und 1 Ziffer; Sonderzeichen erlaubt"
                   autocomplete="new-password"
                   value="${form.getPassword()}"
                   required>
            <div class="form-text">Mindestens 12 Zeichen, je mindestens 1 Großbuchstabe, 1 Kleinbuchstabe und 1 Ziffer. Sonderzeichen sind erlaubt, aber nicht erforderlich.</div>
            <div class="invalid-feedback">Passwort braucht mindestens 12 Zeichen sowie je mindestens 1 Großbuchstabe, 1 Kleinbuchstabe und 1 Ziffer. Sonderzeichen sind erlaubt.</div>
        </div>

        <button type="submit" class="btn btn-primary">Registrieren</button>
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