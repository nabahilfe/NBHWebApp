@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param java.lang.String email = null


@template.layout.main(content = @`

    <h3>Registrieren</h3>
    <p>Bitte gib deine Daten ein, um einen Zugang anzulegen.</p>

    <form action="/register" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="email" class="form-label">Deine E-Mail Adresse</label>
            <input type="email" readonly
                   class="form-control"
                   id="email"
                   name="email"
                   value="${email}"
                   required>
            <div class="invalid-feedback">Bitte eine gültige E-Mail Adresse eingeben.</div>
        </div>

        <div class="mb-3">
            <label for="sixDigitCode" class="form-label">6-stelliger Code<span class="text-danger">*</span></label>
            <input type="text"
                   class="form-control"
                   id="sixDigitCode"
                   name="sixDigitCode"
                   pattern="[0-9]{6}"
                   inputmode="numeric"
                   minlength="6"
                   maxlength="6"
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
                   pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{12,}$"
                   title="Mindestens 12 Zeichen, je 1 Großbuchstabe, 1 Kleinbuchstabe, 1 Ziffer und 1 Sonderzeichen"
                   autocomplete="new-password"
                   required>
            <div class="form-text">Mindestens 12 Zeichen, je mindestens 1 Großbuchstabe, 1 Kleinbuchstabe, 1 Ziffer und 1 Sonderzeichen.</div>
            <div class="invalid-feedback">Passwort braucht mindestens 12 Zeichen sowie je mindestens 1 Großbuchstaben, 1 Kleinbuchstaben, 1 Ziffer und 1 Sonderzeichen.</div>
        </div>

        <button type="submit" class="btn btn-primary">Registrieren</button>
    </form>

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