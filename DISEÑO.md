# Documento de Diseño - Bibliotech

## 1. Introducción

**Bibliotech** es un sistema de gestión bibliotecaria diseñado para administrar préstamos de recursos bibliográficos (libros y ebooks) entre estudiantes y docentes de una institución educativa.

### 1.1 Propósito

El sistema permite:
- Registrar socios (estudiantes y docentes)
- Gestionar el catálogo de recursos (libros y ebooks)
- Controlar el proceso de préstamos y devoluciones
- Aplicar sanciones por devolución tardía
- Consultar el estado de préstamos activos, vencidos y próximos a vencer

---

## 2. Arquitectura General

El sistema sigue una **arquitectura multicapa** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│           Capa de Servicios             │
│  (PrestamoService, RecursoService,      │
│   SocioService, SancionService)         │
├─────────────────────────────────────────┤
│           Capa de Repositorio           │
│  (InMemoryXXXRepository)                │
├─────────────────────────────────────────┤
│           Capa de Modelo                │
│  (Socio, Recurso, Prestamo)             │
└─────────────────────────────────────────┘
```

### 2.1 Decisión de Diseño: Arquitectura en Capas

**Justificación**: La separación en capas permite:
- **Bajo acoplamiento**
- **Mantenibilidad**
- **Reusabilidad**

---

## 3. Modelo de Dominio

### 3.1 Jerarquía de Socios

```
Socio (abstracta)
├── Estudiante
└── Docente
```

**Decisión de Diseño: Clase Abstracta con Especialización**

| Elección                         | Justificación |
|----------------------------------|---------------|
| **Clase abstracta con herencia** | Permite definir comportamiento específico por tipo sin modificar código común |

**Socio** define el comportamiento común:
- Identificación (id, nombre, DNI, email)
- Sistema de sanciones (sancionado, fechaFinSancion)
- Método abstracto `getLimitePrestamo()`

**Estudiante**: límite de 3 préstamos simultáneos

**Docente**: límite de 5 préstamos simultáneos

### 3.2 Interface Recurso

```java
public interface Recurso {
    String isbn();
    String titulo();
    String autor();
    int anio();
    Categoria categoria();
}
```

**Decisión de Diseño: Interface en lugar de Clase Abstracta**

| Elección              | Justificación |
|-----------------------|---------------
| **Interface Recurso** | Permite implementar por diferentes tipos de recursos (Libro, Ebook) sin heredar de una clase base |

**Implementaciones**:
- **Libro**: características físicas (páginas, editorial, ubicación en estantería)
- **Ebook**: características digitales (formato, tamaño, URL de descarga)

### 3.3 Clase Prestamo

**Decisión de Diseño: Estado como Enum en lugar de Constantes**

```java
public enum EstadoPrestamo {
    ACTIVO,
    DEVUELTO,
    VENCIDO
}
```

| Elección                                 | Justificación |
|------------------------------------------|-------------|
| **Enum EstadoPrestamo**                  | Tipado seguro, sin posibilidad de estados inválidos |

**Atributos clave**:
- `fechaPrestamo`: fecha actual al crear
- `fechaLimite`: fecha actual + días de préstamo
- `fechaDevolucion`: null hasta que se devuelve
- `estado`: evoluciona según reglas de negocio

---

## 4. Capa de Servicios

### 4.1 Interfaz vs Implementación

**Decisión de Diseño: Interfaces para Servicios**

Cada servicio define una **interfaz** que es implementada por una clase concreta:

```
SocioService (interfaz)
    └── SocioServiceImpl (implementación)
```

**Justificación**:
- **Inversión de dependencias**: Los clientes dependen de abstractions, no de implementaciones
- **Flexibilidad**: Se puede cambiar implementación sin modificar código cliente

### 4.2 Responsabilidades de Cada Servicio

| Servicio | Responsabilidad |
|----------|-----------------|
| **SocioService** | Registro de socios, búsqueda por id/DNI |
| **RecursoService** | Registro de recursos, búsqueda por diferentes criterios |
| **PrestamoService** | Creación/devolución de préstamos, control de vencimientos |
| **SancionService** | Aplicación y verificación de sanciones |

---

## 5. Capa de Repositorio

### 5.1 Patrón Repository

```java
public interface Repository<T> {
    void guardar(T elemento);
    Optional<T> buscarPorId(Integer id);
    List<T> buscarTodos();
}
```

**Decisión de Diseño: Persistencia en Memoria**

| Elección               | Justificación |
|------------------------|---------------|
| **InMemoryRepository** | Simplifica desarrollo, cumple requisitos del TP |

**Justificación general del patrón**:
- Abstrae la capa de persistencia
- Permite cambiar la implementación sin afectar la lógica de negocio
- Centraliza operaciones CRUD

---

## 6. Sistema de Excepciones

**Decisión de Diseño: Jerarquía de Excepciones Específicas**

```
BibliotecaException (base)
├── SocioException
│   ├── SocioNoEncontradoException
│   ├── SocioDuplicadoException
│   ├── SocioSancionadoException
│   └── DniDuplicadoException / EmailDuplicadoException
├── RecursoException
│   ├── RecursoNoEncontradoException
│   ├── RecursoNoDisponibleException
│   └── IsbnDuplicadoException
├── PrestamoException
│   ├── PrestamoNoEncontradoException
│   ├── PrestamoVencidoException
│   ├── DevolucionInvalidaException
│   └── LimitePrestamoExcedidoException
└── ValidacionException
```

**Justificación**:
- **Tratamiento diferenciado**: El cliente puede catchear excepciones específicas según necesidad
- **Información contextual**: Cada excepción aporta información relevante (ej. ISBN duplicado, DNI del socio conflictivo)

---

## 7. Funcionalidades del Sistema

El sistema ofrece una interfaz CLI (línea de comandos) con las siguientes funcionalidades organizadas por módulo:

### 7.1 Gestión de Socios

| Funcionalidad | Descripción |
|-------------|------------|
| **Registrar Estudiante** | Crea un nuevo socio estudiante con validación de datos (nombre, DNI único, email válido) |
| **Registrar Docente** | Crea un nuevo socio docente con validación de datos |
| **Buscar por ID** | Consulta un socio por su identificador único |
| **Listar todos** | Muestra todos los socios registrados |

**Validaciones:**
- Nombre: solo letras y espacios, máximo 100 caracteres
- DNI: 7 u 8 dígitos numéricos, debe ser único en el sistema
- Email: formato válido (usuario@dominio.com), debe ser único

### 7.2 Gestión de Recursos

| Funcionalidad | Descripción |
|-------------|------------|
| **Registrar Libro** | Registra un libro físico con ISBN (13 dígitos), título, autor, año, categoría, páginas, editorial, ubicación |
| **Registrar Ebook** | Registra un recurso digital con ISBN, título, autor, año, categoría, formato, tamaño, URL de descarga |
| **Buscar por ISBN** | Consulta un recurso por su ISBN |
| **Buscar por Título** | Búsqueda avanzada por palabras en el título |
| **Buscar por Autor** | Búsqueda avanzada por nombre de autor |
| **Buscar por Categoría** | Filtra recursos por categoria |
| **Listar todos** | Muestra todos los recursos del catálogo |


### 7.3 Gestión de Préstamos

| Funcionalidad | Descripción |
|-------------|------------|
| **Realizar préstamo** | Crea un nuevo préstamo verificando: socio existe, no está sancionado, no excede límite, recurso disponible |
| **Devolver préstamo** | Registra la devolución, calcula días de retraso y aplica sanción si corresponde |
| **Listar todos** | Muestra historial completo de préstamos |
| **Listar por socio** | Muestra préstamos de un socio específico |
| **Listar activos** | Muestra préstamos vigentes |
| **Listar vencidos** | Muestra préstamos no devueltos y fuera de fecha |
| **Próximos a vencer** | Lista préstamos que vencen en N días |
| **Verificar disponibilidad** | Consulta si un recurso está disponible para préstamo |

**Reglas de negocio:**
- Estudiante máximo 3 préstamos simultáneos
- Docente máximo 5 préstamos simultáneos
- Un recurso con préstamo activo no puede ser prestado a otro socio

### 7.4 Gestión de Sanciones

| Funcionalidad | Descripción |
|-------------|------------|
| **Listar sancionados** | Muestra todos los socios con sanción activa |
| **Verificar sanción** | Consulta si un socio específico está sancionado |

**Sistema automático:**
- Al devolver un préstamo vencido, se aplica sanción automáticamente
- La sanción dura: `días de retraso × 2`
- El socio sancionado no puede realizar nuevos préstamos hasta que expire la sanción
- Las sanciones se verifican automáticamente al intentar un préstamo

---

## 8. Reglas de Negocio Implementadas

### 7.1 Verificación de Disponibilidad

Un recurso está disponible si **no posee préstamos activos**.

### 7.2 Límites por Tipo de Socio

- **Estudiante**: máximo 3 préstamos simultáneos
- **Docente**: máximo 5 préstamos simultáneos

### 7.3 Sistema de Sanciones

- Al detectar préstamo vencido, se establece sanción automática
- La sanción dura: `días de retraso * 2` (fórmula configurable)
- El socio sancionado no puede realizar nuevos préstamos

### 7.4 Verificación de Vencimientos

- Al verificar, un préstamo ACTIVO cuya fecha actual >= fecha límite pasa a estado VENCIDO
- Un préstamo VENCIDO genera sanción automática al socio

---

## 8. Decisiones Técnicas Adicionales

### 8.1 Uso de Records

`Libro` y `Ebook` utilizan **record** en lugar de clase tradicional:

```java
public record Libro(...) implements Recurso {}
```


### 8.2 Optional para Búsquedas

Los métodos de búsqueda retornan `Optional<T>` en lugar de `null`:

```java
Optional<Recurso> buscarPorIsbn(String isbn);
```

---

## 9. Conclusión

El diseño de Bibliotech prioriza:

- **Separación de responsabilidades**: cada clase tiene una única razón para change
- **Bajo acoplamiento**: comunicación via interfaces bien definidas
- **Alta cohesión**: servicios con responsabilidades claras y delimitadas

Estas decisiones siguen los principios SOLID y los patrones de diseño conocidos, facilitando el mantenimiento y evolución del sistema a futuro.