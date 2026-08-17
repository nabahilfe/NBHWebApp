<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes


@param String privacyPolicy = ""
@param String legalNotice = ""
@param java.util.List<eu.nabahilfe.webapp.members.Member> boardMembers = java.util.Collections.emptyList()
@param java.util.List<eu.nabahilfe.webapp.members.Member> auditorMembers = java.util.Collections.emptyList()

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes = null


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h1 class="">Rechtliche Informationen NBH Maria Anzbach</h1>

    <div class="row mt-4">

        <div class="col-12 col-lg-6 mb-3">
            <!-- Right column content (50%) -->

            @template.components.list-board-members(boardMembers = boardMembers, auditorMembers = auditorMembers)

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4 bg-white">
                <h2>Impressum</h2>
                <p class="card-text">
                    $unsafe{legalNotice}
                </p>
            </div>

        </div>

        <div class="col-12 col-lg-6 mb-3">
            <!-- Left column content (50%) -->

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4 bg-white">
                <h2>Datenschutzerklärung</h2>
                <p class="card-text">
                    $unsafe{privacyPolicy}
                </p>
            </div>

        </div>

    </div>


`)