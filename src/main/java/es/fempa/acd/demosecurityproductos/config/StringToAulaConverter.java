package es.fempa.acd.demosecurityproductos.config;

import es.fempa.acd.demosecurityproductos.model.Aula;
import es.fempa.acd.demosecurityproductos.repository.AulaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAulaConverter implements Converter<String, Aula> {

    private final AulaRepository aulaRepository;

    public StringToAulaConverter(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @Override
    public Aula convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            Long aulaId = Long.valueOf(id);
            return aulaRepository.findById(aulaId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
