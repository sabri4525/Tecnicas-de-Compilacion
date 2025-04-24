# Compilador Proyecto

Este es un proyecto de compilador desarrollado como parte de un curso de Técnicas de Compilación. El objetivo de este proyecto es implementar un compilador básico utilizando ANTLR para el análisis sintáctico y semántico de un lenguaje de programación simple.

## Estructura del Proyecto

El proyecto sigue la siguiente estructura de directorios:

```
compilador-proyecto
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── ejemplo
│   │   │           └── compilador
│   │   │               ├── App.java          # Clase principal del compilador
│   │   │               └── parser
│   │   │                   └── MyGrammar.g4  # Archivo de gramática ANTLR
│   │   └── resources
│   │       └── log4j.properties             # Configuración de logging
│   ├── test
│       └── java
│           └── com
│               └── ejemplo
│                   └── compilador
│                       └── AppTest.java     # Pruebas unitarias para la clase App
├── pom.xml                                      # Archivo de configuración de Maven
└── README.md                                    # Este archivo
```

## Requisitos

- Java JDK 11 o superior
- Maven 3.6 o superior
- ANTLR 4.9 o superior

## Cómo Ejecutar el Proyecto

1. Clona este repositorio en tu máquina local.
2. Navega al directorio del proyecto.
3. Compila el proyecto utilizando Maven:

   ```
   mvn clean install
   ```

4. Ejecuta la aplicación:

   ```
   mvn exec:java -Dexec.mainClass="com.ejemplo.compilador.App"
   ```

## Pruebas

Las pruebas unitarias se encuentran en el directorio `src/test/java`. Puedes ejecutar las pruebas utilizando el siguiente comando:

```
mvn test
```

## Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o un pull request.

## Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo LICENSE para más detalles.