<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Deine Registrierung wurde erfolgreich abgeschlossen!</h3>
    <p>Du kannst dich nun mit deiner E-Mail und deinem Passwort anmelden.</p>

    <a class="btn btn-primary" href="/registration/login">Anmelden</a>

`)