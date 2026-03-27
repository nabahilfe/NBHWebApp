
<!--
    A template to display error messages.

    Parameters:
    - status: The HTTP status code (Integer)
    - error: The error message (String)
    - message: The detailed message (String)
-->


@param eu.nabahilfe.webapp.security.ViewContext ctxt

@param java.lang.Integer status = null
@param java.lang.String error = null
@param java.lang.String message = null


@template.layout.mainpage(
    ctxt = ctxt,
    content = @`

    <h2>StatusCode: ${status}</h2>
    <h2>${error}</h2>
    <p>${message}</p>
`)


