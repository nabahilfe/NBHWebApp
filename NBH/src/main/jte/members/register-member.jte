@template.layout.main(content = @`

    <h3>Registrieren</h3>
    <p>Bitte gib deine Daten ein, um einen Zugang anzulegen.</p>

    <form action="/register" method="post" class="needs-validation" novalidate>
        <div class="mb-3">
            <label for="email" class="form-label">E-Mail Adresse<span class="text-danger">*</span></label>
            <input type="email"
                   class="form-control"
                   id="email"
                   name="email"
                   placeholder="name@example.com"
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
                   pattern="^(?=(?:.*[A-Z]){2,})(?=(?:.*[a-z]){2,})(?=(?:.*\d){2,})(?=(?:.*[^A-Za-z0-9]){1,}).{12,}$"
                   title="Mindestens 12 Zeichen, mindestens 2 Groß- und 2 Kleinbuchstaben, mindestens 2 Ziffern und mindestens 1 Sonderzeichen"
                   autocomplete="new-password"
                   required>
            <div class="form-text">Mindestens 12 Zeichen, mindestens 2 Groß- und 2 Kleinbuchstaben, mindestens 2 Ziffern und mindestens 1 Sonderzeichen.</div>
            <div class="invalid-feedback">Passwort-Anforderungen nicht erfüllt.</div>
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