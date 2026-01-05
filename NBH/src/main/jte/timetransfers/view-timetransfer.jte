@import java.util.List

@import eu.nabahilfe.webapp.timecheques.TimeTransfer
@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.org.Offer

@param TimeTransfer tt = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3 class="text-primary">Zeitscheck angelgt</h3>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

    <p/>

    <div class="row">
      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">

            <h5 class="card-title text-primary">Leistungsempfänger</h5>
            <p class="card-text text-success">Gibt Zeitscheck an Leistungserbringer</p>

            <input type="text" class="form-control" readonly value="${tt.getFromMember().getName()}">

            <label for="offer" class="col-sm-2 col-form-label">Kategorie</label>
            <input type="text" class="form-control" readonly value="${tt.getOffer().getDescription()}">

            <label for="dateOfService" class="col-sm-10 col-form-label">Leistungsdatum</label>
            <div class="col-sm-10">
              <input type="date" class="form-control" readonly value="${tt.getDateOfServiceString()}">
            </div>

            <!-- Hours selector: choose 1..5 hours. Preselect when ttf provided. -->
            <label for="stunden" class="col-form-label">Anzahl Stunden</label>
            <input type="text" class="form-control" readonly value="${tt.getHours()} h">

          </div>
        </div>
      </div>

      <div class="col-sm-6">
        <div class="card">
          <div class="card-body">

            <h5 class="card-title text-primary">Leistungserbringer</h5>
            <p class="card-text text-success">Erhält Zeitscheck vom Leistungsempfänger</p>

            <input type="text" class="form-control" readonly value="${tt.getToMember().getName()}">

          </div>
        </div>
      </div>

    </div>

    <p>

    <form action="/timetransfers/new" method="get">
        <button type="submit" class="btn btn-primary bi-card-checklist"> Weiteren Zeitscheck verbuchen</button>
    </form>
    <p/>

    @template.components.display-statusmessage(successMessage, errorMessage)


`)




