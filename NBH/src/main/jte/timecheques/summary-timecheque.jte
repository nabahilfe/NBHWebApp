@import eu.nabahilfe.webapp.members.Member
@import eu.nabahilfe.webapp.timeexchange.TimeCheque

@param Member member

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@template.layout.main(content = @`

    <h3 class="text-primary">Aktuelles Zeitguthaben</h3>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>

    <div class="card text-center">
        <div class="card-header">
            <h4 class="text-primary">${member.getName()}</h4>
            <p>${member.getAddress()}</p>
        </div>
        <ul class="list-group list-group-flush">
            <li class="list-group-item">Zeitguthaben</li>
            <li class="list-group-item">${member.getAccumulatedHours()} h</li>
        </ul>
    </div>
    
    <p/>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)


