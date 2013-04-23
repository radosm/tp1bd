--
-- OJO! Correr antes tests/test.sql
--

\echo
\echo Reservas actuales:
\echo ==================
\echo
select * from vw_datos_reserva;

begin;
INSERT INTO reserva VALUES ('R0000099', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000099', 1, 'January 17, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000098', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000098', 5, 'February 18, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000097', 'January 18, 2012', 'pendiente', 'tarjeta', 'C0002', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000097', 2, 'January 18, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000096', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000096', 1, 'January 17, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000095', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000095', 5, 'February 18, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000094', 'January 18, 2012', 'pendiente', 'tarjeta', 'C0002', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000094', 2, 'January 18, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000093', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000093', 1, 'January 17, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000092', 'January 5, 2013', 'pendiente', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000092', 5, 'February 18, 2013', 1);
commit;

begin;
INSERT INTO reserva VALUES ('R0000091', 'January 18, 2012', 'pendiente', 'tarjeta', 'C0002', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000091', 2, 'January 18, 2013', 1);
commit;

\echo
\echo Reservas como quedaron
\echo ======================
\echo
select * from vw_datos_reserva;
