@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h1 class="">Willkommen ${ctxt.getUsername()}!</h1>

    @if (ctxt.isAuthenticated())
        <p >Das ist deine Nachbarschaftshilfe Übersicht</p>
    @endif


`)