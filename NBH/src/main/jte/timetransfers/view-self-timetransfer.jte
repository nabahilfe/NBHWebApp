<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.timetransfers.TimeTransfer
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.org.Offer

@param TimeTransfer tt = null

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
                <div class="card shadow" style="background-color: #ECFEFF;">
                    <div class="card-body">

                        <h4 class="card-title text-primary">Übergebener Zeitscheck</h4>
                        <hr/>

                        <label class="col-form-label"><strong>Empfänger des Zeitschecks</strong></label>
                        <input type="text" class="form-control" disabled  value="${tt.getToMember().getNameAndAddress()}">


                        <label class="col-form-label">Kategorie</label>
                        <input type="text" class="form-control" disabled value="${tt.getOffer().getCode()} ${tt.getOffer().getDescription()}">
                        @if(tt.getNote() != null && !tt.getNote().isEmpty())
                            <div class="small text-muted">Beschreibung: ${tt.getNote()}</div>
                        @endif

                    <div class="row g-2 mb-1">

                        <div class="col-6">
                            <label class="col-form-label">Datum</label>
                            <div class="col-sm-10">
                                <input type="date" class="form-control" disabled value="${formatISO(tt.getDateOfService())}">
                            </div>
                        </div>

                        <div class="col-6">
                            <label class="col-form-label">Anzahl Stunden</label>
                            <input type="text" class="form-control" disabled value="${tt.getHours()} h">
                        </div>

                    </div>

                        <br/>
                        <form action="/timetransfers/fromself" method="get">
                            <input type="hidden" name="fromMemberId" value="${tt.getFromMember().getId()}">
                            <button type="submit" class="btn btn-outline-primary bi-play-btn"> Weiteren Zeitscheck übergeben</button>
                        </form>

                    </div>
                </div>
            </div>
        </div>
    </div>

`)