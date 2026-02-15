@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.main(
    ctxt = ctxt,
    content = @`

    <div class="bg-white rounded-lg p-6">
        <h1 class="">Willkommen ${ctxt.getUsername()}!</h1>

        @if (ctxt.isAuthenticated())
            <p >Das ist deine Nachbarschaftshilfe Übersicht</p>
        @endif

    </div>

`)