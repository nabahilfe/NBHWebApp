<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import org.springframework.data.domain.Page

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.Role
@import eu.nabahilfe.webapp.members.Member

@param Page<Member> memberPage

@param java.lang.String searchTerm = ""
@param java.lang.String orderBy = ""
@param java.lang.String order = ""

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

  <!-- Hidden form to handle sorting -->
  <form id="sortForm" method="get" action="/members">
      <input type="hidden" name="orderBy" id="orderBy">
      <input type="hidden" name="order" id="order">
  </form>

    <h3>Unsere Vereinsmitglieder</h3>

    <div class="alert alert-light border small mb-3" role="alert">
        <span class="bi bi-geo-alt-fill text-success"></span>Adresse validiert.&nbsp;
        <span class="bi bi-geo-alt-fill text-danger"></span>Adresse ev. nicht korrekt!
    </div>

    <!-- Display all Member records -->
    <table class="table table-bordered table-hover table-sm shadow h-100" id="memberTable">

      <thead class="table-light">
        <tr>
          <th scope="col" data-col="lastName" data-order="${orderBy.equals("lastName") ? order : ""}">
                Name ${orderBy.equals("lastName") ? (order.equals("asc") ? "↑" : "↓") : "↕"}</th>

          @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
              <th scope="col" data-col="isGeoValidated" data-order="${orderBy.equals("isGeoValidated") ? order : ""}">
                    Adresse ${orderBy.equals("isGeoValidated") ? (order.equals("asc") ? "↑" : "↓") : "↕"}</th>
          @else
              <th scope="col">Adresse</th>
          @endif
          @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
              <th scope="col" data-col="role.roleName" data-order="${orderBy.equals("role.roleName") ? order : ""}">
                    Rolle ${orderBy.equals("role.roleName") ? (order.equals("asc") ? "↑" : "↓") : "↕"}</th>
          @endif
          <th scope="col" data-col="email" data-order="${orderBy.equals("email") ? order : ""}">
                E-Mail ${orderBy.equals("email") ? (order.equals("asc") ? "↑" : "↓") : "↕"}</th>
          <th scope="col">Telefon</th>

          @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
              <th scope="col" class="text-end" data-col="accumulatedHours" data-order="${orderBy.equals("accumulatedHours") ? order : ""}">
                    Guthaben ${orderBy.equals("accumulatedHours") ? (order.equals("asc") ? "↑" : "↓") : "↕"}</th>
              <th scope="col" class="text-center" title="Zeitschecks kaufen">Zeitscheck</th>
              <th scope="col" class="text-center" title="Zeitschecks herunterladen">Download</th>
          @endif
        </tr>
      </thead>

      <tbody>
        @for (Member member : memberPage.getContent())
            <tr>
                  @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
                      <td><a class="text-decoration-none" href="/members/${member.getId()}">${member.getLastName()} ${member.getFirstName()}</a></td>
                  @else
                    <td>${member.getLastName()} ${member.getFirstName()}</td>
                  @endif

                  <td>
                      @if(member.isSystemMember())
                          <span class="bi bi-geo-alt-fill" style="visibility:hidden;"></span>
                          ${member.getAddress()}
                      @elseif(member.getLatitude() != null && member.getLongitude() != null)
                        <span class="bi bi-geo-alt-fill text-success" title="Adresse validiert"></span>
                        <a href="#"
                           onclick="openMap(${member.getLatitude()}, ${member.getLongitude()}, encodeURIComponent('${member.getAddress()}')); return false;"
                           class="text-decoration-none"
                           title="Navigation starten">
                           ${member.getAddress()}
                        </a>
                      @else
                          <span class="bi bi-geo-alt-fill text-danger" title="Adresse nicht validiert"></span>
                          <a href="https://www.google.com/maps/search/?api=1&query=${member.getAddress().replace(" ", "+")}" target="_blank"
                                   rel="noopener noreferrer" class="text-decoration-none" title="In Google Maps öffnen"> ${member.getAddress()}
                          </a>
                      @endif
                  </td>

                  @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
                      <td>${member.getRole() != null ? member.getRole().getRoleName() : ""}</td>
                  @endif

                  <td>
                      @if(member.getEmail() != null && !member.getEmail().isBlank())
                          <a href="mailto:${member.getEmail()}" class="text-decoration-none">${member.getEmail()}</a>
                      @endif
                  </td>

                  <td>
                      @if(member.getPhoneNumber() != null && !member.getPhoneNumber().isBlank())
                          <a href="tel:${member.getPhoneNumber().replace(" ", "")}" class="text-decoration-none"> ${member.getPhoneNumber()}
                          </a>
                      @endif
                  </td>

                  @if(ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
                      <td class="text-end">
                        ${member.getAccumulatedHours() != null ? member.getAccumulatedHours() + " h" : ""}
                      </td>

                      @if(member.isSystemMember())
                          <!-- do not show buy, download buttons for system members -->
                          <td class="text-center"></td>
                          <td class="text-center"></td>
                      @else
                          <!-- show action buttons only for inactive members -->
                          <td class="text-center">
                            <a class="btn btn-outline-primary btn-sm bi-plus-lg" href="/timecheques/new?memberId=${member.getId()}" role="button" title="Zeitschecks kaufen"></a>
                          </td>

                          <td>
                            <form action="/download/timecheque-pdf" method="get">
                                <input type="hidden" name="memberId" value="${member.getId()}">
                                <div class="text-center">
                                    <button type="submit" class="btn btn-outline-primary btn-sm bi-download" title="Zeitschecks herunterladen"></button>
                                </div>
                            </form>
                          </td>
                      @endif

                  @endif

            </tr>
        @endfor
      </tbody>

    </table>


    @if (ctxt != null && ctxt.hasAnyRole("ADMIN", "EXECUTIVE_MEMBER"))
        <!-- add button to creeate new member -->
        <form action="/members/new" method="get">
            <button type="submit" class="btn btn-outline-primary bi-plus-circle"> Mitglied hinzufügen</button>
        </form>
        <br/>
    @endif

    <!-- Pagination controls -->
    @if(memberPage.getTotalPages() > 0)
        <nav aria-label="Page navigation example">
          <ul class="pagination">
                @for(int i = 0; i < memberPage.getTotalPages(); ++i)
                  <li class="page-item ${i == memberPage.getNumber() ? "active" : ""}">
                    <a class="page-link" href="/members?page=${i}&orderBy=${orderBy}&order=${order}&size=${NbhConst.PAGINATION_PAGE_SIZE}">${i+1}</a>
                  </li>
              @endfor
            </ul>
        </nav>
    @endif

    @template.components.display-statusmessage(successMessage, errorMessage)


    <script>

    document.addEventListener("DOMContentLoaded", () => {
        const form = document.getElementById("sortForm");
        const orderByInput = document.getElementById("orderBy");
        const orderInput = document.getElementById("order");

            // Add click listeners to table headers
        document.querySelectorAll("#memberTable th[data-col]").forEach(th => {
          th.style.cursor = "pointer";
          th.addEventListener("click", () => {
              const col = th.dataset.col; // eg "Vorname"

              // Toggle: If currently asc for this column, then desc – otherwise new asc
              const currentOrder = th.dataset.order;
              const newOrder = currentOrder === "asc" ? "desc" : "asc";

              // rest all headers
              document.querySelectorAll("#memberTable th").forEach(h => {
                h.dataset.order = "";
                h.textContent = h.textContent.replace(/[↕↑↓]/g, ''); // Pfeile entfernen
              });

              // Set new order and visual feedback
              th.dataset.order = newOrder;
              th.textContent += newOrder === "asc" ? " ↑" : " ↓"; // Visuelles Feedback

              // Set hidden form values to sort order
              orderByInput.value = col;
              orderInput.value = newOrder;

              form.submit(); // Seite neu laden mit sortierten Daten
          });
        });
    });


    function useAppleMaps() {
        const ua = navigator.userAgent;

        return /iPhone|iPad|Macintosh/.test(ua) &&
               ua.includes("Safari") &&
               !ua.includes("Chrome") &&
               !ua.includes("CriOS") &&
               !ua.includes("Edg") &&
               !ua.includes("OPR") &&
               !ua.includes("Firefox") &&
               !ua.includes("FxiOS");
    };


    function openMap(lat, lon, address) {
        let url;

        //console.log("Address encoded:", address);

        if (useAppleMaps()) {
            url = "https://maps.apple.com/?ll=" + lat + "," + lon + "&q=" + address;
        }
        else {
            url = "https://www.google.com/maps?q=" + lat + "," + lon;
        }

        //console.log("URL:", url);

        window.open(url, "_blank", "noopener,noreferrer");
    };

    </script>


`)




