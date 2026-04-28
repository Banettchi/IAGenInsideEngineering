# SOLUCION.md — Problema #1: El Videoclub de Don Mario

---

## 1. Patrones de Diseño Utilizados

### 🏭 Factory Method (`MovieFactory`)
**Propósito:** centralizar la creación de objetos `Movie` sin exponer las clases concretas (`PhysicalMovie`, `DigitalMovie`) al resto del sistema.

```java
// El cliente crea películas sin conocer las clases concretas:
Movie m = MovieFactory.create(MovieFactory.PHYSICAL, "Interestellar", 8000, true);
```

**Por qué aplica aquí:** el sistema debe registrar películas físicas o digitales. El Factory Method permite agregar nuevos tipos (por ejemplo, `StreamingMovie`) sin tocar el código de `Main` ni de `RentalService`.

---

### 🔀 Strategy (`MembershipStrategy`)
**Propósito:** encapsular los algoritmos de cálculo de precio para cada tipo de membresía en clases separadas, intercambiables en tiempo de ejecución.

```
MembershipStrategy (interfaz)
   ├── BasicMembership   → sin descuento
   └── PremiumMembership → 20% de descuento
```

**Por qué aplica aquí:** el precio varía según la membresía del cliente. Con Strategy, agregar una membresía `Gold` solo requiere crear una nueva clase — sin modificar `RentalService`.

---

## 2. Principios SOLID Aplicados

| Principio | Dónde se aplica |
|-----------|-----------------|
| **S** — Single Responsibility | `MovieFactory` solo crea películas. `RentalService` solo calcula y muestra el recibo. `Main` solo orquesta el flujo. |
| **O** — Open/Closed | Nuevas membresías o tipos de película se agregan con clases nuevas, sin modificar el código existente. |
| **L** — Liskov Substitution | `PhysicalMovie` y `DigitalMovie` son sustituibles por `Movie` en cualquier punto del sistema sin romper el comportamiento. |
| **I** — Interface Segregation | `MembershipStrategy` expone únicamente los métodos necesarios para calcular precios. `Movie` expone solo lo relevante para una película. |
| **D** — Dependency Inversion | `RentalService` depende de `Movie` y `MembershipStrategy` (abstracciones), no de `PhysicalMovie` ni `PremiumMembership` (implementaciones concretas). |

---

## 3. Polimorfismo y Encapsulamiento

### Polimorfismo
- `RentalService.printReceipt(List<Movie>, MembershipStrategy)` trabaja con las **interfaces**, nunca con clases concretas.
- El mismo método procesa indiferentemente `PhysicalMovie` y `DigitalMovie`, llamando `getType()`, `getTitle()`, `getPrice()` de forma polimórfica.

### Encapsulamiento
- Todos los campos de `PhysicalMovie` y `DigitalMovie` son `private final`.
- El estado de disponibilidad solo se expone a través de `isAvailable()`.
- `MovieFactory` tiene constructor privado y solo expone el método estático `create(...)`.

---

## 4. Estructura de Clases

```
donmario/
├── Main.java                          ← Punto de entrada, menú por consola
├── model/
│   ├── Movie.java                     ← Interfaz (abstracción)
│   ├── PhysicalMovie.java             ← Implementación concreta
│   └── DigitalMovie.java              ← Implementación concreta
├── factory/
│   └── MovieFactory.java              ← Patrón Factory Method
├── membership/
│   ├── MembershipStrategy.java        ← Interfaz Strategy
│   ├── BasicMembership.java           ← Estrategia sin descuento
│   └── PremiumMembership.java         ← Estrategia con 20% de descuento
└── rental/
    └── RentalService.java             ← Genera el recibo
```

---

## 5. Evidencia de Ejecución

### Caso 1 — Membresía Premium, películas 1 y 3 (caso del enunciado)

**Entrada:**
```
Membresía: 2 (Premium)
Películas: 1,3
```

**Salida:**
```
--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
--------------------------
¡Disfrute su pelicula!
```

✅ Coincide exactamente con el caso de ejemplo del enunciado.

---

### Caso 2 — Membresía Básica, películas 1, 3 y 4

**Entrada:**
```
Membresía: 1 (Basica)
Películas: 1,3,4
```

**Salida:**
```
--- RECIBO DE ALQUILER ---
Cliente: Basica
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
 - Matrix (Digital) - $6.000
Subtotal: $19.000
Total a pagar: $19.000
--------------------------
¡Disfrute su pelicula!
```

✅ Sin descuento para membresía Básica.

---

### Caso 3 — Intento de alquilar película no disponible (El Padrino)

**Entrada:**
```
Membresía: 2 (Premium)
Películas: 2,3
```

**Salida:**
```
⚠  Advertencias:
   - 'El Padrino' no está disponible.

--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Inception (Digital) - $5.000
Subtotal: $5.000
Total a pagar: $5.000
--------------------------
¡Disfrute su pelicula!
```

✅ El sistema omite la película no disponible y advierte al usuario.

---

## 6. Prompt Utilizado
# Yo le puse este:
https://github.com/IgnacioCastillo05/IAGenInsideEngineering.git


analiza este repo cada cosa y ayudame a resolver los ejercicios que piden empezando por el primer ejercicio que es este que se encuentra en el readme:
Duración: Máximo 15 minutos
Don Mario acaba de abrir un videoclub moderno donde los clientes pueden alquilar peliculas fisicas o digitales. El problema es que su sistema anterior era un caos: todos los precios se calculaban igual sin importar el tipo de pelicula o membresia del cliente, y no habia forma de saber que peliculas estaban disponibles en tiempo real.
Tu Mision
Ayuda a Don Mario creando un sistema de alquiler que permita:

1. Registrar peliculas (fisicas o digitales) con su disponibilidad.
2. Que el cliente elija X peliculas para alquilar.
3. Calcular el precio total segun su tipo de membresia:
   * Basica: precio normal.
   * Premium: 20% de descuento.
4. Mostrar al finalizar un recibo con las peliculas, precio por unidad y total.
Peliculas Disponibles

* [Fisica] Interestellar - $8.000 - Disponible
* [Fisica] El Padrino - $7.000 - No disponible
* [Digital] Inception - $5.000 - Disponible
* [Digital] Matrix - $6.000 - Disponible
Caso de Ejemplo
Membresia del cliente: Premium Seleccione peliculas (numeros separados por coma): 1,3

```
--- RECIBO DE ALQUILER ---
Cliente: Premium
Peliculas:
 - Interestellar (Fisica) - $8.000
 - Inception (Digital) - $5.000
Subtotal: $13.000
Descuento (20%): $2.600
Total a pagar: $10.400
--------------------------
¡Disfrute su pelicula!

```

Objetivos del Ejercicio

* Identificar cual o cuales patrones de diseno utilizar.
* Explicar que principios de SOLID se aplican.
* Aplicar polimorfismo y encapsulamiento.
* Colocar evidencia de la ejecucion del ejercicio (ejecucion por consola; no es necesario hacer front).

analiza todo el contenidpo que te pueda ser util que se encuentre en el mismo repo y empieza a resolverlo

# El que uso claude lo mejoro y uso este:
> "Desarrolla en Java puro (sin frameworks) el sistema de videoclub de Don Mario. 
> El sistema debe registrar películas físicas y digitales con disponibilidad, 
> permitir seleccionar películas por número, calcular el precio según membresía 
> (Básica: precio normal, Premium: 20% descuento) y mostrar un recibo formateado.
> Aplica los patrones Factory Method y Strategy, cumple los principios SOLID, 
> usa polimorfismo con una interfaz Movie implementada por PhysicalMovie y 
> DigitalMovie, y encapsula todos los campos como privados. 
> La solución debe ejecutarse completamente por consola."

## 7. Adicional
Tuve problemas al momento de clonarlo peor como tal se acabo el ejercicio a los 12 minutos desde que empezo el reto