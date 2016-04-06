package cz.cvut.kbss.inbas.reporting.rest.dto.model;

import cz.cvut.kbss.inbas.reporting.dto.OccurrenceReportDto;

import java.util.ArrayList;

/**
 * DTO for OccurrenceReport list serialization.
 * <p>
 * This is because Jackson is not able to write/determine polymorphic types in collection.
 * <p>
 * See <a href="https://github.com/FasterXML/jackson-databind/issues/336">https://github.com/FasterXML/jackson-databind/issues/336
 * </a> for more details.
 */
public class OccurrenceReportDtoList extends ArrayList<OccurrenceReportDto> {

    public OccurrenceReportDtoList() {
    }

    public OccurrenceReportDtoList(int initialCapacity) {
        super(initialCapacity);
    }
}
