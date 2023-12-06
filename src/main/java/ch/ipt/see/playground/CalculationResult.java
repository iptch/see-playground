package ch.ipt.see.playground;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "calculation")
public record CalculationResult(@JsonInclude(JsonInclude.Include.NON_NULL) Long result,
                                @JsonInclude(JsonInclude.Include.NON_NULL) String error) {
}