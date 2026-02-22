@param String aboutUs = ""
@param String contact = ""
@param String events = ""
@param String news = ""
@param String faq = ""
@param String termsOfService = ""
@param String privacyPolicy = ""



@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3 class="">Willkommen bei der Nachbarschaftshilfe Maria Anzbach!</h3>

    <div class="row mt-4">

        <div class="col-12 col-lg-8 mb-3">
            <!-- Left column content (2/3) -->

            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h2 class="card-title">Wer wir sind und was wir tun</h2>
                    <hr/>
                    <p class="card-text">
                        $unsafe{aboutUs}
                    </p>
                </div>
            </div>

            <br>

            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h2 class="card-title">Unsere Termin</h2>
                    <hr/>
                    <p class="card-text">
                        $unsafe{events}
                    </p>
                </div>
            </div>


        </div>

        <div class="col-12 col-lg-4">
            <!-- Right column content (1/3) -->
            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h2 class="card-title">Kontakt</h2>
                    $unsafe{contact}
                </div>
            </div>
        </div>

    </div>

<div class="p-3 text-success-emphasis bg-success-subtle border border-success-subtle rounded-3">
  Example element with utilities
</div>
`)