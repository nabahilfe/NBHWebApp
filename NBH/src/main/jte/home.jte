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

    <h1 class="">Willkommen bei der Nachbarschaftshilfe Maria Anzbach!</h1>

    <br/>

    <div class="row mt-4">

        <div class="col-12 col-lg-8 mb-3">
            <!-- Left column content (2/3) -->

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow p-3 mb-5"
                    style="background-color: #ECFEFF;">
                <h2>Wer wir sind und was wir tun</h2>
                <p class="card-text">
                    $unsafe{aboutUs}
                </p>
            </div>

        </div>

        <div class="col-12 col-lg-4 mb-3 ">
            <!-- Right column content (1/3) -->

            <img src="/images/nbh-logo.002.png" alt="NBH Logo">
            <!--img src="/images/nbh-logo.002.png" class="rounded mx-auto d-block rounded-3 shadow p-3 bg-transparent" alt="NBH Logo"-->

        </div>

        <div class="col-12 col-lg-8 mb-3">
            <!-- Left column content (2/3) -->

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow p-3 mb-5"
                    style="background-color: #ECFDF5;">
                <h2>Termine und Veranstaltungen</h2>
                <p class="card-text">
                    $unsafe{events}
                </p>
            </div>

        </div>

        <div class="col-12 col-lg-4 mb-3">

            <div class="p-3 text-success-emphasis border border-success-subtle rounded-3 shadow p-3 mb-5" style="background-color: #d9f6d7;">
                <h2>Kontakt</h2>
                <p class="card-text">
                    $unsafe{contact}
                </p>
            </div>

        </div>

        <div>

            <p><a href="/home/legal-and-privacy-policy">Datenschutzerklärung und Impressum</a></p>

        </div>

    </div>

`)