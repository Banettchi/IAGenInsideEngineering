
## IAGen Inside Engineering

## IA Generativa en Ingeniería de Sistemas: ¿Potenciador o Dependencia para los Desarrolladores?

## Actividad para curso de CVDS/DOSW

### Enunciado problema

Una tienda virtual desea implementar un sistema de pagos que soporte múltiples métodos: tarjeta de crédito, PayPal y criptomonedas. Cada forma de pago tiene su propio proceso de validación y forma de ejecución. El sistema debe poder crear el objeto de pago y su validador correspondiente sin que la lógica principal de compras tenga que preocuparse por los detalles internos. Además, cuando un pago se procesa exitosamente, el sistema debe notificar automáticamente a otros componentes: 
• El módulo de inventario debe descontar el producto. 
• El módulo de facturación debe generar la factura. 
• El módulo de notificaciones debe enviar un correo al cliente. 
La solución debe ser flexible para soportar nuevos medios de pago y nuevos módulos que necesiten reaccionar al evento de pago, sin modificarla lógica principal del sistema. 
Pistas: se requiere un mecanismo para crear familias de objetos relacionados (pago + validador) y otro para avisar automáticamente a varios módulos cuando ocurre un evento.

### Objetivos

1. Identificar los dos patrones que se están utilizando para la solución de este ejercicio, ¿Crees que son los patrones adecuados o alguno debe cambiar?

2. ¿Qué clases/interfaces hacen falta por implementar del código dado para que satisfagan correctamente los dos patrones utilizados? Dejarlas escritas en el README (no es necesario que lo modifiquen en el diagrama de clases dado)

3. Según el problema dado, ¿El diagrama de contexto si les da información suficiente y pertinente del problema para su solución? Si es necesaria su modificación, colocar lo que cambiarían en el README (no es necesario que lo modifiquen en el diagrama de contexto dado)

3. ¿Qué errores del código dado identifican? ¿Por qué el código no logra compilar?

4. Corregir el código dado para poder solucionar el problema dado

5. En el código dado se les dieron pruebas unitarias para que cuando el código esté hecho, puedan probarlo. Realizar la ejecución de las pruebas unitarias.
En caso de que fallen, corregir las pruebas unitarias dadas.