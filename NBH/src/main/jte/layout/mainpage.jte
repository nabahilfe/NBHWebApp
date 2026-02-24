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
                                    <li><a class="dropdown-item" href="/timetransfers/fromself/${ctxt.getUserId()}">Zeitscheck übergeben</a></li>
                                    <li><a class="dropdown-item" href="/timecheques/buyself/${ctxt.getUserId()}">Zeitscheck kaufen</a></li>
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
                                    Login
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item bi-box-arrow-in-right" href="/registration/login"> Anmelden</a></li>
                                    <li><a class="dropdown-item" href="/registration/email">Registrieren / Passwort ändern</a></li>
                                </ul>
                            @endif
                        </li>


                        <!-- Dropdown Mitglied -->
                        @if (ctxt != null && ctxt.isAuthenticated())
                            <li class="nav-item dropdown">
                                <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                    Mitglieder
                                </button>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="/members">Mitglieder verwalten</a></li>
                                    <li><a class="dropdown-item" href="/roles">Rollen verwalten</a></li>
                                    <li><a class="dropdown-item" href="/members/birthdays">Geburstage</a></li>
                                    @if (ctxt != null && ctxt.isAdmin())
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="/admin">Admin Dashboard</a></li>
                                    @endif

                                </ul>
                            </li>
                        @endif


                        <!-- Dropdown Zeitschecks -->
                        <li class="nav-item dropdown">
                            <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                Zeitschecks
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="/timetransfers/new">Zeitschecks verbuchen</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="#">Meine Zeitschecks</a></li>
                            </ul>
                        </li>


                        <!-- Dropdown Buchungen -->
                        <li class="nav-item dropdown">
                            <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                Buchungen
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="/timecheques/unaccounted">Offene Zeitschecks</a></li>
                                <li><a class="dropdown-item" href="#">Offene Mitgliedsbeiträge</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="#">Sonstige Buchung</a></li>
                            </ul>
                        </li>


                        <!-- Dropdown Text Content -->
                        <li class="nav-item dropdown">
                            <button class="nav-link dropdown-toggle btn btn-link" type="button" data-bs-toggle="dropdown" data-bs-display="static" aria-expanded="true">
                                Content
                            </button>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.ABOUT_US}">${TextContentType.ABOUT_US.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.CONTACT}">${TextContentType.CONTACT.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.EVENTS}">${TextContentType.EVENTS.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.NEWS}">${TextContentType.NEWS.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.FAQ}">${TextContentType.FAQ.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.TERMS_OF_SERVICE}">${TextContentType.TERMS_OF_SERVICE.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.PRIVACY_POLICY}">${TextContentType.PRIVACY_POLICY.getCode()}</a></li>
                                <li><a class="dropdown-item" href="/textcontent/edit/cc/${TextContentType.LEGAL_NOTICE}">${TextContentType.LEGAL_NOTICE.getCode()}</a></li>
                            </ul>
                        </li>

                    </ul>


                    <!-- Suchformular rechts -->
                    <form class="d-flex" role="search" action="/members/search" method="get">
                        <input class="form-control me-2" type="search" name="searchTerm"
                               placeholder="Mitglieder" value="${searchTerm}" />
                        <button class="btn btn-success" type="submit">Suchen</button>
                    </form>

                </div>
            </div>
        </nav>

        <!-- Haupt-Content -->
        <div class="container-fluid justify-content-center align-items-center px-5 pb-4">
            ${content}
        </div>

        <!-- Bootstrap JS Bundle -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>


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
