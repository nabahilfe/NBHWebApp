<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

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

@param int selectedYear


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
                            @if((member.getSalutation() != null && !member.getSalutation().isEmpty()) || member.getTitle() != null && !member.getTitle().isEmpty())
                                ${member.getSalutation() != null ? member.getSalutation() + " " : ""}${member.getTitle() != null ? member.getTitle() + " " : ""}
                                <br/>
                            @endif
                            <strong>${member.getFirstName()} ${member.getLastName()}</strong>
                            <br/>
                            ${member.getStreet() + " " + member.getNumber()}
                            <br/>
                            ${member.getZip() + " " + member.getCity()}
                            <hr/>
                            Geburtstag: ${dateReverseDE(member.getBirthdate())}
                            @if(member.getEmail() != null)
                                <br/>
                                 E-Mail: <a href="mailto:${member.getEmail()}">${member.getEmail()}</a>
                            @endif
                            @if(member.getPhoneNumber() != null)
                                <br/>
                                Telefon: ${member.getPhoneNumber()}
                            @endif
                            <hr/>
                            Mitgliedsnummer: ${member.getMemberNmbr()}
                            <br/>
                            Mitglied seit: ${dateReverseDE(member.getJoiningDate())}
                            <hr/>
                            Einziehungsauftrag:  <strong>${member.getEinziehungsauftrgJaNein()}</strong>
                            <br/>
                            Stundenguthaben: <strong>${member.getAccumulatedHours() != null ? member.getAccumulatedHours() + "" : "0"} h</strong>
                        </p>


                          @if(! member.isSystemMember())
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
                        @endif

                    </div>
                </div>
            </div>
        </div>
    </div>


    <hr/>
    <h5>Zeitscheck-Käufe und Zeitscheck-Transfers</h5>

    @template.components.list-timecheque-data-component(member = member, timeCheques = purchasedTimeCheques, transfersGiven = givenTimeTransfers,
            transfersReceived = receivedTimeTransfers, selectedYear = selectedYear)


`)