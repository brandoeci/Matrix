# Matrix Game

Juego de consola basado en la película Matrix, implementado en Java con hilos.

## Descripción

El juego consiste en un tablero cuadrado generado aleatoriamente con los siguientes personajes:

- **N** - Neo: debe llegar a un teléfono antes de ser atrapado
- **A** - Agentes: persiguen a Neo para atraparlo
- **T** - Teléfono: objetivo de Neo
- **#** - Paredes: bloquean el paso
- **.** - Casilla vacía

## Cómo ejecutar

1. Clonar el repositorio
2. Abrir el proyecto en IntelliJ IDEA
3. Ejecutar la clase `Main.java`
4. Ingresar los parámetros solicitados:
   - Tamaño del tablero (2 a 7)
   - Cantidad de agentes
   - Cantidad de teléfonos
   - Delay entre turnos en ms

## Implementación con hilos

Cada personaje corre en su propio hilo:

- `NeoThread` - hilo de Neo
- `AgentThread` - hilo de cada agente

Para sincronizar los turnos se usa un `CyclicBarrier`. El hilo principal suelta la barrera, todos los hilos se mueven, cuando todos terminan se encuentran en una segunda barrera y el hilo principal imprime el tablero.

Los métodos `moveNeo` y `moveAgent` están marcados como `synchronized` para evitar condiciones de carrera. El estado del juego se maneja con una variable `volatile` en `GameState`.

## Inteligencia de los personajes

- **Neo** usa BFS para encontrar el teléfono donde tiene más ventaja sobre el agente más cercano
- **Agentes** usan BFS para perseguir a Neo en cada turno


## Pantallazos

### Inicio del juego
<img width="271" height="61" alt="image" src="https://github.com/user-attachments/assets/919be2a6-d261-4747-9c02-a767800145e9" />

### Durante el juego
<img width="474" height="179" alt="image" src="https://github.com/user-attachments/assets/e25557f5-9b12-4d7c-b5c2-e81dcb3e5d66" />

### Neo gana
<img width="379" height="212" alt="image" src="https://github.com/user-attachments/assets/dae013e5-eed0-40c4-be7a-be4cc058fd9c" />

### Agentes ganan
<img width="372" height="210" alt="image" src="https://github.com/user-attachments/assets/7049050b-eb37-475d-9a28-1efe8f2506e8" />

## Condiciones de victoria

- **Neo gana** si llega a un teléfono
- **Agentes ganan** si atrapan a Neo

## Tecnologías

- Java 17
- Maven
- IntelliJ IDEA
