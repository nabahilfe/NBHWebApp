Unten ist eine **Feature Map (Epics → Stories)** sowie **User Journeys** für die NBH Web App, **direkt aus den vorhandenen Issues** im Repo `nabahilfe/NBHWebApp` (Stand 2026-04-10) abgeleitet.

---

## Feature Map (Epics → Stories)

### Epic A — Mitgliederverwaltung (Stammdaten, Status, Rollen)
**Ziel:** Mitglieder erfassen/anzeigen/bearbeiten inkl. Status, Rollen und vereinsrelevanter Attribute.

**Stories (aus Issues):**
- Mitgliederliste: Anzeige „Nachname, Vorname“ (#4, closed)
- Mitgliedsmaske: Beitritts- und Austrittsdatum anzeigen/erfassen (#48, open)
- Mitglied bearbeiten: Fehlende Felder ergänzen (unklar welche) (#52, open)
- Stammdaten: Telefonnummer hinzufügen (Titel unvollständig) (#51, open)
- Mitgliedsnummern: Regeln/Anpassungen (#41, open)
- Mitgliederstatus: Ruhend stellen und reaktivieren (#18, open)
- Rollen: Rollenänderung am Mitglied speichern/anzeigen (Bugfix) (#17, closed)
- Vereinskontext: Anrede/Kennzeichnung (Mitglied, Titel, Institution) (#11, closed)
- Vorstand: Alle Vorstandsmitglieder anzeigen (#25, closed)

---

### Epic B — Zeitscheck-/Zeitkonto-System (Erstellen, Kaufen, Übergabe, Verbuchen)
**Ziel:** Zeitguthaben (Stunden) als Zeitschecks verwalten, inkl. Regeln und sicherer Buchungslogik.

**Stories:**
- Zeitscheck erstellen/anlegen (Stunden) (#9, closed)
- Validierungen beim Erstellen von Zeitschecks (#3, closed)
- Nach Anlage: bestimmte Felder read-only setzen (#14, closed)
- Zeitscheck-Übergabe: Beschreibungsfeld erfassen (#42, closed)
- Zeitscheck Zuweisung korrigieren (#1, open)
- Zeitbuchung/Verbuchung (Transfer von → an) (#9 closed; UI-Probleme #15/#21 closed)
- Kauf von 10 oder 20 Zeitschecks (Pakete) (#36, open)
- Selbst erstellen/kaufen nur bei vorhandenem Einziehungsauftrag (#33, open)

---

### Epic C — Sozialkonto (Sonderregeln & Kategorien)
**Ziel:** Spezielle Konten/Fälle („SOZIALKONTO“) mit abweichenden Regeln abbilden.

**Stories:**
- Sozialkonto umsetzen (#10, closed)
- Sozialkonto mit Kategorie verknüpfen (#46, closed)
- Für Sozialkonto automatisch Geburtsdatum eintragen (#49, open)
- Für Sozialkonto Kauf von Stunden verhindern (#50, open)

---

### Epic D — Benachrichtigungen / E-Mail
**Ziel:** Automatisierte Kommunikation bei relevanten Zeitscheck-Ereignissen.

**Stories:**
- Email Service implementieren (#7, closed)
- Email Tests hinzufügen (#20, closed)
- Batch Job für E-Mail Versand (#8, open)
- Mails versenden bei Zeitscheck Aktionen (Trigger definieren & umsetzen) (#47, open)

---

### Epic E — Reporting & Statistiken
**Ziel:** Transparenz über Zeit-/Buchungsdaten und Vereinsentwicklung.

**Stories:**
- Auswertungen für alle Zeit- und Buchungs-Daten (Sammel-Thema) (#37, open)
- Auswertung der Buchungen nach Jahr + Ein-/Ausgaben (#29, open)
- Statistiken: erworbene, vergebene, gekaufte Zeitschecks (#38, open)
- Auswertung Einnahmen und Ausgaben (#39, open)
- Entwicklung der Mitgliederzahlen (#40, open)

---

### Epic F — Rollen, Rechte, Security & Administration
**Ziel:** Sicherer Betrieb; saubere Rollen/Default-Setup; Admin-Beschränkungen.

**Stories:**
- Standard Admin beim Start anlegen (#24, closed)
- Standard-Rollen beim Startup anlegen, falls nicht vorhanden (#31, open)
- System Administrator: keine Zeitschecks bekommen/vergiben (#32, open)
- System Administrator: weitere Einschränkungen definieren/umsetzen (#35, open)
- Controller Services / URLs absichern (#27, open)
- Self-timetransfer: Security Hole schließen (#28, open)
- Exception Handling einbauen (#19, open)

---

### Epic G — UX, Navigation & Stabilität
**Ziel:** Intuitive Bedienung, stabiler UI-Flow, weniger Fehler durch Navigation/Refresh.

**Stories:**
- Menü besser strukturieren (#16, open)
- Aktionen-Menü nach „Zeitbuchung/Zeitscheck verbuchen“ funktioniert nicht mehr (Fix) (#15, closed; #21, closed)
- Summary-Timecheque Seite: refresh-sicher machen (#2, closed)
- Form Layout: 2-spaltig umbauen (#13, closed)

---

### Epic H — Performance / Datenzugriff (Technik, aber user-relevant)
**Ziel:** Performanter Betrieb, v. a. bei Listen/Reports.

**Stories:**
- Hibernate 1+N Probleme finden und lösen (#26, open)
- Bundles überprüfen/validieren (Build/Dependencies) (#5, closed)
- Modell anpassen (#12, closed)

---

### Epic I — (Zukunft) Multi-Tenant / Organisation verwalten
**Ziel:** Langfristig mehrere Organisationen verwaltbar (noch nicht akut).

**Stories:**
- Funktionen zur Organisationsverwaltung (erst bei Multi Tenant relevant) (#30, open)

---

## User Journeys (End-to-End Abläufe)

### Journey 1 — Admin/Office: Neues Mitglied anlegen & korrekt klassifizieren
1. Admin öffnet Mitgliederverwaltung.
2. Admin erfasst Stammdaten (Name, ggf. Anrede/Titel/Institution) (#11).
3. Admin ergänzt Telefonnummer (#51) sowie Beitrittsdatum (und später Austrittsdatum) (#48).
4. System vergibt/validiert Mitgliedsnummern nach Regeln (#41).
5. Admin weist Rollen zu; Änderungen müssen sofort sichtbar/gespeichert sein (#17).
6. Optional: Mitglied wird als Vorstand geführt / Vorstandsliste zeigt alle Vorst��nde (#25).

**Akzeptanzkriterien (abgeleitet):**
- Felder in Maske vollständig (inkl. fehlender Felder aus #52).
- Rollenänderung persistiert und wird korrekt angezeigt (#17).

---

### Journey 2 — Admin: Mitglied ruhend stellen und später reaktivieren
1. Admin sucht Mitglied.
2. Admin setzt Status auf „ruhend“ (#18).
3. System verhindert/steuert je nach Regeln Folgeaktionen (offen: welche Auswirkungen auf Zeitschecks?).
4. Admin reaktiviert Mitglied später (#18).
5. Reporting kann Entwicklung der Mitgliederzahlen abbilden (#40).

**Offen (Klärung):**
- Dürfen ruhende Mitglieder Zeitschecks erhalten/kaufen/verbuchen?

---

### Journey 3 — Mitglied/Benutzer: Zeitschecks kaufen (Pakete) – nur mit Einziehungsauftrag
1. Benutzer wählt „Zeitschecks kaufen“.
2. System prüft, ob Einziehungsauftrag vorhanden ist (#33).
3. Wenn vorhanden: Benutzer kann Paket (10/20) wählen (#36).
4. System erstellt die Zeitschecks/Buchung, setzt Status korrekt, und triggert Benachrichtigung (#47/#8).
5. Wenn *nicht* vorhanden: Kauf/selbst erstellen ist nicht möglich (#33).

**Abzweig Sozialkonto:**
- Wenn Konto/Benutzer „SOZIALKONTO“ ist: Kauf von Stunden verhindern (#50).

---

### Journey 4 — Admin: Zeitscheck erstellen, dann Übergabe mit Beschreibung
1. Admin erstellt Zeitscheck (Stunden) (#9).
2. Beim Erstellen greifen Validierungen (#3).
3. Nach Anlage sind definierte Felder read-only (#14).
4. Admin übergibt/vergibt Zeitscheck an ein anderes Mitglied:
   - Beschreibung zur Übergabe wird erfasst (#42).
5. System verschickt relevante E-Mails (#47) ggf. per Batch (#8).

---

### Journey 5 — Admin/Office: Zeitscheck verbuchen (Transfer „von → an“) ohne UI-Bruch
1. Admin startet „Zeitbuchung / Zeitscheck verbuchen“.
2. Admin wählt „von Mitglied“ und „an Mitglied“, erfasst Stunden/Referenz (#9).
3. Buchung wird gespeichert; UI bleibt bedienbar:
   - Aktionen-Menü funktioniert weiterhin (#15/#21).
4. Optional: danach Wechsel in Summary/Reporting ohne Refresh-Probleme (#2).

---

### Journey 6 — Security: Self-Time-Transfer sicher machen
1. Benutzer versucht einen „self-timetransfer“ (impliziter Use Case).
2. System verhindert Missbrauch/Sicherheitslücke (#28):
   - z. B. keine Transfers an sich selbst, keine Umgehung von Rollen/Rechten, keine Manipulation von Beträgen (konkret offen).
3. Alle Controller/URLs sind serverseitig abgesichert (#27).

---

### Journey 7 — Vorstand/Kassa: Auswertungen & Statistiken abrufen
1. Nutzer mit Reporting-Rechten öffnet Auswertungen (#37).
2. Filter nach Jahr/Zeitraum, Kategorie etc. (#29).
3. Dashboard/Ansichten:
   - erworben/vergeben/gekauft (#38)
   - Einnahmen/Ausgaben (#39)
   - Entwicklung Mitgliederzahlen (#40)
4. Daten werden performant geladen (kein 1+N) (#26).

---

## Nächster sinnvoller Schritt
Wenn du mir sagst, **welche Rollen es tatsächlich gibt** (oder ob ich sie aus dem Code/DB-Schema ableiten soll), kann ich die Journeys noch in **konkrete Screens + Rechte-Matrix (Role → Action → Allowed/Denied)** überführen.
