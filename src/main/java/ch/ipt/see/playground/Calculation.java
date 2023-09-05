package ch.ipt.see.playground;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

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