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
