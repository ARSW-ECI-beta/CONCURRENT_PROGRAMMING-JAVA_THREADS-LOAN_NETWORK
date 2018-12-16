
### Escuela Colombiana de Ingeniería
### Arquitecturas de Software - ARSW


##  Laboratorio – Programación concurrente, condiciones de carrera y sincronización de hilos - Caso Red de préstamos



### Descripción
Este laboratorio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente, además de estrategias que eviten condiciones de carrera.

#### Parte I – Antes de terminar la clase.

Control de hilos con wait/notify. Productor/consumidor.

1.  Revise el funcionamiento del programa y ejecútelo. Mientras esto ocurren, ejecute jVisualVM y revise el consumo de CPU del proceso correspondiente. A qué se debe este consumo?, cual es la clase responsable?
2.  Haga los ajustes necesarios para que la solución use más eficientemente la CPU, teniendo en cuenta que -por ahora- la producción es lenta y el consumo es rápido. Verifique con JVisualVM que el consumo de CPU se reduzca.
3.  Haga que ahora el productor produzca muy rápido, y el consumidor consuma lento. Teniendo en cuenta que el productor conoce un límite de Stock (cuantos elementos debería tener, a lo sumo en la cola), haga que dicho límite se respete. Revise el API de la colección usada como cola para ver cómo garantizar que dicho límite no se supere. Verifique que, al poner un límite pequeño para el 'stock', no haya consumo alto de CPU ni errores.

#### Parte II - Red de prestamistas
La 'red de prestamistas' es un modelo de colaboración en el que un grupo de personas se compromete a prestar una suma de dinero (por ahora es una cantidad fija) a cualquier otra persona de su red que se lo solicite (es decir, cada persona de la red podrá tanto pedir prestado y siempre deberá prestar a quien se lo solicite). 
El programa planteado es una simulación de este modelo, en el que N prestamistas, representados por N hilos, concurrente y aleatoriamente solicitan préstamos a otros integrantes de la red. En la simulación todos tienen un saldo inicial de $500 USD, y los prestamos siempre son de $10 USD. Por regla general, quienes lleguen a la bancarrota (saldo 0), ya no podrán solicitar ni realizar más préstamos. El programa automáticamente detiene la simulación cada 10 segundos para mostrar el saldo total de los prestamistas (es decir, la sumatoria de los saldos de todos los integrantes de la red de préstamos).


1. Revise el funcionamiento del simulador, ejecutándolo varias veces a través del comando:

	```java
	mvn clean compile
	mvn exec:java -Dexec.mainClass="edu.eci.arsw.loannetsim.LoanNetworkSimulation" 
	```


2. Analice con el funcionamiento de la aplicación: por qué razón a veces el mensaje de **PRESS ENTER TO VIEW STATISTICS**  se presenta antes de los últimos LOGs de préstamos?. Plantee una solución para esto.
	
	
3. Analice la lógica del programa. Qué valor se debería mostrar cada vez que se pausa la simulación?. Si no es consistente, identifique la región crítica donde se da la condición de carrera que da lugar a este resultado. 
Implemente una estrategia de bloqueo que evite las condiciones de carrera. Recuerde que si usted requiere usar dos o más ‘locks’ simultáneamente, puede usar bloques sincronizados anidados:
	```java
	synchronized(locka){
		synchronized(lockb){
			…
		}
		
	}
	```
4. Ejecute de nuevo, varias veces, el programa, y rectifique que (1) el resultado ahora SÍ sea consistente, y (2) que el mismo NO llegue a un DeadLock. Para poder rectificar si un proceso Java tiene hilos en Deadlock (en caso de que el programa quede interrumpido por fuera del lapso de los 10 segundos), use los comandos jps y jstack.

	```java
	jps
	jstack IDPROCESO
	```

5. Si su solución conduce a un Deadlock, puede revisar de nuevo las páginas 206 y 207 de  _Java Concurrency in Practice_ e implemente una estrategia para evitar esto. De nuevo, rectifique (experimentalmente) tanto la correctitud de la salida como la ausencia de Deadlocks.
6. Un elemento molesto para la simulación es que en cierto punto de la misma hay pocos 'prestamistas' con dinero realizando prestamos fallidos con 'prestamistas' ya quebrados. Es necesario ir suprimiendo los prestamistas muertos de la simulación a medida que van muriendo. Para esto:

-   Analizando el esquema de funcionamiento de la simulación, esto podría crear una condición de carrera? Implemente la funcionalidad, ejecute la simulación y observe qué problema se presenta cuando hay muchos 'prestamistas' en la misma. Escriba sus conclusiones al respecto en el archivo RESPUESTAS.txt.
-   Corrija el problema anterior  **SIN hacer uso de sincronización**, pues volver secuencial el acceso a la lista compartida de prestamistas haría extremadamente lenta la simulación.

