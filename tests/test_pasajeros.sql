
--para correr conviene primero vaciar todo corriendo creadb
-- psql -d postgres -f creadb.sql
-- psql -d postgres -f test/test_pasajeros.sql
-- psql -d postgres -f pasajeros.sql

-- 
--  CUENTA
--

INSERT INTO cuenta VALUES ('C0001', 'Juan', 'Gomez', 'abc123', 'juan@law.com', 'abogado','555-0001','calle 11');
INSERT INTO cuenta VALUES ('C0009', 'Fernando', 'Gomez', 'abc124', 'fer@law.com', 'abogado','555-0002','calle 12');
INSERT INTO cuenta VALUES ('C0010', 'Mauricio', 'Gomez', 'abc125', 'mau@law.com', 'abogado','555-0003','calle 13');

--
-- CLASE
--

INSERT INTO clase VALUES ('BIZ', 'Clase ejecutiva');
INSERT INTO clase VALUES ('PRI', 'Clase primera');
INSERT INTO clase VALUES ('TUR', 'Clase turista');

--
-- RESERVA
--

INSERT INTO reserva VALUES ('R0000001', 'January 5, 2013', 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva VALUES ('R0000002', 'January 5, 2010', 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva VALUES ('R0000003', 'January 5, 1993', 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');

-- 
-- PERSONA
--

INSERT INTO persona VALUES (1, 19000000, 'Juan', 'Gomez', 'March 15, 1966', 'Argentino');
INSERT INTO persona VALUES (1, 30000000, 'Fernando', 'Gomez', 'March 15, 1964', 'Argentino');
INSERT INTO persona VALUES (1, 34213223, 'Mauricio', 'Gomez', 'March 15, 1980', 'Burkina Faso');

-- 
-- PERSONA RESERVA
--

INSERT INTO persona_reserva VALUES ('R0000001', 1, 19000000);
INSERT INTO persona_reserva VALUES ('R0000002', 1, 30000000);
INSERT INTO persona_reserva VALUES ('R0000003', 1, 34213223);

-- 
-- PAIS
--

INSERT INTO pais VALUES ('01', 'Argentina');
INSERT INTO pais VALUES ('02', 'Brasil');

--
-- AEROPUERTO
-- 

INSERT INTO aeropuerto VALUES ('001', 'Aeropuerto de Ezeiza', 200.0, 'foo..', '01');
INSERT INTO aeropuerto VALUES ('002', 'Aeropuerto de São Paulo', 300.0, 'foo..', '02');

--
-- VUELO
-- 

INSERT INTO vuelo VALUES ( 1, '001', '002', '10:10:00', '3 hours', 1400); -- BSAS --> SAO PAULO

--
-- PRECIO
--
INSERT INTO precio VALUES (1,'TUR',100);
INSERT INTO precio VALUES (1,'PRI',200);
INSERT INTO precio VALUES (1,'BIZ',300);

--
-- VIAJE
-- 

INSERT INTO viaje VALUES ( 1, 'January 17, 2013');	-- BS --> SAO PAULO
INSERT INTO viaje VALUES ( 1, 'January 17, 2010');	-- BS --> SAO PAULO
INSERT INTO viaje VALUES ( 1, 'January 17, 1993');	-- BS --> SAO PAULO viejo

-- 
-- RESERVA VIAJE
--

INSERT INTO reserva_viaje VALUES ('R0000001', 1, 'January 17, 2013', 1);
INSERT INTO reserva_viaje VALUES ('R0000002', 1, 'January 17, 2010', 1);
INSERT INTO reserva_viaje VALUES ('R0000003', 1, 'January 17, 1993', 1);
