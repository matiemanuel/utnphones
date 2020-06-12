# utnphones
TP INTEGRADOR BASE DE DATOS/ PROGRAMACIÓN AVANZADA 
UTN Phones ​es una compañia de telefonia celular con base en Mar del Plata, y quiereempezar a operar con nuevos sistemas.
Con este fin se nos explica el siguiente escenario :
●En nuestra compañía, un cliente es un individuo que posee 1 o mas lineas detelefono. Cada uno de estas líneas puede realizar llamadas a cualquier linea detelefono de nuestra propia compañía. No hay forma de realizar una llamada a otracompañía.
●Los precios de cada llamada se va a calcular dependiendo la localidad de origen yla de destino. Los numeros seran guardados siempre con el prefijo (Ej : 4552394de Mar del Plata será guardado como 2234552394) y se debe contar con la tarifacorrespondiente a las llamadas de la misma ciudad. ​ Por ejemplo : ​Tarifa desdeMDQ a MDQ es de $10 por minuto , MDQ a MIR $5 el minuto. Idealmente almomento de inserción de la llamada se debe calcular la localidad de origen y la dedestino y aplicar la lógica para calcular su precio. Cada localidad tiene un prefijoque la identifica y que nos permitirá saber el precio por minuto de la llamada.
●Hay un proceso automático que todos los 1º de cada mes, corre automáticamentey verifica las llamadas no facturadas y genera una nueva factura con unvencimiento a 15 días. Debemos guardar solo el estado de si la factura esta pagao no.
●Se hace una factura por cada línea registrada en el sistema.
●Cada llamada debe pertenecer a solo una factura.
La información de los empleados y clientes debe ser la siguiente :
  ●Nombre
  ●Apellido
  ●DNI
  ●Ciudad
  ●Provincia
Cada línea tiene los siguientes datos :
  ●Numero de linea (numero de telefono).
  ●Tipo de usuario (móvil o residencial).Las llamadas incluyen :
  ●Numero de telefono origen●Número de teléfono destino
  ●Localidad de origen
  ●Localidad de destino
  ●Precio por minuto
  ●Duración (en segundos)
  ●Precio total
  ●Factura donde está incluida esta llamada.
Las tarifas incluyen :
  ●Ciudad de origen
  ●Ciudad de destino
  ●Precio por minuto
  (*) La ciudad de origen y de destino serán iguales para llamadas locales
Las facturas deben tener :
  ●Línea
  ●Cliente
  ●Cantidad de llamadas
  ●Precio costo
  ●Precio total
  ●Fecha
  ●Fecha de vencimiento
  
 PROGRAMACIÓN AVANZADA I​
 UTN Phones ​ha contratado los servicios de dos empresas expertas en la realización deplataformas web. Con el fin de distribuir la carga de trabajo entre las dos empresas, unade ellas se encargará del desarrollo Web y Aplicación Celular, mientras a nuestraorganización se encargará del desarrollo de una API REST para el mantenimiento de laslíneas (Llámese desde ahora ​Backoffice​, para permitir a los empleados poder manejarlos datos de clientes, facturación, suspensión de líneas ,etc) y también para dar soporteal ​portal de usuarios web y Aplicación Android​. Se debe generar también lainfraestructura de base de datos necesaria para guardar la información. ​La base dedatos será escogida por el alumno según considere.
 ● El portal de usuarios y aplicacion Android deberá permitir :
    1)Login de clientes
    2)Consulta de llamadas del usuario logueado por rango de fechas.
    3)Consulta de facturas del usuario logueado por rango de fechas.
    4)Consulta de destinos más llamados por el usuario.
 ●Desde el sistema de ​Backoffice​, se debe permitir :
    1)Login de empleados.
    2)Manejo de clientes.
    3)Alta , baja  y suspensiónde líneas.
    4)Consulta de tarifas.
    5)Consulta de llamadaspor usuario.
    6)Consulta de facturación .La facturación se harádirectamente por un proceso interno en la base  datos.
 ●Se debe permitir también el agregado de llamadas, con un login especial, ya queeste método de nuestra API será llamado nada más que por el área deinfraestructura cada vez que se produzca una llamada.  El área de infraestructurasólo enviará la siguiente información de llamadas :
    ○Número de origen
    ○Número de destino
    ○Duración de la llamada
    La tarifa y las localidades de destino deberán calcularse al momento de guardar lallamada y no será recibido por la API REST.
Para la completitud del TP se considera :
●Seguir los fundamentos de API REST.
●División en capas mostradas en clase (Controller, Service, Persistence/Dao)
●Unit tests con cobertura de al menos 70% del código.
El no cumplimiento de alguno de los requisitos significa la desaprobación del TP con laconsiguiente desaprobación de la materia.Fechas de entrega :​  23/06 - 25/06 - 30/06 - 02/07



BASE DE DATOS II
Se debe generar un ​diseño de base de datos relacional ​ que permita dar soporte alescenario planteado anteriormente, y a su vez todo el trabajo que se requiera paracumplimentar lo siguiente :
  1)Generar las estructuras necesarias para dar soporte a 4 sistemas diferentes :
      a)BACKOFFICE, que permitirá el manejo de clientes, líneas y tarifas.
      b)CLIENTES, que permitirá consultas de llamadas y facturación.
      c)INFRAESTRUCTURA , que será el sistema que enviará la información dellamadas a la base de datos.
      d)FACTURACIÓN , proceso automático de facturación.
  2)La facturación se realizará por un proceso automático en la base de datos. Sedebe programar este proceso para el primer día de cada mes y debe generar unafactura por línea y debe tomar en cuenta ​todas las llamadas no facturadaspara cada una de las líneas, sin tener en cuenta su fecha. ​La fecha devencimiento de esta factura será estipulada a 15 días.
  3)Generar las estructuras necesarias para el cálculo de precio de llamadas y laslocalidades ( Ciudad de origen y ciudad de destino) al momento de inserción dellamadas, ya que solo se reciben los números de origen y destino , la fecha y laduración de la llamada.
  4)Generar las estructuras necesarias para dar soporte a las consultas de llamadaspor fecha y por usuario , debido a que tenemos restricción de que estas nopueden demorar más de dos segundos y tenemos previsto que tendremos500.000.000 de llamadas en el sistema en el mediano plazo. Este reporte incluirá:
      i)Número de origen
      ii)Ciudad de origen
      iii)Número de destino
      iv)Ciudad de destino
      v)Precio total
      vi)Duración
      vii)Fecha y hora de llamada.
  5)Como PLAN B , generar una estructura de base de datos NoSQL de su preferencia para dar soporte al problema planteado.
  Fechas de entrega :​  23/06 - 25/06 - 30/06 - 02/07
