<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.textcontent.TextContentType
@import eu.nabahilfe.webapp.GlobalModelAttributes


@param gg.jte.Content content
@param String searchTerm = null

@param GlobalModelAttributes globalModelAttributes = null
@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput = null
@param eu.nabahilfe.webapp.security.ViewContext ctxt = null


<!doctype html>
<html lang="en" data-bs-theme="auto">
    <head>

        <meta name="description" content="NBH - Startseite der Nachbarschaftshilfe Maria Anzbach.">

        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Nachbarschaftshilfe Maria Anzbach - gegenseitige Hilfe auf Stundentauschbasis</title>

        <link rel="manifest" href="/manifest.webmanifest">
        <link rel="apple-touch-icon" href="/icons/apple-touch-icon.png">
        <meta name="apple-mobile-web-app-title" content="NBH">

        <!-- Verbindung zum CDN früh aufbauen -->
        <link rel="preconnect" href="https://cdn.jsdelivr.net" crossorigin>

        <link rel="preload"
              href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
              as="style">

        <link rel="preload"
              href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css"
              as="style">

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

            .dropdown-submenu {
                position: relative;
            }

            .dropdown-submenu > .dropdown-menu {
                top: 0;
                left: 100%;
                margin-top: -1px;
            }

            .dropdown-submenu:hover > .dropdown-menu {
                display: block;
            }

            .dropdown-submenu > .dropdown-toggle {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

            .dropdown-submenu > .dropdown-toggle::after {
                margin-left: 1rem;
                flex-shrink: 0;
                transform: rotate(-90deg);
                float: none;   /* wichtig */
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

                        <!-- Desktop only content -->
                        <!-- Look for mobile only content in home.jte, there are alternative button navigations for mobile -->
                        <div class="d-none d-md-block">
                            <!-- Dropdown User -->
                            <li class="nav-item dropdown">
                                @if (ctxt != null && ctxt.isAuthenticated())
                                    <button class="nav-link dropdown-toggle bi-person-square" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                        ${ctxt.getFirstNameLastName()}
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li><a class="dropdown-item" href="/members/mydata/${ctxt.getUserId()}">Meine Daten</a></li>
                                        @if (!ctxt.isSystemMember()) <!-- no timecheques for SystemMembers -->
                                            <li><a class="dropdown-item" href="/timetransfers/fromself?fromMemberId=${ctxt.getUserId()}">Zeitscheck übergeben</a></li>
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
                        </div>

                        <!-- Dropdown Mitglied -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Mitglieder
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/members">Mitglieder verwalten</a></li>
                                    <li><a class="dropdown-item" href="/members/resigned">Ausgetretene Mitglieder</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/open-membership-fees">Mitgliedsbeiträge vorschreiben</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/validate-addresses">Adressen validieren</a></li>
                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Zeitschecks -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "TIME_KEEPER", "EXECUTIVE_MEMBER"))
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
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER", "TREASURER", "AUDITOR"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link"
                                    type="button"
                                    data-bs-toggle="dropdown"
                                    data-bs-display="static">
                                    Buchungen
                                </button>

                                <ul class="dropdown-menu">

                                    <!-- Offene Buchungen -->
                                    <li class="dropdown-submenu">
                                        <a class="dropdown-item dropdown-toggle" href="#">
                                            Offene Buchungen
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="/timecheques/unaccounted">Offene Zeitschecks</a></li>
                                            <li><a class="dropdown-item" href="/members/unaccounted-mbshipfees">Offene Mitgliedsbeiträge</a></li>
                                            <li><a class="dropdown-item" href="/accountings/misc-unaccounted">Sonstige offene Buchung</a></li>
                                        </ul>
                                    </li>

                                    <!-- Einnahmen / Ausgaben -->
                                    @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
                                        <li class="dropdown-submenu">
                                            <a class="dropdown-item dropdown-toggle" href="#">
                                                Einnahmen / Ausgaben
                                            </a>
                                            <ul class="dropdown-menu">
                                                <li><a class="dropdown-item" href="/accountings/income">Einnahme erfassen</a></li>
                                                <li><a class="dropdown-item" href="/accountings/expense">Ausgabe erfassen</a></li>
                                            </ul>
                                        </li>
                                    @endif

                                    <li><a class="dropdown-item" href="/accountings/show-accountings">Umsätze</a></li>
                                </ul>


                            </li>
                        @endif


                        <!-- Dropdown Content -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER", "SECRETARY"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link"
                                        type="button"
                                        data-bs-toggle="dropdown"
                                        data-bs-display="static">
                                    Content
                                </button>

                                <ul class="dropdown-menu">

                                    <!-- Texte -->
                                    <li class="dropdown-submenu">
                                        <a class="dropdown-item dropdown-toggle" href="#">
                                            Texte
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.ABOUT_US}">
                                                ${TextContentType.ABOUT_US.getCode()}</a></li>

                                            <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.CONTACT}">
                                                ${TextContentType.CONTACT.getCode()}</a></li>

                                            <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.EVENTS}">
                                                ${TextContentType.EVENTS.getCode()}</a></li>

                                            <!--li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.NEWS}">
                                                ${TextContentType.NEWS.getCode()}</a></li-->

                                            <!--li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.FAQ}">
                                                ${TextContentType.FAQ.getCode()}</a></li-->

                                            <!--li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.TERMS_OF_SERVICE}">
                                                ${TextContentType.TERMS_OF_SERVICE.getCode()}</a></li-->

                                            <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.PRIVACY_POLICY}">
                                                ${TextContentType.PRIVACY_POLICY.getCode()}</a></li>

                                            <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.LEGAL_NOTICE}">
                                                ${TextContentType.LEGAL_NOTICE.getCode()}</a></li>
                                        </ul>
                                    </li>

                                    <!-- image management -->
                                    <li class="dropdown-submenu">
                                        <a class="dropdown-item dropdown-toggle" href="#">
                                            Bilder
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="/gallery/list">Galerie</a></li>
                                            <li><a class="dropdown-item" href="/images/upload">Bild hochladen</a></li>
                                            <li><a class="dropdown-item" href="/images/categories">Kategorien</a></li>
                                        </ul>
                                    </li>


                                    <!-- FIXME: enable this Dokumente menu once we have a proper document management system in place
                                    <li class="dropdown-submenu">
                                        <a class="dropdown-item dropdown-toggle" href="#">
                                            Dokumente
                                        </a>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="/documents">Alle Dokumente</a></li>
                                            <li><a class="dropdown-item" href="/documents/upload">Dokument hochladen</a></li>
                                            <li><a class="dropdown-item" href="/documents/categories">Kategorien</a></li>
                                        </ul>
                                    </li>
                                    -->

                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Domain Values -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER", "TREASURER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Einstellungen
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/domaintypes">Gebühren-Sätze</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/roles">Rollen und Berechtigungen</a></li>
                                    <!--li><a class="dropdown-item" href="#">...</a></li>-->
                                    @if (ctxt != null && ctxt.hasAnyRole("ADMIN"))
                                        <li><hr class="dropdown-divider"></li>
                                        <li>
                                            <form action="/email/sendTestEmail" method="post" class="m-0 p-0">
                                                ${csrfHiddenInput}
                                                <button type="submit" class="dropdown-item">Test E-Mail senden</button>
                                            </form>
                                        </li>
                                    @endif
                                </ul>
                            </li>
                        @endif

                        <!-- Dropdown Auswertung -->
                        @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER", "BOARD_MEMBER"))
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Auswertungen
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/members/birthdays">Geburtstage</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/timecheques/booking-report">Fremd-verbuchte Zeitschecks</a></li>
                                    <li><a class="dropdown-item" href="/timecheques/self-booking-report">Selbst-verbuchte Zeitschecks</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/timecheques/statistics">Zeitschecks pro Mitglied</a></li>
                                    <li><a class="dropdown-item" href="/timecheques/category-statistics">Zeitschecks pro Kategorie</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/statistics">Mitgliederentwicklung</a></li>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="/members/registered">Registrierte Mitglieder</a></li>
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
        <div class="container-fluid px-2 px-sm-3 px-lg-5 pb-4">
            ${content}
        </div>


        <footer class="text-center text-muted small py-3 mt-2 border-top">
            <a href="/home/legal-and-privacy-policy" class="text-muted text-decoration-none">
                <strong>§ Rechtliche Informationen</strong>
            </a>
            &nbsp;|&nbsp;
            Version ${globalModelAttributes.appVersion()} &mdash; Build: ${globalModelAttributes.buildTime()}
            &nbsp;|&nbsp;
            <a href="#" class="text-muted text-decoration-none" data-bs-toggle="modal" data-bs-target="#releaseNotesModal">
                <i class="bi bi-journal-text" style="-webkit-text-stroke: 0.3px currentColor;"></i> <strong>Release Notes</strong>
            </a>
        </footer>

        <!-- Release Notes Modal -->
        <div class="modal fade" id="releaseNotesModal" tabindex="-1" aria-labelledby="releaseNotesModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable modal-lg">
                <div class="modal-content border-success-subtle">
                    <div class="modal-header" style="background-color: #77DD77;">
                        <h5 class="modal-title fw-semibold" id="releaseNotesModalLabel">
                            <i class="bi bi-journal-text me-2"></i>Release Notes
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Schließen"></button>
                    </div>
                    <div class="modal-body px-4 py-3">
                        $unsafe{globalModelAttributes.releaseNotesHtml()}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-success" data-bs-dismiss="modal">Schließen</button>
                    </div>
                </div>
            </div>
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
