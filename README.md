cc8-lab3
===================

Chaman Web Server.

----------

Questions
-------------

 - **¿Qué sucede desde el punto de vista del usuario cuando se intenta abrir más conexiones que las disponibles en su webserver?**
 
Se rechaza el intento de conexión, pues el Executor no tiene permisos de crear más Threads de los indicados.

- **Cree una tabla comparativa del rendimiento (thread vs tiempo) al utilizar un Threadpool  de 1 a 100 threads  y  Grafique los resultados.**

Ver [regresión](/regression.pdf)
 
- **¿El tiempo de respuesta decrese considerablemente con la cantidad de Threads?**

75 threads

 - **¿En que punto la cantidad de Thread ya no es significante?**
  
Por la prueba que se hizo se nota que al principio no es tan influyente la cantidad de Threads, pues los requests son demasiados para el pool.


----------
Galileo University
Computer Science 2016

