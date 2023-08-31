# Vorbereitung
- Git Repository auschecken, in IntelliJ öffnen
  - Notizen auschecken (Branch `notes`, Folder `notes`): https://github.com/iptch/see-playground/tree/notes/notes
- Run Configs 
  - `PlaygroundApplication` 
  - und `All in playground` laufen lassen, um zu prüfen, dass alles funktioniert
- Rolle "Developer" vorbereiten
  - github.com Repo in Browser öffnen: https://github.com/iptch/see-playground/
  - sonarcloud.com in Browser öffnen: https://sonarcloud.io/projects
  - JIRA öffnen: https://ipt-training.atlassian.net/jira/software/projects/SEE/boards/1/backlog
    - Sprint Startdaten anpassen
- Rolle "Reviewer" (Gill Bates) vorbereiten (Incognito/private Browser)
  - github.com Repo in Browser öffnen, *einloggen*: https://github.com/iptch/see-playground/
  - JIRA öffnen: https://ipt-training.atlassian.net/jira/software/projects/SEE/boards/1

# Einstieg JIRA
- Sprints zeigen (aktueller, zukünftige)
  - Stories, Schätzungen
- Sprint Backlog zeigen
- Story zeigen, assignen, in implementation ziehen
  - DoD

# IntelliJ starten
- Prüfen/zeigen, dass Workspace funktionsfähig ist:
  - Applikation starten (Run Config `PlaygroundApplication`)
    - http://localhost:8080/hello?name=test öffnen
  - Tests ausführen (Run Config `All in playground`)
- Ctrl+Shift+A - Apply patch: `notes/SEE-3__00__Implement_hyperrectangle_volume_computation.patch`
- Lokal tests ausführen: Run Config `MathControllerTest`
- mit curl testen lokal
```shell
curl --header 'Content-Type: application/xml' -d '<calculation><side length="4"/><side length="8"/></calculation>' localhost:8080/math/area_computation/hyperrectangle && echo
```
- Story in Review setzen
- PR erstellen
  - CI-Lauf abwarten
    - kurzer Refresher warum CI, was gehört alles dazu, was kann man damit machen?
    - CI-Resultate anschauen (ggf. nach Review machen, falls CI-Lauf zu lange dauert)
      - Tests
      - statische Analyse CodeQL
      - statische Analyse SonarQube
        - kurz zeigen, was Sonar bietet (Coverage, Code Smells, etc.)
  - Reviewer zuweisen
  - In der Rolle des "Reviewers" (Reviewer-Browserfenster öffnen)
    - siehe [code-review.txt](code-review.txt)
      - Lob für Tests
      - fehlender Testfall
      - XML-Parsing mit Framework lösen
      - endpoint-path falsch benannt
      - naming methoden
      - xml resultat struktur

# Review-Findings umsetzen
- Story wieder in Bearbeitung ziehen, mir selbst zuweisen
- Ctrl+Shift+A - Apply patch: `notes/SEE-3__01__implement_code_review_findings.patch`

# erneutes Testing, Review
- alle Tests laufen lassen lokal
- manuell testen
```shell
curl --header 'Content-Type: application/xml' -d '<calculation><side length="4"/><side length="8"/></calculation>' localhost:8080/math/volume-computation/hyperrectangle && echo
```
- pushen
- CI-Lauf abwarten, erneutes Review anfordern
- In der Rolle des "Reviewers":
  - Prüfen, ob Review-Findings adressiert wurden
  - PR approven mit Praise

# JIRA - Story abschliessen
- DoD prüfen
- Story auf abgeschlossen setzen

# weiteres, was ggf. zur Story gehört
- Dokumentation nachführen, Release Notes ergänzen
- Spezifikation ergänzen
- Testing durch API-Consumer
- deployen & live testen

# Cleanup
- Branch lokal und auf GitHub löschen (PR wird geclosed)
- JIRA Story unassignen, auf `TO DO` schieben