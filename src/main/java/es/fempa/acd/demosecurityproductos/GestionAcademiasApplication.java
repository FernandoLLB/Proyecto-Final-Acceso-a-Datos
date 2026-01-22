package es.fempa.acd.demosecurityproductos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionAcademiasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionAcademiasApplication.class, args);
		System.out.println("=== ✅ Aplicación iniciada - Usando datos de MySQL ===");
	}

	// DATOS DE PRUEBA COMENTADOS - La aplicación ahora usa únicament los datos de tu base de datos MySQL
	/*
	@Bean
	public CommandLineRunner initData(
			AcademiaRepository academiaRepository,
			UsuarioRepository usuarioRepository,
			ProfesorRepository profesorRepository,
			AlumnoRepository alumnoRepository) {

		return (args) -> {
			PasswordEncoder encoder = new BCryptPasswordEncoder();
			String password = encoder.encode("password123");

			// Limpiar datos existentes para evitar duplicados
			System.out.println("=== Inicializando datos de prueba del prototipo ===");

			// 1. CREAR/ACTUALIZAR USUARIO ADMIN (sin academia)
			if (usuarioRepository.findByUsername("admin").isEmpty()) {
				Usuario admin = new Usuario();
				admin.setUsername("admin");
				admin.setEmail("admin@plataforma.com");
				admin.setPassword(password);
				admin.setNombre("Administrador");
				admin.setApellidos("del Sistema");
				admin.setRol(Rol.ADMIN);
				admin.setActivo(true);
				admin.setAcademia(null); // Admin no pertenece a ninguna academia
				usuarioRepository.save(admin);
				System.out.println("✅ Usuario ADMIN creado");
			} else {
				// Actualizar contraseña del admin existente
				Usuario admin = usuarioRepository.findByUsername("admin").get();
				admin.setPassword(password);
				admin.setEmail("admin@plataforma.com");
				admin.setNombre("Administrador");
				admin.setApellidos("del Sistema");
				admin.setRol(Rol.ADMIN);
				admin.setActivo(true);
				admin.setAcademia(null);
				usuarioRepository.save(admin);
				System.out.println("🔄 Usuario ADMIN actualizado con contraseña password123");
			}

			// 2. CREAR ACADEMIA TECHLEARN
			Academia techLearn = academiaRepository.findByNombre("TechLearn Academy").orElse(null);
			if (techLearn == null) {
				techLearn = new Academia();
				techLearn.setNombre("TechLearn Academy");
				techLearn.setNifCif("B12345678");
				techLearn.setEmailContacto("contacto@techlearn.com");
				techLearn.setTelefono("912345678");
				techLearn.setDireccion("Calle Tecnología 123, Madrid");
				techLearn.setActiva(true);
				techLearn = academiaRepository.save(techLearn);
				System.out.println("✅ Academia TechLearn creada");
			}

			// 3. CREAR ACADEMIA INNOVAEDU
			Academia innovaEdu = academiaRepository.findByNombre("InnovaEdu").orElse(null);
			if (innovaEdu == null) {
				innovaEdu = new Academia();
				innovaEdu.setNombre("InnovaEdu");
				innovaEdu.setNifCif("B87654321");
				innovaEdu.setEmailContacto("info@innovaedu.com");
				innovaEdu.setTelefono("913456789");
				innovaEdu.setDireccion("Avenida Innovación 456, Barcelona");
				innovaEdu.setActiva(true);
				innovaEdu = academiaRepository.save(innovaEdu);
				System.out.println("✅ Academia InnovaEdu creada");
			}

			// 4. CREAR USUARIOS Y RELACIONES PARA TECHLEARN
			createUserIfNotExists(usuarioRepository, "propietario1", password, "propietario1@techlearn.com",
				"Carlos", "García", Rol.PROPIETARIO, techLearn);

			createUserIfNotExists(usuarioRepository, "secretaria1", password, "secretaria1@techlearn.com",
				"María", "López", Rol.SECRETARIA, techLearn);

			// Profesores TechLearn
			Usuario prof1User = createUserIfNotExists(usuarioRepository, "profesor1", password, "profesor1@techlearn.com",
				"Juan", "Martínez", Rol.PROFESOR, techLearn);
			if (prof1User != null && profesorRepository.findByUsuario(prof1User).isEmpty()) {
				Profesor prof1 = new Profesor();
				prof1.setUsuario(prof1User);
				prof1.setAcademia(techLearn);
				prof1.setEspecialidad("Programación Web");
				prof1.setBiografia("Experto en desarrollo web con más de 10 años de experiencia");
				prof1.setFechaContratacion(LocalDate.of(2020, 1, 15));
				profesorRepository.save(prof1);
			}

			Usuario prof2User = createUserIfNotExists(usuarioRepository, "profesor2", password, "profesor2@techlearn.com",
				"Ana", "Fernández", Rol.PROFESOR, techLearn);
			if (prof2User != null && profesorRepository.findByUsuario(prof2User).isEmpty()) {
				Profesor prof2 = new Profesor();
				prof2.setUsuario(prof2User);
				prof2.setAcademia(techLearn);
				prof2.setEspecialidad("Bases de Datos");
				prof2.setBiografia("Especialista en SQL, NoSQL y diseño de bases de datos");
				prof2.setFechaContratacion(LocalDate.of(2021, 3, 10));
				profesorRepository.save(prof2);
			}

			// Alumnos TechLearn
			Usuario alum1User = createUserIfNotExists(usuarioRepository, "alumno1", password, "alumno1@techlearn.com",
				"Pedro", "Sánchez", Rol.ALUMNO, techLearn);
			if (alum1User != null && alumnoRepository.findByUsuario(alum1User).isEmpty()) {
				Alumno alum1 = new Alumno();
				alum1.setUsuario(alum1User);
				alum1.setAcademia(techLearn);
				alum1.setEstadoMatricula("ACTIVO");
				alum1.setObservaciones("Alumno de Desarrollo Web Full Stack");
				alum1.setFechaRegistro(LocalDate.of(2023, 9, 1));
				alumnoRepository.save(alum1);
			}

			Usuario alum2User = createUserIfNotExists(usuarioRepository, "alumno2", password, "alumno2@techlearn.com",
				"Laura", "Rodríguez", Rol.ALUMNO, techLearn);
			if (alum2User != null && alumnoRepository.findByUsuario(alum2User).isEmpty()) {
				Alumno alum2 = new Alumno();
				alum2.setUsuario(alum2User);
				alum2.setAcademia(techLearn);
				alum2.setEstadoMatricula("ACTIVO");
				alum2.setObservaciones("Interesada en Frontend y UX/UI");
				alum2.setFechaRegistro(LocalDate.of(2023, 9, 15));
				alumnoRepository.save(alum2);
			}

			Usuario alum3User = createUserIfNotExists(usuarioRepository, "alumno3", password, "alumno3@techlearn.com",
				"Miguel", "Torres", Rol.ALUMNO, techLearn);
			if (alum3User != null && alumnoRepository.findByUsuario(alum3User).isEmpty()) {
				Alumno alum3 = new Alumno();
				alum3.setUsuario(alum3User);
				alum3.setAcademia(techLearn);
				alum3.setEstadoMatricula("ACTIVO");
				alum3.setObservaciones("Con conocimientos previos en Java");
				alum3.setFechaRegistro(LocalDate.of(2023, 10, 1));
				alumnoRepository.save(alum3);
			}

			// 5. CREAR USUARIOS Y RELACIONES PARA INNOVAEDU
			createUserIfNotExists(usuarioRepository, "propietario2", password, "propietario2@innovaedu.com",
				"Elena", "Moreno", Rol.PROPIETARIO, innovaEdu);

			createUserIfNotExists(usuarioRepository, "secretaria2", password, "secretaria2@innovaedu.com",
				"Carmen", "Jiménez", Rol.SECRETARIA, innovaEdu);

			// Profesores InnovaEdu
			Usuario prof3User = createUserIfNotExists(usuarioRepository, "profesor3", password, "profesor3@innovaedu.com",
				"Roberto", "Díaz", Rol.PROFESOR, innovaEdu);
			if (prof3User != null && profesorRepository.findByUsuario(prof3User).isEmpty()) {
				Profesor prof3 = new Profesor();
				prof3.setUsuario(prof3User);
				prof3.setAcademia(innovaEdu);
				prof3.setEspecialidad("Diseño Gráfico");
				prof3.setBiografia("Diseñador con amplia experiencia en Adobe Creative Suite");
				prof3.setFechaContratacion(LocalDate.of(2019, 6, 1));
				profesorRepository.save(prof3);
			}

			Usuario prof4User = createUserIfNotExists(usuarioRepository, "profesor4", password, "profesor4@innovaedu.com",
				"Sofía", "Ruiz", Rol.PROFESOR, innovaEdu);
			if (prof4User != null && profesorRepository.findByUsuario(prof4User).isEmpty()) {
				Profesor prof4 = new Profesor();
				prof4.setUsuario(prof4User);
				prof4.setAcademia(innovaEdu);
				prof4.setEspecialidad("Marketing Digital");
				prof4.setBiografia("Experta en SEO, SEM y redes sociales");
				prof4.setFechaContratacion(LocalDate.of(2020, 9, 15));
				profesorRepository.save(prof4);
			}

			// Alumnos InnovaEdu
			Usuario alum4User = createUserIfNotExists(usuarioRepository, "alumno4", password, "alumno4@innovaedu.com",
				"David", "Gómez", Rol.ALUMNO, innovaEdu);
			if (alum4User != null && alumnoRepository.findByUsuario(alum4User).isEmpty()) {
				Alumno alum4 = new Alumno();
				alum4.setUsuario(alum4User);
				alum4.setAcademia(innovaEdu);
				alum4.setEstadoMatricula("ACTIVO");
				alum4.setObservaciones("Alumno de Diseño Gráfico");
				alum4.setFechaRegistro(LocalDate.of(2023, 9, 5));
				alumnoRepository.save(alum4);
			}

			Usuario alum5User = createUserIfNotExists(usuarioRepository, "alumno5", password, "alumno5@innovaedu.com",
				"Isabel", "Hernández", Rol.ALUMNO, innovaEdu);
			if (alum5User != null && alumnoRepository.findByUsuario(alum5User).isEmpty()) {
				Alumno alum5 = new Alumno();
				alum5.setUsuario(alum5User);
				alum5.setAcademia(innovaEdu);
				alum5.setEstadoMatricula("ACTIVO");
				alum5.setObservaciones("Alumna de Marketing Digital");
				alum5.setFechaRegistro(LocalDate.of(2023, 9, 20));
				alumnoRepository.save(alum5);
			}

			Usuario alum6User = createUserIfNotExists(usuarioRepository, "alumno6", password, "alumno6@innovaedu.com",
				"Javier", "Muñoz", Rol.ALUMNO, innovaEdu);
			if (alum6User != null && alumnoRepository.findByUsuario(alum6User).isEmpty()) {
				Alumno alum6 = new Alumno();
				alum6.setUsuario(alum6User);
				alum6.setAcademia(innovaEdu);
				alum6.setEstadoMatricula("INACTIVO");
				alum6.setObservaciones("Dado de baja temporalmente por motivos personales");
				alum6.setFechaRegistro(LocalDate.of(2023, 8, 15));
				alumnoRepository.save(alum6);
			}

			System.out.println("=== ✅ Datos de prueba inicializados correctamente ===");
			System.out.println("Contraseña para todos los usuarios: password123");
		};
	}
	*/

	/*
	private Usuario createUserIfNotExists(UsuarioRepository repository, String username, String password,
			String email, String nombre, String apellidos, Rol rol, Academia academia) {
		if (repository.findByUsername(username).isEmpty()) {
			Usuario user = new Usuario();
			user.setUsername(username);
			user.setPassword(password);
			user.setEmail(email);
			user.setNombre(nombre);
			user.setApellidos(apellidos);
			user.setRol(rol);
			user.setActivo(true);
			user.setAcademia(academia);
			Usuario saved = repository.save(user);
			System.out.println("✅ Usuario " + username + " creado con rol " + rol);
			return saved;
		} else {
			// Usuario ya existe, actualizar contraseña y datos
			Usuario user = repository.findByUsername(username).get();
			user.setPassword(password);
			user.setEmail(email);
			user.setNombre(nombre);
			user.setApellidos(apellidos);
			user.setRol(rol);
			user.setActivo(true);
			user.setAcademia(academia);
			Usuario saved = repository.save(user);
			System.out.println("🔄 Usuario " + username + " actualizado con contraseña password123");
			return saved;
		}
	}
	*/
}
