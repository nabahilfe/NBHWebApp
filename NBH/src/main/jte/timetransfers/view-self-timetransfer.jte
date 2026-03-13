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
            <div class="card">
                <div class="card-body bg-success bg-opacity-10">

                    <h4 class="card-title text-primary">Übergebener Zeitscheck</h4>
                    <hr/>
                    <p class="card-text text-success">Ich - <strong>${ctxt.getFullName()}</strong> - habe diesen Zeitscheck übergeben</p>

                    <label class="col-form-label">Kategorie</label>
                    <input type="text" class="form-control" disabled value="${tt.getOffer().getDescription()}">
                    @if(tt.getNote() != null && !tt.getNote().isEmpty())
                        <div class="small text-muted">Beschreibung: ${tt.getNote()}</div>
                    @endif

                    <label class="col-form-label">Datum</label>
                    <div class="col-sm-10">
                        <input type="date" class="form-control" disabled value="${formatISO(tt.getDateOfService())}">
                    </div>

                    <label class="col-form-label">Anzahl Stunden</label>
                    <input type="text" class="form-control" disabled value="${tt.getHours()} h">

                    <hr/>

                    <p class="card-text text-success"><strong>Empfänger des Zeitscheck</strong></p>
                    <input type="text" class="form-control" disabled  value="${tt.getToMember().getNameAndAddress()}">

                    <br/>
                    <form action="/timetransfers/fromself/${tt.getFromMember().getId()}"  method="get">
                        <button type="submit" class="btn btn-outline-primary bi-play-btn"> Weiteren Zeitscheck übergeben</button>
                    </form>
                </div>
            </div>
        </div>

    </div>

    </div>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)


`)