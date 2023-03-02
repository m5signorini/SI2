
Instrucciones
## Previo
1. Asegurar variables de entorno:
   1. export JAVA\_HOME=/opt/jdk1.8.0\_111
   1. export J2EE\_HOME=/opt/glassfish4/glassfish
   1. export PATH=/opt/jdk1.8.0\_111/bin:${PATH}
1. Establecer dirección IP virtual para la comunicación con la VM.
   1. **Laboratorio**
      1. sudo /opt/si2/virtualip.sh enp1s0
   1. Casa
      1. ifconfig
      1. virtualip.sh
1. Crear máquina virtual:
   1. Extraer del tar.gz
   1. sh si2fixMAC.sh 2401 7 1
## Credenciales
1. User y passwovrd
   1. si2
   1. 2022sid0s
## Conexión
1. Desde host usar SSH
   1. ssh si2@10.250.1.58
1. Desde SSH iniciar servidor
   1. asadmin start-domain domain1
   1. asadmin stop-domain domain1
1. Desde SSH quizá se requiera reiniciar postgresql
   1. sudo service postgresql-8.4 restart
1. Desde la carpeta de la aplicación usar Ant para desplegar:
   1. ant replegar limpiar-todo unsetup-db todo
1. Ir al navegador en incógnito para evitar problemas de caché
1. **No abrir Glassfish Admin mientras pagos, y usar testbd.jsp**
##
## Info
0004 9839 0829 3274

Blas Avila Sparrow

10/10

04/23

227


0028 1652 2262 7263

Clodoveo Moss Cozar

05/08

08/23

080
## P3 Info
0001 7771 6822 7061

Kate Poza Coll

10/10

05/24

520
## Referencias
<https://github.com/erpheus/SI2>

<https://github.com/OscarGB/SI2>


FALLO TARJETA NO AUTORIZADA CUANDO LA BD ESTA BN (+-)

1. replegar todo.
1. asadmin stop-domain domain1
1. asadmin start-domain domain1
1. ant limpiar-todo unsetup-db todo
1. PROBAR TODO Y FUNCIONA.
## Acceso a PgAdmin
1. Create server
1. connection > host: ip de bridge
1. connection > username/password: alumnodb




**Paso 1:** Descargar si2srv descomprimir-

**Paso 2:** Randomizar mac’s en configuración antes de ejecutar máquina virtual: 

sh si2fixMAC.sh 2401 6 2 

**Paso 3:** Ejecutar y configurar máquina virtual, pareja 6

**Paso 5:** en el pc del laboratorio: export J2EE\_HOME=/opt/glassfish4/glassfish

**Paso 6:** en la maquina virtual: export J2EE\_HOME=/opt/glassfish4.1.2/glassfish

**Paso 7:**  ¡No cambiar de terminal!

export J2EE\_HOME=/opt/glassfish4/glassfish/

export PATH=/opt/jdk1.8.0\_111/bin:${PATH}

export JAVA\_HOME=/opt/jdk1.8.0\_111

**Paso 8:** Cambiar en codigo ips a 10.250.1.56

**AÑADIR ATRIBUTO BASE DE DATOS DE PAGOS (ver diapositiva 10 presntacion p3)**

**primero modificamos el objeto pago (pagobean)**

1. añadir  campos create.sql
1. añadir al bean adecuado en src/ssii2/visa
   1. atributos privados
   1. setters & getters
1. añadir en ComienzaPago.java y ProcesaPago.java en la función creaPago la llamada a los setters (introducir datos)

**después modificamos para que se inserte correctamente en la base de datos con los nuevos campos**

1. en VisaDAO 
   1. añadir atributos en la query privada estática del INSERT (direct\_connection)
   1. añadir atributos en la función privada del INSERT (prepared\_statement)
   1. en función realizaPago pasar los parametros al prepared statement

**EN CLASE HACER:**

export J2EE\_HOME=/opt/glassfish4/glassfish/

export PATH=/opt/jdk1.8.0\_111/bin:${PATH}

export JAVA\_HOME=/opt/jdk1.8.0\_111

**EN CASA HACER:**

export AS\_JAVA=/usr/lib/jvm/java-8-openjdk-amd64

export PATH=/opt/glassfish4/glassfish/bin:${PATH}

export J2EE\_HOME=/opt/glassfish4/glassfish

sudo sh ./virtualip.sh wlo1

sudo sh ./virtualip.sh wlp5s0

Iniciar maquina virtual y ejecutar:

asadmin start-domain domain1

En P1-base ejecutar, cuando el servidor haya sido started:

sudo ant replegar limpiar-todo unsetup-db todo

sudo ant replegar-servicio compilar-servicio empaquetar-servicio desplegar-servicio

Si fallan cosas, usar en la VM:

asadmin stop-domain domain1

sudo service postgresql-8.4 restart

asadmin start-domain domain1

(Cerrar pgadmin)

Después volver a  ejecutar fuera VM:

sudo ant replegar limpiar-todo unsetup-db todo

admin glasfish puerto 4848: usuario: admin ; contraseña: adminadmin




**CONSULTAR DATOS EN PSQL SOBRE EL PAGO**

psql -U alumnodb -d visa

SELECT \* FROM pago;

## Tocar
find . -type f -exec touch {} +

– Sacerdote Jose Jorro, 2022

“

Los del grupo 2311 hemos tenido el examen de las prácticas de SI2 esta mañana, os explico lo que nos ha entrado para quien le interese

Eran 3 ejercicios, y la puntuación era 5 ptos el ejercicio en el que saques más nota y 2.5 ptos los otros dos. En el examen te dejan usar internet y, a nosotros por lo menos, nos han dejado llegar antes al laboratorio y tener las máquinas ya preparadas con todo desplegado antes de que empezara el examen.

El primero era modificar el P1-ws (desplegando cliente y servidor en la misma máquina)

Había que modificar el testbd.jsp para que en el apartado getPagos incluyese 2 campos que representaran el importe mínimo y el importe máximo y luego cambiar getPagos.java y VisaDAOWS.java para que devolvieran los pagos cuyo importe está dentro de ese rango. Después había que responder una pregunta sobre un fichero de WSDL pero no he entendido ni a qué se referían.

El ejercicio 2 era como el ejercicio 7 del .zip que mandó César y viene muy bien explicado en un apartado del enunciado de la práctica 2.

El ejercicio 3 era teoría sobre afinidad de sesión, te ponían un par de capturas de un balanceador de carga y tenías que explicar por qué había ocurrido un problema de afinidad de sesión.

“ – Quintanilla, 2022.

**Pasos (añadir campos en búsquedas de pagos):**

1. Modificar testbd.jsp
   1. Utilizar como referencia importe/idComercio
1. Modificar GetPagos.java
   1. en processRequest:
      1. añadir request.getParameter(“minImporte”)
      1. añadir request.getParameter(“maxImporte”)
      1. pasarlos a la llamada a getPagos(idComercio, **min**, **max**)
1. Modificar VisaDAOWS.java
   1. en SELECT\_PAGOS\_QRY
      1. SQL modificado
   1. en getPagos
      1. Añadir parámetros
      1. settear strings para el ptsmt
         1. Recuerda el orden de los parámetros
         1. Sigue ejemplo de idComercio

**CUIDADO: TIPOS DEL PTSMT.SET(DOUBLE/STRING/…)**
