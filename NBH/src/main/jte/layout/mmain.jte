@param gg.jte.Content content

@param String searchTerm = null

<!doctype html>
<html lang="en" data-bs-theme="auto">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>NBH</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.13.1/font/bootstrap-icons.min.css">

    <script src="/assets/js/color-modes.js"></script>
    <!--  FIXME: brauchen wir das?
    <link href="../assets/dist/css/bootstrap.min.css" rel="stylesheet" />
    -->
    <meta name="theme-color" content="#712cf9" />
    <!-- link href="navbar-fixed.css" rel="stylesheet" /-->

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js">
    </script>


<script>
  // Force Bootstrap to rebind dropdowns in the navbar
  document.addEventListener('DOMContentLoaded', function () {
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'))
    dropdownElementList.map(function (dropdownToggleEl) {
      return new bootstrap.Dropdown(dropdownToggleEl)
    })
  });
</script>

  </head>




  <body>


<!--
<nav class="navbar navbar-expand-lg bg-body-tertiary">

<nav class="navbar navbar-expand-lg" style="background-color: #e3f2fd;" data-bs-theme="light">

<nav class="navbar navbar-expand-lg bg-dark border-bottom border-body" data-bs-theme="dark">

<nav class="navbar navbar-expand-lg border-bottom border-body" style="background-color: #5C5551;" data-bs-theme="dark">

-->
<nav class="navbar navbar-expand-lg" style="background-color: #77DD77;">


  <div class="container-fluid">

    <a class="navbar-brand" href="#">NBH Maria Anzbach</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>


    <div class="collapse navbar-collapse" id="navbarSupportedContent">

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




<li class="nav-item dropdown">
  <button
    class="nav-link dropdown-toggle btn btn-link"
    type="button"
    data-bs-toggle="dropdown"
    data-bs-display="static"
    aria-expanded="false">
    Actions
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

          <form class="d-flex" role="search" action="/members/search" method="get">
            <input class="form-control me-2" type="search" name="searchTerm" placeholder="Mitglieder" value="${searchTerm}"/>
            <button class="btn btn-success" type="submit">Suchen</button>
          </form>

    </div>
  </div>
</nav>



    <!-- This is the place for the Content -->

    <main class="container mt-4">
      <div class="bg-body-tertiary p-5 rounded">

        <!-- Content from jte -->
         ${content}

      </div>
    </main>




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
