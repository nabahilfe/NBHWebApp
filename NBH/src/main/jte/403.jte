<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
     ctxt = ctxt,
     content = @`

    <h3>403 - Zugriff verweigert</h3>
    <p>Du hast keine Berechtigung für diese Seite.</p>

`)