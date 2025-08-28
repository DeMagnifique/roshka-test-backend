INSERT INTO users (username, password, email, role) VALUES 
  ('admin', '$2a$10$N2c1tzHzErhiZmnNc.mc4uwP4nAnypmx/T3cSIibvbC8baDp3zHsi', 'admin@roshka.com', 'ADMIN'),
  ('user', '$2a$10$noutQDn/vZchutnnO8F4uuDstUv/E/zchyzgT9RuTIzfWZ1xj6/me', 'user@roshka.com', 'USER');

--admin: passwordadmin
--user: userpass

INSERT INTO tasks (title, description, category) VALUES 
  ('Desarrollar API REST', 'Implementar CRUD completo para gestión de tareas', 'DESARROLLO'),
  ('Configurar Base de Datos', 'Setup H2 y configuración de JPA', 'INFRAESTRUCTURA'),
  ('Implementar Autenticación', 'Agregar JWT y seguridad', 'SEGURIDAD'),
  ('Testing de Endpoints', 'Probar todos los endpoints con Postman', 'QA'),
  ('Documentación', 'Crear documentación completa del proyecto', 'DOCUMENTACION');

-- SubTasks de ejemplo
INSERT INTO subtasks (title, description, task_id) VALUES 
  ('Crear entidades JPA', 'Definir Task y SubTask entities', 1),
  ('Implementar repositorios', 'Crear TaskRepository y SubTaskRepository', 1),
  ('Desarrollar servicios', 'Lógica de negocio para CRUD operations', 1),
  ('Crear controladores REST', 'Endpoints para Tasks y SubTasks', 1),
  ('Configurar H2 Console', 'Habilitar acceso a base de datos', 2),
  ('Setup conexión JPA', 'Configurar DataSource y propiedades', 2);
