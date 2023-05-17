# JWT SpringBoot3 y Spring Security6

Este proyecto es una implementación de autenticación con JsonWebToken usando la ultima version de Spring boot y Spring Security.

Para ponder iniciar el proyecto usando Docker, sigue estos pasos:

```bash
# 
docker build -t jwt_app:1.0.0 .
```

Luego de Generar la imagen, solo queda usar el docker-compose:

```bash
docker-compose up
```

El servidor por defecto se queda escuchando en el puerto 8080, pero podes elejir el numero de puerto que quieras.
Solo tienes que pasarle en el docker-compose una variable de entorno **APP_PORT** antes de iniciar la aplicación.

