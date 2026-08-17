<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.List
@import eu.nabahilfe.webapp.members.MemberBirthdayForm

@param List<MemberBirthdayForm> currentMonth
@param List<MemberBirthdayForm> nexttMonth

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

    <h3>Geburtstage</h3>
    <p class="text-muted">Aktueller und nächster Monat</p>

    @template.components.list-birthdates(currentMonth = currentMonth, nexttMonth = nexttMonth)

`)