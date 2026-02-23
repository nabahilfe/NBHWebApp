@import java.util.List
@import eu.nabahilfe.webapp.members.MemberBirthdayForm

@param List<MemberBirthdayForm> currentMonth
@param List<MemberBirthdayForm> nexttMonth

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Geburtstage</h3>
    <p class="text-muted">Aktueller und nächster Monat</p>

    @template.components.list-birthdates(currentMonth = currentMonth, nexttMonth = nexttMonth)

`)
