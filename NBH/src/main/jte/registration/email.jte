<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param java.lang.String email = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Registrierungscode anfordern</h3>
    <p>
        Gib deine E-Mail ein um einen 6-stelligen Registrierungscode zu erhalten.
        Der Code ist 15 Minuten gültig und wird für die Aktivierung
        deines Kontos benötigt.
    </p>

    <p>Hinweis: du kannst damit auch ein neues Passwort erstellen,
    falls du dein Passwort vergessen hast.</p>


    <form action="/registration/email" method="post" class="needs-validation" novalidate>
        ${csrfHiddenInput}
        <div class="mb-3">
            <label for="email" class="form-label">Deine E-Mail<span class="text-danger">*</span></label>
            <input type="email"
                   class="form-control"
                   id="email"
                   name="email"
                   placeholder="name@example.com"
                   value="${email}"
                   required>
            <div class="invalid-feedback">Bitte eine gültige E-Mail Adresse eingeben.</div>
        </div>

        <p><strong>Wichtig</strong>: Überprüfe auch deinen Spam-Ordner,
                falls du die E-Mail nicht in deinem Posteingang findest!</p>

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