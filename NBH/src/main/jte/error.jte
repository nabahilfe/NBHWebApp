@param java.lang.Integer status = null
@param java.lang.String error = null
@param java.lang.String message = null

<!--
    A template to display error messages.

    Parameters:
    - status: The HTTP status code (Integer)
    - error: The error message (String)
    - message: The detailed message (String)
-->

<h1>StatusCode: ${status}</h1>
<h2>${error}</h2>
<p>${message}</p>

<p><a href="/">Zurück zur Startseite</a></p>

