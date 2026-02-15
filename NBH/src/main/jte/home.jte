@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@template.layout.main(
    isAuthenticated = isAuthenticated,
    username = username,
    fullUsername = fullUsername,
    content = @`

    <div class="bg-white rounded-lg p-6">
        <h1 class="">Willkommen ${username}!</h1>

        @if (isAuthenticated)
            <p >Das ist deine Nachbarschaftshilfe Übersicht</p>
        @endif

    </div>

`)