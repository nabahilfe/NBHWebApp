@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false

@param eu.nabahilfe.webapp.security.ViewContext ctxt

@template.layout.mainpage(
    isAuthenticated = isAuthenticated,
    username = username,
    fullUsername = fullUsername,
    ctxt = ctxt,
    content = @`

    <div class="bg-white rounded-lg p-6">
        <h1 class=""></h1>
        <p class="text-gray-600">Da fehlt noch was...!</p>
    </div>
`)