<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes

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
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    ctxt = ctxt,
    content = @`

    <h2>StatusCode: ${status == null ? -1 : status}</h2>
    <h2>${error == null ? "Ungültige Operation" : error}</h2>
    <p>${message}</p>
`)