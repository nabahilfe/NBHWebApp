@param gg.jte.Content content

@param String searchTerm = null

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

  </head>


  <body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-light" style="background-color: #77DD77;">
      <div class="container-fluid">
        <a class="navbar-brand" href="/">NBH Maria Anzbach</a>


        <!-- Navbar Collapse -->
        <div class="collapse navbar-collapse" id="navbarSupportedContent">

          <!-- Links links -->
          <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="/members">Mitglieder</a>
            </li>
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="/roles">Rollen</a>
            </li>
            <li class="nav-item">
              <a class="nav-link active" aria-current="page" href="/timetransfers/new">Zeitbuchung</a>
            </li>

            <!-- Dropdown -->
            <li class="nav-item dropdown">
              <button class="nav-link dropdown-toggle btn btn-link"
                      type="button"
                      data-bs-toggle="dropdown"
                      data-bs-display="static"
                      aria-expanded="false">
                Aktionen
              </button>
              <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="/roles">Action</a></li>
                <li><a class="dropdown-item" href="#">Another action</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="#">Something else here</a></li>
              </ul>
            </li>

            <li class="nav-item">
              <a class="nav-link disabled" aria-disabled="true">Disabled</a>
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
    <main class="container mt-4">
      <div class="bg-body-tertiary p-5 rounded">
        ${content}
      </div>
    </main>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>


<!--
    < ! -- Stabile Dropdown-Initialisierung -- >
    <script>
      function initDropdowns() {
        const dropdowns = document.querySelectorAll('.dropdown-toggle');
        dropdowns.forEach(btn => {
          // Alte Instanz entfernen, falls vorhanden
          const oldInstance = bootstrap.Dropdown.getInstance(btn);
          if (oldInstance) oldInstance.dispose();

          // Neue Instanz mit 'static' Display
          const dd = new bootstrap.Dropdown(btn, { display: 'static' });
          btn._dropdown = dd; // Referenz speichern, falls später dispose benötigt
        });
      }

      // Initialisierung beim Laden
      document.addEventListener('DOMContentLoaded', initDropdowns);

      // Beispiel: Falls Content dynamisch ersetzt wird, Dropdowns erneut binden
      function updateMainContent(html) {
        const main = document.querySelector('main .container');
        main.innerHTML = html;
        initDropdowns();
      }
    </script>
-->

    <!-- Optional: Auto-dismiss alerts -->
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


