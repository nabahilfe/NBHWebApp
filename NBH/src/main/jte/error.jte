@param java.lang.String status = null
@param java.lang.String error = null
@param java.lang.String message = null

@template.layout.main(content = @`

    <div class="container">

        <h1>${status}</h1>
        <p>${error}</p>
        @if(message != null && message != "")
            <p>Details: ${message}</p>
        @endif

        <p><a href="/">Zurück zur Startseite</a></p>
    </div>

`)



