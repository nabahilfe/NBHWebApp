@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput

@param java.lang.String email = null

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

    <h3>Registrierungscode anfordern</h3>
    <p>Gib deine E-Mail ein um einen 6-stelligen Registrierungscode zu erhalten.<br/>
    Der Code, den du per e-Mail erhälts, ist 15 Minuten gültig und wird für die Aktivierung deines Kontos benötigt.</p>
    <p>Hinweis: Du kannst mit diesem Formular auch ein neues Paswort erstellen, falls du dein Passwort vergessen hast.</p>


    <form action="/registration/email" method="post" class="needs-validation" novalidate>
        ${csrfHiddenInput}
        <div class="mb-3">
            <label for="email" class="form-label">Meine E-Mail<span class="text-danger">*</span></label>
            <input type="email"
                   class="form-control"
                   id="email"
                   name="email"
                   placeholder="name@example.com"
                   value="${email}"
                   required>
            <div class="invalid-feedback">Bitte eine gültige E-Mail Adresse eingeben.</div>
        </div>

        <button type="submit" class="btn btn-primary">Code anfordern</button>

    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    @if(successMessage != null && !successMessage.isEmpty())
        <a class="btn btn-outline-success bi-arrow-right" href="/members/register-member"> Weiter zur Registrierung</a>
    @endif

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