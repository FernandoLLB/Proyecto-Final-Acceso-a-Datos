package es.fempa.acd.demosecurityproductos.config;

import es.fempa.acd.demosecurityproductos.model.Profesor;
import es.fempa.acd.demosecurityproductos.repository.ProfesorRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProfesorConverter implements Converter<String, Profesor> {

    private final ProfesorRepository profesorRepository;

    public StringToProfesorConverter(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Override
    public Profesor convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        try {
            Long profesorId = Long.valueOf(id);
            return profesorRepository.findById(profesorId).orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
