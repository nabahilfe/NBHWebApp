
@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput


@template.layout.mainpage(
    content = @`

    <h3>Abmelden</h3>
    <p>Klicke den Button um dich abzumelden.</p>

    <form action="/registration/logout" method="post">
        ${csrfHiddenInput}
        <button type="submit" class="btn btn-primary bi-box-arrow-left"> Abmelden</button>
    </form>

`)