# Einstieg JIRA
- Backlog / Sprints zeigen
- Sprint Backlog zeigen
- Story zeigen, assignen, in implementation ziehen
  - DoD

# IntelliJ starten
- MathController implementieren
```java
@RestController
public class MathController {
    private final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @PostMapping(
            path = "/math/area_computation/hyperrectangle",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> calculateRectangleArea(@RequestBody String input) {
        Document document;
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            document = documentBuilder.parse(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<error>Could not parse input</error>");
        }

        NodeList sideElements = document.getElementsByTagName("side");
        int numberOfSides = sideElements.getLength();

        if (numberOfSides < 2) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("<error>Needs at least two sides</error>");
        }

        Long area = null;
        for (int i = 0; i < numberOfSides; i++) {
            Element sideElement = (Element) sideElements.item(i);
            Long length = Long.valueOf(sideElement.getAttribute("length"));
            area = area == null ? length : area * length;
        }
        return ResponseEntity.ok("<calculation><area>"+area+"</area></calculation>");
    }
}
```

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MathControllerTest {

  @Autowired
  private MockMvc mvc;

  @Test
  void calculateRectangleArea_twoDimensional() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/math/area_computation/hyperrectangle")
                    .content("<calculation><side length=\"4\"/><side length=\"8\"/></calculation>")
                    .contentType(MediaType.APPLICATION_XML)
            )
            .andExpect(status().isOk())
            .andExpect(content().xml("<calculation><area>32</area></calculation>"));
  }

  @Test
  void calculateRectangleArea_threeDimensional() throws Exception {
    mvc.perform(MockMvcRequestBuilders.post("/math/area_computation/hyperrectangle")
                    .content("<calculation><side length=\"4\"/><side length=\"8\"/><side length=\"2\"/></calculation>")
                    .contentType(MediaType.APPLICATION_XML)
            )
            .andExpect(status().isOk())
            .andExpect(content().xml("<calculation><area>64</area></calculation>"));
  }
}
```
- mit curl testen lokal
```shell
curl --header 'Content-Type: application/xml' -d '<calculation><side length="4"/><side length="8"/></calculation>' localhost:8080/math/area_computation/hyperrectangle && echo
```
- Story in Review setzen
- PR erstellen
  - CI-Lauf abwarten
  - Reviewer zuweisen
    - Lob für Tests
    - fehlender Testfall
    - XML-Parsing mit Framework lösen
    - endpoint-path falsch benannt
    - naming methoden
    - xml resultat struktur
  - CI-Resultate anschauen
    - Tests
    - statische Analyse CodeQL
    - statische Analyse SonarQube

# Review-Findings umsetzen
```java
package ch.ipt.see.playground;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MathController {

    @PostMapping(
            path = "/math/volume-computation/hyperrectangle",
            consumes = MediaType.APPLICATION_XML_VALUE,
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<CalculationResult> calculateHyperrectangleVolume(@RequestBody Calculation calculation) {
        if (calculation.getSides().size() < 2) {
            CalculationResult result = new CalculationResult(null, "Hyperrectangle must have at least two sides");
            return ResponseEntity.badRequest().body(result);
        }

        Long volume = null;
        for (Calculation.Side side : calculation.getSides()) {
            volume = volume == null ? side.getLength() : volume*side.getLength();
        }

        return ResponseEntity.ok(new CalculationResult(volume, null));
    }
}
```
```xml
		<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
		</dependency>
```
```java
@JacksonXmlRootElement(localName = "calculation")
public class Calculation {
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Side> side;

    private Calculation() {}

    public List<Side> getSides() {
        return this.side;
    }

    public static class Side {
        @JacksonXmlProperty(isAttribute = true, localName = "length")
        private long length;

        private Side() {}

        public long getLength() {
            return length;
        }
    }
}
```
```java
@JacksonXmlRootElement(localName = "calculation")
public record CalculationResult(@JsonInclude(JsonInclude.Include.NON_NULL) Long result,
                                @JsonInclude(JsonInclude.Include.NON_NULL) String error) {
}
```

```java
@SpringBootTest
@AutoConfigureMockMvc
class MathControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void calculateHyperrectangleVolume_twoDimensional() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/math/volume-computation/hyperrectangle")
                        .content("<calculation><side length=\"4\"/><side length=\"8\"/></calculation>")
                        .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(content().xml("<calculation><result>32</result></calculation>"));
    }

    @Test
    void calculateHyperrectangleVolume_threeDimensional() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/math/volume-computation/hyperrectangle")
                        .content("<calculation><side length=\"4\"/><side length=\"8\"/><side length=\"2\"/></calculation>")
                        .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(content().xml("<calculation><result>64</result></calculation>"));
    }

    @Test
    void calculateHyperrectangleVolume_oneDimensional_returnsError() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/math/volume-computation/hyperrectangle")
                        .content("<calculation><side length=\"4\"/></calculation>")
                        .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isBadRequest());
    }
}
```

# erneutes Review
- alle Tests laufen lassen lokal
- manuell testen
```shell
curl --header 'Content-Type: application/xml' -d '<calculation><side length="4"/><side length="8"/></calculation>' localhost:8080/math/volume-computation/hyperrectangle && echo
```
- pushen
- CI-Lauf abwarten, erneutes Review anfordern
- Review OK, praise nicht vergessen

# JIRA
- DoD prüfen
- Story auf abgeschlossen setzen

# weiteres
- Dokumentation nachführen, Release Notes ergänzen
- Spezifikation ergänzen
- Testing durch API-Consumer
- deployen & live testen
