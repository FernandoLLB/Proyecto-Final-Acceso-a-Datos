-- ========================================
-- Script de Datos de Prueba - ALUMNOS, SECRETARIAS, CURSOS, AULAS
-- Versión: 1.0
-- Fecha: 10/02/2026
-- ========================================
-- Este script crea datos de prueba completos para cada academia
-- Se ejecuta después de V6__datos_profesores.sql
-- Contraseña para todos los usuarios: admin123

-- ========================================
-- SECRETARIAS PARA ACADEMIAS
-- ========================================

-- Obtener IDs de academias
SET @academia1 = (SELECT id FROM academia WHERE nombre = 'Academia Elite Madrid Centro' LIMIT 1);
SET @academia2 = (SELECT id FROM academia WHERE nombre = 'Academia Elite Barcelona' LIMIT 1);
SET @academia3 = (SELECT id FROM academia WHERE nombre = 'Formación Avanzada Central' LIMIT 1);
SET @academia4 = (SELECT id FROM academia WHERE nombre = 'Formación Avanzada Norte' LIMIT 1);
SET @academia5 = (SELECT id FROM academia WHERE nombre = 'Formación Avanzada Sur' LIMIT 1);
SET @academia6 = (SELECT id FROM academia WHERE nombre = 'CEI Campus Principal' LIMIT 1);

-- Secretaria 1: Academia Elite Madrid Centro
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('secretaria1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'secretaria1@elitemadrid.com', 'Carmen', 'Vega Ruiz', 'SECRETARIA', 1, 1, @academia1);

-- Secretaria 2: Academia Elite Barcelona
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('secretaria2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'secretaria2@elitebarcelona.com', 'Pilar', 'Molina García', 'SECRETARIA', 1, 1, @academia2);

-- Secretaria 3: Formación Avanzada Central
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('secretaria3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'secretaria3@formacion.com', 'Rosa', 'Fernández López', 'SECRETARIA', 1, 1, @academia3);

-- Secretaria 4: CEI Campus Principal
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('secretaria4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'secretaria4@innovacion.com', 'Teresa', 'Gómez Martín', 'SECRETARIA', 1, 1, @academia6);

-- ========================================
-- AULAS PARA ACADEMIAS
-- ========================================

-- Aulas Academia Elite Madrid Centro
INSERT INTO aula (academia_id, nombre, capacidad, activa, recursos)
VALUES
(@academia1, 'Aula 101 - Informática', 25, 1, 'Proyector, 25 PCs, Pizarra digital, Aire acondicionado'),
(@academia1, 'Aula 102 - Diseño', 20, 1, 'Proyector, 20 iMacs, Tabletas gráficas, Software Adobe'),
(@academia1, 'Aula 103 - Teoría', 40, 1, 'Proyector, Pizarra, Sillas con mesa abatible');

-- Aulas Academia Elite Barcelona
INSERT INTO aula (academia_id, nombre, capacidad, activa, recursos)
VALUES
(@academia2, 'Aula A - Programación', 30, 1, 'Proyector, 30 PCs, Pizarra digital'),
(@academia2, 'Aula B - Bases de Datos', 25, 1, 'Proyector, 25 PCs, Servidor de prácticas'),
(@academia2, 'Sala de Reuniones', 10, 1, 'Proyector, Videoconferencia');

-- Aulas Formación Avanzada Central
INSERT INTO aula (academia_id, nombre, capacidad, activa, recursos)
VALUES
(@academia3, 'Aula Marketing 1', 35, 1, 'Proyector, WiFi alta velocidad, Pizarra'),
(@academia3, 'Aula Ciberseguridad', 20, 1, 'Proyector, 20 PCs, Laboratorio virtual, Firewall'),
(@academia3, 'Aula Inactiva', 15, 0, 'En mantenimiento');

-- Aulas Formación Avanzada Norte
INSERT INTO aula (academia_id, nombre, capacidad, activa, recursos)
VALUES
(@academia4, 'Aula Idiomas 1', 15, 1, 'Proyector, Sistema de audio, Auriculares'),
(@academia4, 'Aula Idiomas 2', 15, 1, 'Proyector, Sistema de audio, Cabinas individuales');

-- Aulas CEI Campus Principal
INSERT INTO aula (academia_id, nombre, capacidad, activa, recursos)
VALUES
(@academia6, 'Laboratorio IA', 20, 1, 'GPUs NVIDIA, Servidores, 20 estaciones de trabajo'),
(@academia6, 'Aula Data Science', 25, 1, 'Cluster de análisis, 25 PCs, Tableau Server'),
(@academia6, 'Auditorio', 100, 1, 'Escenario, Sistema de sonido, Streaming');

-- ========================================
-- ALUMNOS PARA ACADEMIAS
-- ========================================

-- Alumnos Academia Elite Madrid Centro
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno1@email.com', 'David', 'González Pérez', 'ALUMNO', 1, 1, @academia1);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia1, '2024-09-01', 'ACTIVO', 'Alumno destacado en programación');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno2@email.com', 'Sara', 'Martínez Ruiz', 'ALUMNO', 1, 1, @academia1);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia1, '2024-09-15', 'ACTIVO', 'Interesada en diseño UX/UI');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno3@email.com', 'Roberto', 'López Sánchez', 'ALUMNO', 1, 1, @academia1);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia1, '2024-10-01', 'ACTIVO', NULL);

-- Alumnos Academia Elite Barcelona
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno4@email.com', 'Lucía', 'García Fernández', 'ALUMNO', 1, 1, @academia2);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia2, '2024-08-20', 'ACTIVO', 'Experiencia previa en bases de datos');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno5@email.com', 'Fernando', 'Díaz Moreno', 'ALUMNO', 1, 1, @academia2);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia2, '2024-09-10', 'ACTIVO', NULL);

-- Alumnos Formación Avanzada Central
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno6', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno6@email.com', 'Patricia', 'Hernández Gil', 'ALUMNO', 1, 1, @academia3);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia3, '2024-07-15', 'ACTIVO', 'Emprendedora interesada en marketing digital');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno7@email.com', 'Alberto', 'Romero Castro', 'ALUMNO', 1, 1, @academia3);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia3, '2024-08-01', 'ACTIVO', 'Profesional de IT buscando especialización en seguridad');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno8', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno8@email.com', 'Beatriz', 'Jiménez Vega', 'ALUMNO', 0, 1, @academia3);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia3, '2024-06-01', 'INACTIVO', 'Baja temporal por motivos personales');

-- Alumnos Formación Avanzada Norte
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno9', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno9@email.com', 'Cristina', 'Navarro López', 'ALUMNO', 1, 1, @academia4);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia4, '2024-09-05', 'ACTIVO', 'Nivel B2 de inglés previo');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno10', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno10@email.com', 'Manuel', 'Ortiz Blanco', 'ALUMNO', 1, 1, @academia4);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia4, '2024-10-01', 'ACTIVO', 'Preparando examen TOEFL');

-- Alumnos CEI Campus Principal
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno11', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno11@email.com', 'Alejandro', 'Serrano Ramos', 'ALUMNO', 1, 1, @academia6);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia6, '2024-08-15', 'ACTIVO', 'Ingeniero informático con interés en IA');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno12', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno12@email.com', 'Marta', 'Santos Iglesias', 'ALUMNO', 1, 1, @academia6);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia6, '2024-09-01', 'ACTIVO', 'Matemática especializada en estadística');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno13', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno13@email.com', 'Jorge', 'Castro Delgado', 'ALUMNO', 1, 1, @academia6);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia6, '2024-09-20', 'ACTIVO', 'Analista de datos junior');

INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('alumno14', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'alumno14@email.com', 'Irene', 'Morales Fuentes', 'ALUMNO', 1, 1, @academia6);
INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (LAST_INSERT_ID(), @academia6, '2024-10-05', 'ACTIVO', NULL);

-- ========================================
-- CURSOS PARA ACADEMIAS
-- ========================================

-- Obtener IDs de profesores
SET @profesor1 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor1'));
SET @profesor2 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor2'));
SET @profesor3 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor3'));
SET @profesor4 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor4'));
SET @profesor5 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor5'));
SET @profesor6 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor6'));
SET @profesor7 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor7'));
SET @profesor8 = (SELECT id FROM profesor WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'profesor8'));

-- Cursos Academia Elite Madrid Centro
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, categoria, profesor_id, plazas_disponibles, activo)
VALUES
(@academia1, 'Desarrollo Web Full Stack', 'Aprende HTML, CSS, JavaScript, Node.js y React desde cero hasta nivel profesional',
 200, 1500.00, '2026-02-15', '2026-06-15', 'Programación', @profesor1, 20, 1),
(@academia1, 'Java y Spring Boot', 'Desarrollo de aplicaciones empresariales con Java y el framework Spring Boot',
 150, 1200.00, '2026-03-01', '2026-06-01', 'Programación', @profesor1, 15, 1),
(@academia1, 'Diseño UX/UI Profesional', 'Fundamentos de experiencia de usuario y diseño de interfaces con Figma y Adobe XD',
 120, 900.00, '2026-02-20', '2026-05-20', 'Diseño', @profesor2, 18, 1),
(@academia1, 'Ilustración Digital', 'Técnicas de ilustración digital con Procreate y Adobe Illustrator',
 80, 600.00, '2026-04-01', '2026-06-01', 'Diseño', @profesor2, 12, 1);

-- Cursos Academia Elite Barcelona
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, categoria, profesor_id, plazas_disponibles, activo)
VALUES
(@academia2, 'Administración de MySQL', 'Gestión, optimización y administración de bases de datos MySQL',
 100, 800.00, '2026-02-15', '2026-04-30', 'Bases de Datos', @profesor3, 20, 1),
(@academia2, 'PostgreSQL Avanzado', 'Características avanzadas de PostgreSQL: JSON, replicación, particionamiento',
 80, 750.00, '2026-03-15', '2026-05-30', 'Bases de Datos', @profesor3, 15, 1),
(@academia2, 'MongoDB y NoSQL', 'Introducción a bases de datos NoSQL con MongoDB',
 60, 500.00, '2026-04-01', '2026-05-15', 'Bases de Datos', @profesor3, 25, 1);

-- Cursos Formación Avanzada Central
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, categoria, profesor_id, plazas_disponibles, activo)
VALUES
(@academia3, 'Marketing Digital 360', 'Estrategias completas de marketing digital: SEO, SEM, redes sociales, email marketing',
 150, 1100.00, '2026-02-10', '2026-05-30', 'Marketing', @profesor4, 30, 1),
(@academia3, 'Community Manager Profesional', 'Gestión profesional de redes sociales para empresas',
 80, 650.00, '2026-03-01', '2026-05-01', 'Marketing', @profesor4, 25, 1),
(@academia3, 'Hacking Ético y Pentesting', 'Fundamentos de hacking ético, análisis de vulnerabilidades y pentesting',
 120, 1300.00, '2026-02-15', '2026-05-15', 'Ciberseguridad', @profesor5, 15, 1),
(@academia3, 'Seguridad en Redes', 'Configuración segura de redes, firewalls y sistemas de detección de intrusos',
 100, 1000.00, '2026-04-01', '2026-06-30', 'Ciberseguridad', @profesor5, 18, 1);

-- Cursos Formación Avanzada Norte
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, categoria, profesor_id, plazas_disponibles, activo)
VALUES
(@academia4, 'Inglés B2 First Certificate', 'Preparación intensiva para el examen Cambridge B2 First',
 180, 800.00, '2026-02-01', '2026-07-31', 'Idiomas', @profesor6, 12, 1),
(@academia4, 'Inglés C1 Advanced', 'Preparación para el examen Cambridge C1 Advanced',
 200, 950.00, '2026-02-01', '2026-08-31', 'Idiomas', @profesor6, 10, 1),
(@academia4, 'Business English', 'Inglés para negocios y entorno profesional',
 100, 700.00, '2026-03-15', '2026-06-15', 'Idiomas', @profesor6, 15, 1);

-- Cursos CEI Campus Principal
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, categoria, profesor_id, plazas_disponibles, activo)
VALUES
(@academia6, 'Machine Learning con Python', 'Fundamentos de machine learning: regresión, clasificación, clustering con Python y scikit-learn',
 160, 1600.00, '2026-02-15', '2026-06-15', 'Inteligencia Artificial', @profesor7, 18, 1),
(@academia6, 'Deep Learning y Redes Neuronales', 'Redes neuronales profundas con TensorFlow y PyTorch',
 140, 1800.00, '2026-03-01', '2026-06-30', 'Inteligencia Artificial', @profesor7, 15, 1),
(@academia6, 'Procesamiento de Lenguaje Natural', 'NLP con transformers, BERT y GPT',
 100, 1400.00, '2026-04-15', '2026-07-15', 'Inteligencia Artificial', @profesor7, 12, 1),
(@academia6, 'Análisis de Datos con Python', 'Pandas, NumPy, visualización de datos y estadística descriptiva',
 120, 900.00, '2026-02-10', '2026-05-10', 'Data Science', @profesor8, 25, 1),
(@academia6, 'Big Data y Apache Spark', 'Procesamiento de grandes volúmenes de datos con Spark',
 100, 1200.00, '2026-03-15', '2026-06-15', 'Data Science', @profesor8, 20, 1),
(@academia6, 'Visualización con Tableau', 'Creación de dashboards profesionales con Tableau',
 60, 600.00, '2026-04-01', '2026-05-30', 'Data Science', @profesor8, 22, 1);

-- ========================================
-- MATRÍCULAS
-- ========================================

-- Obtener IDs de alumnos
SET @alumno1 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno1'));
SET @alumno2 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno2'));
SET @alumno3 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno3'));
SET @alumno4 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno4'));
SET @alumno5 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno5'));
SET @alumno6 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno6'));
SET @alumno7 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno7'));
SET @alumno9 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno9'));
SET @alumno10 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno10'));
SET @alumno11 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno11'));
SET @alumno12 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno12'));
SET @alumno13 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno13'));
SET @alumno14 = (SELECT id FROM alumno WHERE usuario_id = (SELECT id FROM usuario WHERE username = 'alumno14'));

-- Obtener IDs de cursos
SET @curso1 = (SELECT id FROM curso WHERE nombre = 'Desarrollo Web Full Stack' AND academia_id = @academia1 LIMIT 1);
SET @curso2 = (SELECT id FROM curso WHERE nombre = 'Java y Spring Boot' AND academia_id = @academia1 LIMIT 1);
SET @curso3 = (SELECT id FROM curso WHERE nombre = 'Diseño UX/UI Profesional' AND academia_id = @academia1 LIMIT 1);
SET @curso4 = (SELECT id FROM curso WHERE nombre = 'Administración de MySQL' AND academia_id = @academia2 LIMIT 1);
SET @curso5 = (SELECT id FROM curso WHERE nombre = 'PostgreSQL Avanzado' AND academia_id = @academia2 LIMIT 1);
SET @curso6 = (SELECT id FROM curso WHERE nombre = 'Marketing Digital 360' AND academia_id = @academia3 LIMIT 1);
SET @curso7 = (SELECT id FROM curso WHERE nombre = 'Hacking Ético y Pentesting' AND academia_id = @academia3 LIMIT 1);
SET @curso8 = (SELECT id FROM curso WHERE nombre = 'Inglés B2 First Certificate' AND academia_id = @academia4 LIMIT 1);
SET @curso9 = (SELECT id FROM curso WHERE nombre = 'Inglés C1 Advanced' AND academia_id = @academia4 LIMIT 1);
SET @curso10 = (SELECT id FROM curso WHERE nombre = 'Machine Learning con Python' AND academia_id = @academia6 LIMIT 1);
SET @curso11 = (SELECT id FROM curso WHERE nombre = 'Deep Learning y Redes Neuronales' AND academia_id = @academia6 LIMIT 1);
SET @curso12 = (SELECT id FROM curso WHERE nombre = 'Análisis de Datos con Python' AND academia_id = @academia6 LIMIT 1);

-- Matrículas Academia Elite Madrid Centro
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, observaciones)
VALUES
(@academia1, @alumno1, @curso1, '2026-02-01 10:00:00', 'ACTIVA', 'Matrícula pagada al contado'),
(@academia1, @alumno1, @curso2, '2026-02-20 11:30:00', 'ACTIVA', NULL),
(@academia1, @alumno2, @curso3, '2026-02-15 09:00:00', 'ACTIVA', 'Descuento 10% aplicado'),
(@academia1, @alumno2, @curso1, '2026-02-15 09:15:00', 'ACTIVA', NULL),
(@academia1, @alumno3, @curso1, '2026-02-10 14:00:00', 'ACTIVA', NULL);

-- Matrículas Academia Elite Barcelona
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, observaciones)
VALUES
(@academia2, @alumno4, @curso4, '2026-02-01 09:30:00', 'ACTIVA', 'Profesional en activo'),
(@academia2, @alumno4, @curso5, '2026-03-01 10:00:00', 'ACTIVA', NULL),
(@academia2, @alumno5, @curso4, '2026-02-05 11:00:00', 'ACTIVA', NULL);

-- Matrículas Formación Avanzada Central
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, observaciones)
VALUES
(@academia3, @alumno6, @curso6, '2026-01-20 10:30:00', 'ACTIVA', 'Plan de financiación'),
(@academia3, @alumno7, @curso7, '2026-02-01 09:00:00', 'ACTIVA', 'Certificación OSCP objetivo');

-- Matrículas Formación Avanzada Norte
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, observaciones)
VALUES
(@academia4, @alumno9, @curso8, '2026-01-25 10:00:00', 'ACTIVA', 'Preparación examen junio'),
(@academia4, @alumno10, @curso9, '2026-01-28 11:00:00', 'ACTIVA', 'Nivel previo C1 parcial');

-- Matrículas CEI Campus Principal
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, observaciones)
VALUES
(@academia6, @alumno11, @curso10, '2026-02-01 09:00:00', 'ACTIVA', 'Beca parcial aprobada'),
(@academia6, @alumno11, @curso11, '2026-02-25 10:00:00', 'ACTIVA', NULL),
(@academia6, @alumno12, @curso10, '2026-02-05 09:30:00', 'ACTIVA', NULL),
(@academia6, @alumno12, @curso12, '2026-02-05 09:45:00', 'ACTIVA', NULL),
(@academia6, @alumno13, @curso12, '2026-02-10 10:00:00', 'ACTIVA', 'Pago fraccionado'),
(@academia6, @alumno14, @curso10, '2026-02-15 11:00:00', 'ACTIVA', NULL);

-- ========================================
-- RESERVAS DE AULAS
-- ========================================

-- Obtener IDs de aulas
SET @aula1 = (SELECT id FROM aula WHERE nombre = 'Aula 101 - Informática' AND academia_id = @academia1 LIMIT 1);
SET @aula2 = (SELECT id FROM aula WHERE nombre = 'Aula 102 - Diseño' AND academia_id = @academia1 LIMIT 1);
SET @aula3 = (SELECT id FROM aula WHERE nombre = 'Aula A - Programación' AND academia_id = @academia2 LIMIT 1);
SET @aula4 = (SELECT id FROM aula WHERE nombre = 'Aula Marketing 1' AND academia_id = @academia3 LIMIT 1);
SET @aula5 = (SELECT id FROM aula WHERE nombre = 'Aula Idiomas 1' AND academia_id = @academia4 LIMIT 1);
SET @aula6 = (SELECT id FROM aula WHERE nombre = 'Laboratorio IA' AND academia_id = @academia6 LIMIT 1);
SET @aula7 = (SELECT id FROM aula WHERE nombre = 'Aula Data Science' AND academia_id = @academia6 LIMIT 1);

-- Reservas para cursos en progreso
INSERT INTO reserva_aula (academia_id, aula_id, profesor_id, curso_id, fecha_inicio, fecha_fin, motivo, estado)
VALUES
-- Reservas Academia Elite Madrid Centro
(@academia1, @aula1, @profesor1, @curso1, '2026-02-17 09:00:00', '2026-02-17 13:00:00', 'Clase Desarrollo Web Full Stack - Módulo HTML/CSS', 'ACTIVA'),
(@academia1, @aula1, @profesor1, @curso1, '2026-02-18 09:00:00', '2026-02-18 13:00:00', 'Clase Desarrollo Web Full Stack - Módulo JavaScript', 'ACTIVA'),
(@academia1, @aula1, @profesor1, @curso2, '2026-03-03 15:00:00', '2026-03-03 19:00:00', 'Clase Java y Spring Boot - Introducción', 'ACTIVA'),
(@academia1, @aula2, @profesor2, @curso3, '2026-02-24 10:00:00', '2026-02-24 14:00:00', 'Clase Diseño UX/UI - Fundamentos', 'ACTIVA'),

-- Reservas Academia Elite Barcelona
(@academia2, @aula3, @profesor3, @curso4, '2026-02-17 09:00:00', '2026-02-17 12:00:00', 'Clase MySQL - Instalación y configuración', 'ACTIVA'),
(@academia2, @aula3, @profesor3, @curso5, '2026-03-17 14:00:00', '2026-03-17 18:00:00', 'Clase PostgreSQL - JSON y JSONB', 'ACTIVA'),

-- Reservas Formación Avanzada Central
(@academia3, @aula4, @profesor4, @curso6, '2026-02-12 10:00:00', '2026-02-12 14:00:00', 'Clase Marketing Digital - SEO Básico', 'ACTIVA'),

-- Reservas Formación Avanzada Norte
(@academia4, @aula5, @profesor6, @curso8, '2026-02-03 09:00:00', '2026-02-03 11:00:00', 'Clase Inglés B2 - Speaking practice', 'ACTIVA'),
(@academia4, @aula5, @profesor6, @curso9, '2026-02-03 11:30:00', '2026-02-03 13:30:00', 'Clase Inglés C1 - Advanced grammar', 'ACTIVA'),

-- Reservas CEI Campus Principal
(@academia6, @aula6, @profesor7, @curso10, '2026-02-17 09:00:00', '2026-02-17 13:00:00', 'Clase Machine Learning - Regresión lineal', 'ACTIVA'),
(@academia6, @aula6, @profesor7, @curso11, '2026-03-03 14:00:00', '2026-03-03 18:00:00', 'Clase Deep Learning - Redes convolucionales', 'ACTIVA'),
(@academia6, @aula7, @profesor8, @curso12, '2026-02-12 10:00:00', '2026-02-12 14:00:00', 'Clase Análisis de Datos - Pandas básico', 'ACTIVA');

-- ========================================
-- VERIFICACIÓN
-- ========================================

SELECT '========================================' AS '';
SELECT 'RESUMEN DE DATOS CREADOS V7' AS Resultado;
SELECT '========================================' AS '';

SELECT 'SECRETARIAS' AS Entidad, COUNT(*) AS Total FROM usuario WHERE rol = 'SECRETARIA';

SELECT 'AULAS' AS Entidad, COUNT(*) AS Total FROM aula;

SELECT 'ALUMNOS' AS Entidad, COUNT(*) AS Total FROM alumno;

SELECT 'CURSOS' AS Entidad, COUNT(*) AS Total FROM curso;

SELECT 'MATRÍCULAS' AS Entidad, COUNT(*) AS Total FROM matricula;

SELECT 'RESERVAS DE AULA' AS Entidad, COUNT(*) AS Total FROM reserva_aula;

SELECT '========================================' AS '';
SELECT 'DETALLE POR ACADEMIA' AS Resultado;
SELECT '========================================' AS '';

SELECT
    a.nombre AS Academia,
    (SELECT COUNT(*) FROM alumno al WHERE al.academia_id = a.id) AS Alumnos,
    (SELECT COUNT(*) FROM curso c WHERE c.academia_id = a.id) AS Cursos,
    (SELECT COUNT(*) FROM aula au WHERE au.academia_id = a.id) AS Aulas,
    (SELECT COUNT(*) FROM matricula m WHERE m.academia_id = a.id) AS Matriculas
FROM academia a
ORDER BY a.nombre;

SELECT '========================================' AS '';
SELECT 'CREDENCIALES DE PRUEBA' AS Resultado;
SELECT '========================================' AS '';
SELECT 'Secretarias: secretaria1, secretaria2, secretaria3, secretaria4' AS Info;
SELECT 'Alumnos: alumno1 a alumno14' AS Info;
SELECT 'Contraseña para todos: admin123' AS Info;

