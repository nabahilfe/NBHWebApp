<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes


@param String aboutUs = ""
@param String contact = ""
@param String events = ""
@param String news = ""
@param String faq = ""
@param String termsOfService = ""
@param String privacyPolicy = ""
@param String legalNotice = ""
@param java.util.List<eu.nabahilfe.webapp.members.Member> boardMembers = java.util.Collections.emptyList()
@param String appVersion = ""
@param String buildTime = ""
@param String releaseNotesHtml = ""

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="row gx-2 gx-lg-4 mt-0">

         <div class="col-12 col-lg-4 mb-2">

            <!-- Left column content (1/3) -->


            @if(ctxt != null && ctxt.isAuthenticated())
                <!-- Mobile only content -->
                <div class="d-block d-md-none">
                    <div class="d-flex flex-column align-items-center gap-2">
                        @if(!ctxt.isSystemAdmin())
                            <a class="btn btn-lg btn-success bi bi-share-fill w-75" href="/timetransfers/fromself?fromMemberId=${ctxt.getUserId()}">&nbsp; Zeitscheck übergeben</a>
                            <a class="btn btn-lg btn-success bi bi-bag-plus w-75" href="/timecheques/newfromself/${ctxt.getUserId()}">&nbsp; Zeitschecks kaufen</a>
                        @endif
                        <a class="btn btn-lg btn-success bi bi-clipboard-data w-75" href="/members/mydata/${ctxt.getUserId()}">&nbsp; Meine Daten</a>
                        <p></p>

                        <button type="button" class="btn btn-outline-danger bi bi-box-arrow-left w-75" data-bs-toggle="modal" data-bs-target="#logoutConfirmModal">&nbsp; Abmelden</button>

                        <!-- Logout Confirm Modal -->
                        <div class="modal fade" id="logoutConfirmModal" tabindex="-1" aria-labelledby="logoutConfirmModalLabel" aria-hidden="true">
                            <div class="modal-dialog modal-dialog-centered">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="logoutConfirmModalLabel">Abmelden</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Schließen"></button>
                                    </div>
                                    <div class="modal-body">
                                        Möchtest du dich wirklich abmelden?
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
                                        <form action="/registration/logout" method="post">
                                            ${csrfHiddenInput}
                                            <button type="submit" class="btn btn-danger bi bi-box-arrow-left"> Abmelden</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <p></p>
                    </div>
                </div>

                <!-- Icon only if Desktop content -->
                <div class="d-none d-md-block">
                    <img src="/images/NBH-MA-Logo-720.webp"
                            class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid"
                            alt="Logo Nachbarschaftshilfe Maria Anzbach helfende Hände" title="Originalfoto von Austin Kehmeier auf Unsplash">
                </div>

            @else
                <!-- if not authenticated, always show Logo -->
                <img src="/images/NBH-MA-Logo-720.webp"
                        class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid"
                        alt="Logo Nachbarschaftshilfe Maria Anzbach helfende Hände" title="Originalfoto von Austin Kehmeier auf Unsplash">
            @endif


            <!-- Mobile only content -->
            <div class="d-block d-md-none">
                @if (ctxt == null || (ctxt != null && !ctxt.isAuthenticated()))
                    <div class="d-flex flex-column align-items-center gap-2">
                        <a class="btn btn-lg btn-success bi-box-arrow-in-right w-75" href="/registration/login">&nbsp; Anmelden</a>
                        <a class="btn btn-success bi-person-plus w-75" href="/registration/email">&nbsp; Registrieren</a>
                        <p></p>
                    </div>
                @endif
            </div>

			<!-- FIXME: remove this with next big release -->
            <div class="alert alert-warning shadow d-flex justify-content-between align-items-center" role="alert">
              <span class="badge text-bg-success align-text-top">Neu</span>
                <span class="text-center flex-grow-1">
                    <a href="/gallery/user-list"><strong>Foto-Galerien - schau dir das an!</strong></a>
                    <br/>
					<small>&nbsp;Die Foto-Galerien sind auch über den Menüpunkt <strong>Fotos & Dokumente</strong> zu erreichen.</small>
                </span>
              &nbsp;<span class="badge text-bg-success align-text-top">Neu</span>
            </div>

            <!-- Contact info at this position only if Desktop mode -->
            <div class="d-none d-md-block">
                <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4" style="background-color: #ECFEFF;">
                    <h2>Kontakt</h2>
                    <p class="card-text">
                        $unsafe{contact}
                    </p>
                </div>
            </div>

        </div>


        <div class="col-12 col-lg-8 mb-3">
            <!-- Right column content (2/3) -->

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4"
                    style="background-color: #ECFEFF;">
                <h2>Wer wir sind und was wir tun</h2>
                <p class="card-text">
                    $unsafe{aboutUs}
                </p>
            </div>

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4"
                    style="background-color: #ECFEFF;">
                <h2>Termine und Veranstaltungen</h2>
                <p class="card-text">
                    $unsafe{events}
                </p>
                <i>WICHTIG: Termine können sich ändern, erkundigen Sie sich ob der Termin planmäßig stattfindet.</i>
            </div>


            <!-- Contact info at this position only if Mobile mode -->
            <div class="d-block d-md-none">
                <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4"
                style="background-color: #ECFEFF;">
                    <h2>Kontakt</h2>
                    <p class="card-text">
                        $unsafe{contact}
                    </p>
                </div>
            </div>


        </div>


    </div>



`)
