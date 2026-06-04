<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@param String aboutUs = ""
@param String contact = ""
@param String events = ""
@param String news = ""
@param String faq = ""
@param String termsOfService = ""
@param String privacyPolicy = ""
@param String legalNotice = ""
@param java.util.List<eu.nabahilfe.webapp.members.Member> boardMembers = java.util.Collections.emptyList()



@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="row gx-2 gx-lg-4 mt-0">

         <div class="col-12 col-lg-4 mb-2">

            <!-- Left column content (1/3) -->
            <!--
            <img src="/images/nbh-logo-gelb.png" class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid" alt="NBH Logo">
            <img src="/images/nbh-logo-neu-bg-white.png" class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid" alt="NBH Logo">
            -->
            <img src="/images/NBH-Helping-Hands-Logo-2.png" class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid" alt="NBH Logo" title="Originalfoto von Austin Kehmeier auf Unsplash">
            <!--
            <img src="/images/nbh-logo-neu-filled.png" class="border border-success-subtle rounded mx-auto d-block shadow mb-4 bg-transparent img-fluid" alt="NBH Logo">
            -->

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4"
            style="background-color: #ECFEFF;">
                <h2>Kontakt</h2>
                <p class="card-text">
                    $unsafe{contact}
                </p>

                <br/>
                <a href="/home/legal-and-privacy-policy">Datenschutzerklärung und Impressum</a>

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

                <br/>
                Weitere Informationen LINK-TBD
            </div>

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow mb-4"
                    style="background-color: #ECFEFF;">
                <h2>Termine und Veranstaltungen</h2>
                <p class="card-text">
                    $unsafe{events}
                </p>
                <i>WICHTIG: Termine können sich ändern, erkundigen Sie sich ob der Termin planmäßig stattfindet.</i>
            </div>

        </div>


    </div>

`)