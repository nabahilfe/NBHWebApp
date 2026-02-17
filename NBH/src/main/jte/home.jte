@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h1 class="">Nachbarschaftshilfe Maria Anzbach</h1>

    @if (ctxt.isAuthenticated())
        <h5>Willkommen ${ctxt.getUsername()}! Hier geht es zu deiner Nachbarschaftshilfe Übersicht</h5>
    @endif


    <div class="row mt-4">

        <div class="col-12 col-lg-8 mb-3">
            <!-- Left column content (2/3) -->

            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h4 class="card-title">Wer wir sind und was wir tun</h4>
                    <hr/>
                    <p class="card-text">
                        Die Nachbarschaftshilfe Maria Anzbach ist ein gemeinnütziger Verein, dessen Mitglieder Unterstützung und Hilfe auf Stunden-Tausch-Basis anbieten.
                        Auf einem Stundenkonto werden die erworbenen Zeitguthaben verbucht und Sie können sich wiederum von anderen helfen lassen.
                        <br/>
                        <br/>
                        Wir möchten dazu beitragen, dass Sie möglichst lange in Ihrer vertrauten Umgebung Ihre Lebensqualität genießen können.
                    </p>
                </div>
            </div>

            <br>

            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h4 class="card-title">Die nächsten Termine</h4>
                    <hr/>
                    <p class="card-text">
                        Die Nachbarschaftshilfe Maria Anzbach ist ein gemeinnütziger Verein, dessen Mitglieder Unterstützung und Hilfe auf Stunden-Tausch-Basis anbieten.
                        Auf einem Stundenkonto werden die erworbenen Zeitguthaben verbucht und Sie können sich wiederum von anderen helfen lassen.
                        <br/>
                        <br/>
                        Wir möchten dazu beitragen, dass Sie möglichst lange in Ihrer vertrauten Umgebung Ihre Lebensqualität genießen können.
                    </p>
                </div>
            </div>


        </div>

        <div class="col-12 col-lg-4">
            <!-- Right column content (1/3) -->
            <div class="card p-3 rounded-3" style="background-color: #d9f6d7;">
                <div class="card-body">
                    <h4 class="card-title">Kontakt</h4>
                    <hr/>
                    <p class="card-text">
                        Sie haben Fragen oder möchten Mitglied werden? Kontaktieren Sie uns gerne!
                        <br/>
                        <br/>
                        <strong>Adresse</strong>
                        <br/>Hauptstraße 123, 12345 Maria Anzbach
                        <br/>
                        <strong>Telefon</strong> <a href="tel:+13055555555">305-555-5555</a>
                        <br/>
                        <strong>E-Mail</strong> <a href= "mailto:name@email.com">name@email.com</a>
                   </p>


                </div>
            </div>
        </div>

    </div>

<div class="p-3 text-success-emphasis bg-success-subtle border border-success-subtle rounded-3">
  Example element with utilities
</div>
`)