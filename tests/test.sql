-- 
--  CUENTA
--

INSERT INTO cuenta VALUES ('C0001', 'Juan', 'Gomez', 'abc123', 'juan@law.com', 'abogado','555-0001','calle 11');
INSERT INTO cuenta VALUES ('C0002', 'Maria', 'Ramos', 'qwerty', 'maria@acc.com', 'contadora','555-0010','calle 12');

--
-- CLASE
--

INSERT INTO clase VALUES ('BIZ', 'Clase ejecutiva');
INSERT INTO clase VALUES ('PRI', 'Clase primera');
INSERT INTO clase VALUES ('TUR', 'Clase turista');


--
-- RESERVA
--

INSERT INTO reserva VALUES ('R0000001', 'January 5, 2013', 3, 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva VALUES ('R0000002', 'January 5, 2013', 3, 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'PRI');
INSERT INTO reserva VALUES ('R0000003', 'January 5, 2013', 4, 'confirmado', 'efectivo', 'C0002', 'calle 13', 'BIZ');
INSERT INTO reserva VALUES ('R0000004', 'January 10, 2013', 4, 'pendiente', 'efectivo', 'C0002', 'calle 13', 'BIZ');
INSERT INTO reserva VALUES ('R0000005', 'January 15, 2013', 1, 'pendiente', 'tarjeta', 'C0002', 'calle 13', 'BIZ');
INSERT INTO reserva VALUES ('R0000006', 'February 3, 2013', 2, 'confirmado', 'tarjeta', 'C0002', 'calle 13', 'PRI');

-- 
-- PAIS
--

INSERT INTO pais VALUES ('AR', 'Argentina');
INSERT INTO pais VALUES ('BR', 'Brasil');
INSERT INTO pais VALUES ('CO', 'Colombia');
INSERT INTO pais VALUES ('MX', 'México');
INSERT INTO pais VALUES ('ES', 'España');

-- 
-- PERSONA
--

INSERT INTO persona VALUES (1, 37000000, 'Cristian', 'Gomez', 'December 15, 1996', 'AR');
INSERT INTO persona VALUES (1, 38000000, 'Mariela', 'Gomez', 'October 15, 1997', 'AR');
INSERT INTO persona VALUES (1, 19000000, 'Juan', 'Gomez', 'March 15, 1966', 'AR');
INSERT INTO persona VALUES (1, 20000000, 'Ernestina', 'Quintana', 'May 15, 1967', 'AR');

INSERT INTO persona VALUES (1, 28000000, 'Ramos', 'Maria', 'December 27, 1979', 'AR');
INSERT INTO persona VALUES (1, 26000000, 'Andres', 'Diaz', 'October 15, 1975', 'AR');
INSERT INTO persona VALUES (1, 28000001, 'Ramos', 'Maria', 'December 27, 1979', 'AR');
INSERT INTO persona VALUES (1, 26000001, 'Andres', 'Diaz', 'October 15, 1975', 'AR');

INSERT INTO persona VALUES (2, 1000000, 'Rosa', 'Garcia', 'June 27, 1940', 'ES');


-- 
-- PERSONA RESERVA
--

INSERT INTO persona_reserva VALUES ('R0000001', 1, 37000000);
INSERT INTO persona_reserva VALUES ('R0000001', 1, 38000000);
INSERT INTO persona_reserva VALUES ('R0000002', 1, 19000000);
INSERT INTO persona_reserva VALUES ('R0000002', 1, 20000000);

INSERT INTO persona_reserva VALUES ('R0000003', 1, 28000000);
INSERT INTO persona_reserva VALUES ('R0000003', 1, 26000000);
INSERT INTO persona_reserva VALUES ('R0000004', 1, 28000001);
INSERT INTO persona_reserva VALUES ('R0000004', 1, 26000001);

INSERT INTO persona_reserva VALUES ('R0000005', 2, 1000000);
INSERT INTO persona_reserva VALUES ('R0000005', 1, 26000000);


--
-- AEROPUERTO
-- 

INSERT INTO aeropuerto VALUES ('001', 'Aeropuerto de Ezeiza', 200.0, 'foo..', 'AR');
INSERT INTO aeropuerto VALUES ('002', 'Aeropuerto de São Paulo', 300.0, 'foo..', 'BR');
INSERT INTO aeropuerto VALUES ('005', 'Aeropuerto de Bogotá', 240.0, 'foo..', 'CO');
INSERT INTO aeropuerto VALUES ('009', 'Aeropuerto de ciudad de México', 240.0, 'foo..', 'MX');


--
-- VUELO
-- 

INSERT INTO vuelo VALUES ( 1, '001', '002', '10:10:00', '3 hours', 1400); -- BSAS --> SAO PAULO
INSERT INTO vuelo VALUES ( 2, '001', '002', '18:10:00', '3 hours', 1400); -- BSAS --> SAO PAULO
INSERT INTO vuelo VALUES ( 3, '001', '005', '16:10:00', '8 hours', 2900); -- BSAS --> BOGOTA
INSERT INTO vuelo VALUES ( 4, '002', '005', '01:10:00', '7 hours', 2600); -- SAO PAULO --> BOGOTA
INSERT INTO vuelo VALUES ( 5, '005', '009', '10:10:00', '4 hours', 1900); -- BOGOTA --> MEX DF

--
-- PRECIO
--
INSERT INTO precio VALUES (1,'TUR',100);
INSERT INTO precio VALUES (1,'PRI',200);
INSERT INTO precio VALUES (1,'BIZ',300);

INSERT INTO precio VALUES (2,'TUR',400);
INSERT INTO precio VALUES (2,'PRI',500);
INSERT INTO precio VALUES (2,'BIZ',600);

INSERT INTO precio VALUES (3,'TUR',700);
INSERT INTO precio VALUES (3,'PRI',800);
INSERT INTO precio VALUES (3,'BIZ',900);

INSERT INTO precio VALUES (4,'TUR',1000);
INSERT INTO precio VALUES (4,'PRI',1100);
INSERT INTO precio VALUES (4,'BIZ',1200);

INSERT INTO precio VALUES (5,'TUR',1300);
INSERT INTO precio VALUES (5,'PRI',1400);
INSERT INTO precio VALUES (5,'BIZ',1500);

--
-- VIAJE
-- 

INSERT INTO viaje VALUES ( 1, 'January 17, 2013');	-- BS --> SAO PAULO

INSERT INTO viaje VALUES ( 2, 'January 17, 2013');	-- BS AS --> SAO PAULO
INSERT INTO viaje VALUES ( 2, 'January 18, 2013');	-- BS AS --> SAO PAULO
INSERT INTO viaje VALUES ( 4, 'January 31, 2013');	-- SAO PAULO --> BOGOTA
INSERT INTO viaje VALUES ( 5, 'February 18, 2013');	-- BOGOTA --> MEX DF

INSERT INTO viaje VALUES ( 3, 'February 15, 2013');	-- BS AS --> BOGOTA


-- 
-- RESERVA VIAJE
--

INSERT INTO reserva_viaje VALUES ('R0000001', 1, 'January 17, 2013', 1);

INSERT INTO reserva_viaje VALUES ('R0000002', 1, 'January 17, 2013', 1);

INSERT INTO reserva_viaje VALUES ('R0000003', 2, 'January 17, 2013', 1);  --
INSERT INTO reserva_viaje VALUES ('R0000003', 4, 'January 31, 2013', 2);  -- Los dos primeros viajes con una reserva

INSERT INTO reserva_viaje VALUES ('R0000004', 5, 'February 18, 2013', 1); -- El tercer viaje, con una reserva aparte

INSERT INTO reserva_viaje VALUES ('R0000005', 5, 'February 18, 2013', 1);
