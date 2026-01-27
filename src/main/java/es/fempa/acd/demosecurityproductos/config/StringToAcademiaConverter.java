package es.fempa.acd.demosecurityproductos.config;

import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.repository.AcademiaRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAcademiaConverter implements Converter<String, Academia> {

    private final AcademiaRepository academiaRepository;

    public StringToAcademiaConverter(AcademiaRepository academiaRepository) {
        this.academiaRepository = academiaRepository;
    }

    @Override
    public Academia convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            Long academiaId = Long.valueOf(id);
            return academiaRepository.findById(academiaId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
