@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param List<TimeCheque> purchasedTimeCheques

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`




    <div class="container d-flex justify-content-center">

        <div class="row">

            <div class="col-auto">

                <div class="card shadow" style="background-color: #ECFEFF;"">
                    <div class="card-body">

                        <h4 class="card-title text-primary">Aktuelles Zeitguthaben</h4>

                        <hr/>

                        <h4>${member.getName()}</h4>
                        <h5>${member.getAddress()}</h5>

                        <hr/>

                        Aktuelles Zeitguthaben:<b> ${member.getAccumulatedHours()} h</b>

                        <hr/>

                        Zeitschecks herunterladen zum ausdrucken
                        <br/>
                        <br/>

                        <!-- download a personalized Zeitscheck.pdf -->
                        <form action="/download/timecheque-pdf" method="get">
                            <input type="hidden" name="memberId" value="${member.getId()}">
                            <div class="text-center">
                                <button type="submit" class="btn btn-primary bi-download"> Zeitschecks herunterladen</button>
                            </div>
                        </form>

                    </div>

                </div>

            </div>

        </div>

    </div>






    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)


    <div class="card">

       @template.components.list-timecheques-component(member = member, timeCheques = purchasedTimeCheques)

    </div>

    <br/>

    @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
        <form action="/members/${member.getId()}" method="get">
            <button type="submit" class="btn btn-outline-primary bi-arrow-left"> Zurück zum Mitglied</button>
        </form>
    @endif

`)



