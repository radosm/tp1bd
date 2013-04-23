--
-- OJO! Correr antes tests/test.sql
--

\echo
\echo Reservas actuales:
\echo ==================
\echo
select * from vw_datos_reserva;

--
-- 3ra para iguales fechas y aeropuertos=>falla
--
begin;
INSERT INTO reserva VALUES ('R0000099', 'January 5, 2013', 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000099', 1, 'January 17, 2013', 1);
commit;

--
-- Anda ok
--
begin;
INSERT INTO reserva VALUES ('R0000098', 'January 5, 2013', 'confirmado', 'tarjeta', 'C0001', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000098', 5, 'February 18, 2013', 1);
commit;

--
-- Solapada=>falla
--
begin;
INSERT INTO reserva VALUES ('R0000097', 'January 18, 2012', 'confirmado', 'tarjeta', 'C0002', 'calle 13', 'TUR');
INSERT INTO reserva_viaje VALUES ('R0000097', 2, 'January 18, 2013', 1);
commit;

\echo
\echo Reservas como quedaron
\echo ======================
\echo
select * from vw_datos_reserva;
