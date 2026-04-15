@import java.util.List
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.members.Salutation
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.timetransfers.TimeTransfer
@import eu.nabahilfe.webapp.timecheques.TimeCheque

@param Member member
@param String roleNames
@param List<TimeCheque> purchasedTimeCheques
@param List<TimeTransfer> receivedTimeTransfers
@param List<TimeTransfer> givenTimeTransfers

@param java.lang.String action

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param Integer numberOfTimecheques = null


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container d-flex justify-content-center">
        <div class="row">
            <div class="col-auto">
                <div class="card shadow" style="background-color: #ECFEFF;">

                    <div class="card-body">

                        <h4 class="card-title text-primary">${ctxt.getFullName()}</h4>
                        <strong>${roleNames}</strong>
                        <hr/>


                        <p class="card-text">
                            ${member.getSalutation() != null ? member.getSalutation() + " " : ""}${member.getTitle() != null ? member.getTitle() + " " : ""}
                            <br/>
                            <strong>${member.getFirstName()} ${member.getLastName()}</strong>
                            <br/>
                            ${member.getStreet() + " " + member.getNumber()}
                            <br/>
                            ${member.getZip() + " " + member.getCity()}
                            <hr/>
                            Geburtstag: ${dateReverseDE(member.getBirthdate())}
                            @if(member.getEmail() != null)
                                <p class="card-text">
                                    E-Mail: <a href="mailto:${member.getEmail()}">${member.getEmail()}</a>
                                </p>
                            @endif
                            <hr/>
                            Mitgliedsnummer: ${member.getMemberNmbr()}
                            <br/>
                            Mitglied seit: ${dateReverseDE(member.getJoiningDate())}
                            <br/>
                            Austritt am: ${member.getResignationDate() != null ? dateReverseDE(member.getResignationDate()) : "aktives Mitglied"}
                            <hr/>
                            Einziehungsauftrag:  <strong>${member.getEinziehungsauftrgJaNein()}</strong>
                            <hr/>
                            Stundenguthaben: <strong>${member.getAccumulatedHours() != null ? member.getAccumulatedHours() + "" : "0"} h</strong>
                        </p>

                        <hr/>

                        <!-- download a personalized Zeitscheck.pdf -->
                        <form action="/download/timecheque-pdf" method="get">
                            <input type="hidden" name="memberId" value="${member.getId()}">
                            <div class="text-center">
                                Zeitschecks herunterladen zum Ausdrucken
                                <br/><br/>
                               <button type="submit" class="btn btn-primary bi-download"> Zeitschecks herunterladen</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <br/>
    <div>
        <i>Letztes Update: ${dateTimeDE(member.getUpdatedAt())}</i>
    </div>

    <br/>

    <div class="row">
        <div class="col-sm-12 mb-3 mb-sm-0">

            <ul class="nav nav-tabs" id="memberDetailTabs" role="tablist">
                <li class="nav-item" role="presentation">
                    <button class="nav-link active" id="timecheques-tab" data-bs-toggle="tab" data-bs-target="#timecheques-pane" type="button" role="tab" aria-controls="timecheques-pane" aria-selected="true">
                        Zeitschecks gekauft
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="transfers-tab" data-bs-toggle="tab" data-bs-target="#transfers-pane" type="button" role="tab" aria-controls="transfers-pane" aria-selected="false">
                        Zeitschecks übergeben
                    </button>
                </li>
                <li class="nav-item" role="presentation">
                    <button class="nav-link" id="received-tab" data-bs-toggle="tab" data-bs-target="#received-pane" type="button" role="tab" aria-controls="received-pane" aria-selected="false">
                        Zeitschecks erhalten
                    </button>
                </li>
            </ul>

            <div class="tab-content" id="memberDetailTabsContent">
                <div class="tab-pane fade show active" id="timecheques-pane" role="tabpanel" aria-labelledby="timecheques-tab" tabindex="0">
                    <div>
                        @template.components.list-timecheques-component(member = member, timeCheques = purchasedTimeCheques)
                    </div>
                </div>
                <div class="tab-pane fade" id="transfers-pane" role="tabpanel" aria-labelledby="transfers-tab" tabindex="0">
                    <div>
                        @template.components.list-timetransfers-given-component(member = member, transfers = givenTimeTransfers)
                    </div>
                </div>
                <div class="tab-pane fade" id="received-pane" role="tabpanel" aria-labelledby="received-tab" tabindex="0">
                    <div>
                        @template.components.list-timetransfers-received-component(member = member, transfers = receivedTimeTransfers)
                    </div>
                </div>
            </div>

        </div>

    </div>


`)