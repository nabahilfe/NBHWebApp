<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.textcontent.TextContentType


@param gg.jte.Content content
@param String searchTerm = null


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput = null
@param eu.nabahilfe.webapp.security.ViewContext ctxt = null


<!doctype html>
<html lang="en" data-bs-theme="auto">
    <head>

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>NBH</title>

        <!-- Bootstrap CSS -->
        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css">

        <link rel="stylesheet"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">

        <meta name="theme-color" content="#712cf9" />

        <style>
            body {
                padding-top: 90px;
                background: #e9fbe9;
            }
        </style>

    </head>


    <body>
        <!-- Navbar -->
        <nav class="navbar navbar-expand-lg navbar-light fixed-top" style="background-color: #77DD77;">
            <div class="container-fluid">
                <a class="navbar-brand" href="/">NBH Maria Anzbach</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>


                <!-- Navbar Collapse -->
                <div class="collapse navbar-collapse" id="navbarSupportedContent">

                    <!-- Links links -->
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">

                        <!-- Dropdown User -->
                        <li class="nav-item dropdown">
                            @if (ctxt != null && ctxt.isAuthenticated())
                                <button class="nav-link dropdown-toggle bi-person-square" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    ${ctxt.getFirstNameLastName()}
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/members/mydata/${ctxt.getUserId()}">Meine Daten</a></li>
                                    @if (! ctxt.isSystemAdmin()) <!-- no timecheques for SystemAdmin -->
                                        <li><a class="dropdown-item" href="/timetransfers/fromself?fromMemberId=${ctxt.getUserId()}">Zeitschecks übergeben</a></li>
                                            <li><a class="dropdown-item" href="/timecheques/newfromself/${ctxt.getUserId()}">Zeitschecks kaufen</a></li>
                                    @endif
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <form action="/registration/logout" method="post">
                                            ${csrfHiddenInput}
                                            <button type="submit" class="dropdown-item btn btn-primary bi-box-arrow-left"> Abmelden</button>
                                        </form>
                                    </li>
                                </ul>
                            @else
                                <button class="nav-link dropdown-toggle bi-person" type="button" data-bs-toggle="dropdown" data-bs-display="static">
                                    Anmelden / Registrieren
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item bi-box-arrow-in-right" href="/registration/login"> Anmelden</a></li>
                                    <li><a class="dropdown-item" href="/registration/email">Registrieren / Passwort ändern</a></li>
                                </ul>
                            @endif
                        </li>


                        <!-- Dropdown Mitglied -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Mitglieder
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/members">Mitglieder verwalten</a></li>
                                    <li><a class="dropdown-item" href="/roles">Rollen verwalten</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/birthdays">Geburstage</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/open-membership-fees">Offene Mitgliedsbeiträge</a></li>
                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Zeitschecks -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "TIME_KEEPER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Zeitschecks
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/timetransfers/new">Zeitschecks verbuchen</a></li>
                                    <li><a class="dropdown-item" href="/members">Zeitscheck erstellen</a></li>
                                </ul>
                            </li>
                        @endif

                        <!-- Dropdown Buchungen -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "TIME_KEEPER", "TREASURER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Buchungen
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/timecheques/unaccounted">Offene Zeitschecks</a></li>
                                    <li><a class="dropdown-item" href="/members/unaccounted-mbshipfees">Offene Mitgliedsbeiträge</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="#">Sonstige Buchung</a></li>
                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Text Content -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER", "SECRETARY"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Content
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.ABOUT_US}">
                                            ${TextContentType.ABOUT_US.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.CONTACT}">
                                            ${TextContentType.CONTACT.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.EVENTS}">
                                            ${TextContentType.EVENTS.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.NEWS}">
                                            ${TextContentType.NEWS.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.FAQ}">
                                            ${TextContentType.FAQ.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.TERMS_OF_SERVICE}">
                                            ${TextContentType.TERMS_OF_SERVICE.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.PRIVACY_POLICY}">
                                            ${TextContentType.PRIVACY_POLICY.getCode()}</a></li>
                                    <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.LEGAL_NOTICE}">
                                            ${TextContentType.LEGAL_NOTICE.getCode()}</a></li>
                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Domain Values -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "TIME_KEEPER", "TREASURER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Einstellungen
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/domaintypes">Gebühren-Sätze</a></li>
                                    <li><a class="dropdown-item" href="#">Offene Mitgliedsbeiträge</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="#">Sonstige Buchung</a></li>
                                </ul>
                            </li>
                        @endif

                        <!-- Dropdown Auswertung -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "BOARD_MEMBER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Auswertungen
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/timecheques/statistics">Zeitschecks pro Mitglied</a></li>
                                    <li><a class="dropdown-item" href="/timecheques/category-statistics">Zeitschecks pro Kategorie</a></li>
                                    <li><a class="dropdown-item" href="/members/statistics">Mitgliederentwicklung</a></li>
                                </ul>
                            </li>
                        @endif



                    </ul>


                    <!-- Suchformular rechts -->
                    <form class="d-flex" role="search" id="memberSearchForm" action="/members/search" method="get">
                        <input class="form-control me-2" type="search" name="searchTerm"
                               id="memberSearchInput" placeholder="Mitglieder" value="${searchTerm != null ? searchTerm : ""}" />
                        <button class="btn btn-success" type="submit">Suchen</button>
                    </form>

                </div>
            </div>
        </nav>

        <!-- Haupt-Content -->
        <div class="container-fluid justify-content-center align-items-center px-1 px-md-4 pb-4">
            ${content}
        </div>

        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>


        <!-- Persist member search term across pages via sessionStorage -->
        <script>
            (function () {
                const KEY = 'nbh_memberSearch';
                const form  = document.getElementById('memberSearchForm');
                const input = document.getElementById('memberSearchInput');

                // Restore saved term if the field is empty (no server-side value was injected)
                if (input && !input.value) {
                    const saved = sessionStorage.getItem(KEY);
                    if (saved) input.value = saved;
                }

                // Save the current value whenever search is submitted
                if (form) {
                    form.addEventListener('submit', function () {
                        sessionStorage.setItem(KEY, input.value);
                    });
                }

                // Also keep sessionStorage in sync with what the server rendered
                if (input && input.value) {
                    sessionStorage.setItem(KEY, input.value);
                }
            })();
        </script>

        <!-- Auto-dismiss success alerts -->
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const alertEl = document.getElementById('successAlert');
                if (alertEl) {
                    setTimeout(function () {
                        const bsAlert = new bootstrap.Alert(alertEl);
                        bsAlert.close();
                    }, 5000);
                }
            });
        </script>

    </body>
</html>
