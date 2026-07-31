/***********************************************************************************************/
/* Zuerst muss der SysAdmin manuell angelegt werden - erst danach kann die App getartet werden */
/* für andere DB-Namen / Subdomains muss die E-Mail Subdomain angepasst werden!                */


insert into members
(member_nmbr,      first_name, last_name,    birthdate, street, number, zip, city, email,                      is_imported_member, is_system_account, created_by_id, version, joining_date) values
(          1, 'Administrator',  'System', '2000-01-01', '-',    '-',    '-',  '-', 'sysadmin.ma@nabahilfe.eu', TRUE,               TRUE,              1,             0,      '2020-01-01');


/* prüfen ob alles geklappt hat und ob die created_by_id der id entspricht */
/***************************************************************************/


/* Testdaten
insert into members
(member_nmbr,   first_name, last_name,    birthdate, street, number, zip, city, email,              is_imported_member, is_system_account, created_by_id, version, joining_date) values
(       2001,   'Inserter',  'Member', '2000-01-01', '-',    '-',    '-',  '-', 'test332@test.com', TRUE,               TRUE,              101,           0,      '2020-01-01');
*/



/************ Jetzt App starten, die legt die Rollen, Offers und Sozialkonto an *************/


/* Daten für ROLES */
/* werden automatisch angelegt */

/*
INSERT INTO roles (is_board_member,  is_treasurer, is_secretary, is_auditor, is_time_keeper, is_admin, is_miscellaneous, role_name,          version) VALUES
                    (true,             false,          false,       false,      false,       true,       false,          'Obmann',              0),
                    (true,             false,          false,       false,      false,       true,       false,          'Obmann Stv.',         0),
                    (true,             false,          false,       false,      true,        false,      false,          'Obfrau',              0),
                    (true,             false,          false,       false,      true,        false,      false,          'Obfrau Stv.',         0),
                    (false,            true,           false,       false,      false,       false,      false,          'Kassier',             0),
                    (false,            true,           false,       false,      false,       false,      false,          'Kassier Stv.',        0),
                    (false,            false,          false,       true,       false,       false,      false,          'Rechnungsprüfer',     0),
                    (false,            false,          false,       true,       false,       false,      false,          'Rechnungsprüfer Stv.',0),
                    (false,            false,          true,        false,      false,       true,       false,          'Schriftührer',        0),
                    (false,            false,          true,        false,      false,       true,       false,          'Schriftührer Stv.',   0),
                    (false,            false,          false,       false,      false,       false,      true,           'Ehrenmitglied',       0);
*/



/* Daten für Offers / Tätigkeiten */
/* werden automatisch angelegt */

/*
insert into offers (code, description, version) values
('100','Erfahrungsaustausch und Gespräche', 0),
('200','Alltägliche Hilfsdienste', 0),
('300','Initiieren und Organisieren von Freizeitaktivitäten', 0),
('400','Unterstützung bei Formularen sowie Behördenkontakten', 0),
('500','Transport und Fahrtendienste', 0),
('600','Leih-Oma / Leih-Opa', 0),
('700','Kleinere Außen- oder Reparaturarbeiten', 0),
('800','Hilfe beim Bedienen technischer Geräte und Computer', 0),
('900','Sonstiges - bitte Beschreibung angeben!', 0),
('950','Spende von Stunden', 0),
('999','Korrekturbuchung', 0);
*/



/*  Echt - Mitglieder Import */

/*

  Optionale Felder sind: title, institution, email, phone_number.

  Alle Felder müssen einen gültigen Wert haben, das gilt vor allem für:

  - member_nmbr: darf nicht kleiner als 1000 sein
  - salutation: 'Herr' oder 'Frau' oder 'Divers' oder '' (unbekannt)
  - joining_date: ISO Format, also 'YYYY-MM-DD'
  - phone_number (wenn vorhanden):
    '+43699123456' oder
    '0699123456'
  - direct_debit_authorization:
    'TRUE' wenn ein Einziehungsauftrag vorhanden ist 'FALSE' wenn keiner vorhanden ist
  - version muss immer '0' sein

  WICHTIG: email Adressen müssen eindeutig sein! Also jedes Mitglied muss (so vorhanden) einen individuelle E-Mail Adresse haben!

  WICHTIG: Die Datei Zeichensatz-Codierung muss UTF-8 sein, und nicht irgendein Windows Code!

*/


insert into members (member_nmbr,  salutation, title, first_name, last_name, institution, birthdate, street, number, zip, city, email, phone_number, direct_debit_authorization, accumulated_hours, joining_date, version) values
(3001, 'Herr', '',    'Thomas',    'Huber',  '',                       '1978-11-02', 'Hauptstraße',  '7', '4020', 'Linz',          'test@test.com',  '0699 19998080', 'TRUE',  '3', '2018-03-01', '0'),
(3002, 'Frau', 'Dr.', 'Katharina', 'Gruber', 'Gemeinde Maria Anzbach', '1992-06-21', 'Kirchengasse', '3', '3034', 'Maria Anzbach', '',               '072818993',     'FALSE', '0', '2018-06-01', '0');



/*  Mitglieder Testdaten */

insert into members (member_nmbr, first_name, last_name, birthdate, street, number, zip, city, version, joining_date) values
(1001, 'Thomas','Huber','1978-11-02','Hauptstraße','7','4020','Linz', 0, '2018-03-01'),
(1002, 'Katharina','Gruber','1992-06-21','Kirchengasse','3','8010','Graz', 0, '2018-06-01'),
(1003, 'Markus','Pichler','1980-01-18','Schillerstraße','25','5020','Salzburg', 0, '2018-09-01'),
(1004, 'Julia','Bauer','1995-09-09','Mozartgasse','8','5020','Salzburg', 0, '2018-12-01'),
(1005, 'Andreas','Steiner','1973-04-30','Ringstraße','44','1010','Wien', 0, '2019-02-01'),
(1006, 'Verena','Hofer','1988-12-12','Waldweg','6','3100','St. Pölten', 0, '2019-04-01'),
(1007, 'Lukas','Leitner','1999-07-05','Sonnenweg','15','4600','Wels', 0, '2019-07-01'),
(1008, 'Sabine','Koller','1982-02-27','Feldstraße','19','3500','Krems', 0, '2019-10-01'),
(1009, 'Daniel','Mayer','1990-10-10','Berggasse','4','6020','Innsbruck', 0, '2020-01-01'),
(1010, 'Claudia','Fuchs','1976-08-03','Dorfstraße','22','6845','Hohenems', 0, '2020-03-01'),
(1011, 'Michael','Wagner','1984-05-16','Poststraße','11','7000','Eisenstadt', 0, '2020-06-01'),
(1012, 'Petra','Schmid','1993-03-08','Parkallee','9','2340','Mödling', 0, '2020-09-01'),
(1013, 'Stefan','Brunner','1987-11-25','Industriestraße','31','2700','Wiener Neustadt', 0, '2020-12-01'),
(1014, 'Melanie','Lang','2000-01-14','Tulpenweg','5','2000','Stockerau', 0, '2021-02-01'),
(1015, 'Florian','Eder','1991-06-19','Lindenstraße','17','4910','Ried im Innkreis', 0, '2021-04-01'),
(1016, 'Birgit','Haas','1979-09-01','Seestraße','2','4800','Attnang-Puchheim', 0, '2021-07-01'),
(1017, 'Patrick','Schwarz','1996-04-23','Amselweg','13','2230','Gänserndorf', 0, '2021-10-01'),
(1018, 'Sandra','Reiter','1983-12-07','Rosenweg','10','3430','Tulln', 0, '2022-01-01'),
(1019, 'Martin','Neubauer','1975-07-29','Bahngasse','27','2500','Baden', 0, '2022-03-01'),
(1020, 'Nina','Wolf','1998-02-11','Ahornstraße','18','4400','Steyr', 0, '2022-06-01'),
(1021, 'Georg','Maier','1969-10-05','Schulstraße','1','8750','Judenburg', 0, '2022-09-01'),
(1022, 'Lisa','Egger','2001-06-30','Alpenweg','20','9900','Lienz', 0, '2022-12-01'),
(1023, 'Bernhard','Ortner','1986-01-09','Marktplatz','6','6370','Kitzbühel', 0, '2023-02-01'),
(1024, 'Eva','Zeller','1994-08-18','Mühlgasse','14','6800','Feldkirch', 0, '2023-04-01'),
(1025, 'Harald','König','1972-05-22','Gartenstraße','23','2232','Deutsch-Wagram', 0, '2023-07-01'),
(1026, 'Tanja','Brandner','1989-09-15','Wiesenweg','7','5162','Obertrum', 0, '2023-10-01'),
(1027, 'Christian','Riedl','1997-12-03','Höhenstraße','35','4850','Timelkam', 0, '2024-01-01'),
(1028, 'Marlene','Auer','2002-03-26','Birkenweg','9','4710','Grieskirchen', 0, '2024-03-01'),
(1029, 'Robert','Weiss','1981-07-07','Fliederweg','16','7083','Purbach', 0, '2024-06-01'),
(1030, 'Sophie','Lackner','1999-11-20','Hirtenweg','12','8600','Bruck an der Mur', 0, '2024-09-01'),
(1031, 'Alexander','Kastner','1985-04-02','Waldstraße','29','3943','Schrems', 0, '2024-12-01'),
(1032, 'Nicole','Dorner','1993-06-14','Rebenweg','3','3493','Kamptal', 0, '2025-02-01'),
(1033, 'Manuel','Seidl','2003-10-09','Panoramastraße','21','5580','Tamsweg', 0, '2025-04-01'),
(1034, 'Daniela','Pfeiffer','1977-02-17','Lärchenweg','8','6130','Schwaz', 0, '2025-07-01'),
(1035, 'Simon','Moser','1990-05-05','Heideweg','11','8430','Leibnitz', 0, '2025-10-01'),
(1036, 'Karin','Winter','1984-12-28','Eichenweg','6','9800','Spittal an der Drau', 0, '2026-01-01'),
(1037, 'Dominik','Holzer','1996-08-04','Siedlungsstraße','19','4880','St. Georgen', 0, '2026-02-01'),
(1038, 'Theresa','Vogl','2004-01-31','Kapellenweg','2','5221','Lochen', 0, '2026-03-01'),
(1039, 'Johannes','Hirsch','1971-09-12','Kastanienallee','24','3350','Haag', 0, '2026-04-01'),
(1040, 'Franziska','Berger','1988-03-19','Brunnenweg','15','8480','Mureck', 0, '2018-01-01'),
(1041, 'Paul','Niederer','1992-07-01','Uferstraße','5','9210','Pörtschach', 0, '2018-03-01'),
(1042, 'Martina','Sailer','1980-11-08','Hofgasse','10','5730','Mittersill', 0, '2018-06-01'),
(1043, 'Felix','Krall','1997-06-16','Buchenweg','4','3240','Mank', 0, '2018-09-01'),
(1044, 'Barbara','Thaler','1983-04-27','Wachtbergstraße','18','3508','Paudorf', 0, '2018-12-01'),
(1045, 'David','Plank','2001-02-13','Sonnenallee','7','7431','Bad Tatzmannsdorf', 0, '2019-02-01'),
(1046, 'Helena','Kraus','1995-09-06','Glockengasse','22','1190','Wien', 0, '2019-04-01'),
(1047, 'Julian','Fink','2000-12-01','Rathausplatz','1','2100','Korneuburg', 0, '2019-07-01'),
(1048, 'Irene','Stocker','1974-05-11','Mitterweg','9','4550','Kremsmünster', 0, '2019-10-01'),
(1049, 'Maximilian','Baumgartner','1998-08-24','Neugasse','14','8280','Fürstenfeld', 0, '2020-01-01'),
(1050, 'Elisabeth','Schober','1968-01-20','Pfarrgasse','6','4770','Andorf', 0, '2020-03-01'),
(1051, 'Sebastian','Roth','1986-10-15','Steinfeldstraße','28','2630','Ternitz', 0, '2020-06-01'),
(1052, 'Hannah','Pammer','2003-04-09','Blumenstraße','12','3040','Neulengbach', 0, '2020-09-01'),
(1053, 'Oliver','Mayr','1979-07-03','Schlossweg','3','5230','Mattighofen', 0, '2020-12-01'),
(1054, 'Magdalena','Wieser','1991-02-25','Hangstraße','17','5582','St. Michael', 0, '2021-02-01'),
(1055, 'Tobias','Krenn','1994-11-30','Reithweg','20','8940','Liezen', 0, '2021-04-01'),
(1056, 'Ursula','Kaufmann','1970-06-06','Hohlweg','8','7321','Raiding', 0, '2021-07-01'),
(1057, 'Leon','Schilling','2002-09-19','Parkstraße','26','8605','Kapfenberg', 0, '2021-10-01'),
(1058, 'Petra','Kainz','1987-03-01','Kornweg','11','7540','Güssing', 0, '2022-01-01'),
(1059, 'Fabian','Lindner','1996-05-28','Am Anger','4','2130','Mistelbach', 0, '2022-03-01'),
(1060, 'Renate','Zöchling','1978-12-22','Hauptplatz','1','3390','Melk', 0, '2022-06-01'),
(1061, 'Jakob','Forster','1999-01-17','Kirchweg','7','4142','Hofkirchen', 0, '2022-09-01'),
(1062, 'Silvia','Reinhard','1982-08-08','Gartenweg','13','2483','Ebreichsdorf', 0, '2022-12-01'),
(1063, 'Benjamin','Sonnleitner','1993-10-26','Siedlerweg','21','4970','Eitzing', 0, '2023-02-01'),
(1064, 'Clara','Brandl','2004-06-04','Mühlweg','9','6410','Telfs', 0, '2023-04-01'),
(1065, 'Rainer','Wimmer','1976-02-14','Fichtenstraße','16','4070','Eferding', 0, '2023-07-01'),
(1066, 'Laura','Heigl','1995-11-07','Liliengasse','5','1220','Wien', 0, '2023-10-01'),
(1067, 'Kevin','Obauer','2001-03-18','Bachstraße','10','5600','St. Johann', 0, '2024-01-01'),
(1068, 'Monika','Stark','1989-09-29','Alleeweg','19','9560','Feldkirchen', 0, '2024-03-01'),
(1069, 'Philipp','Grasl','1997-04-12','Sonnenhang','23','2560','Berndorf', 0, '2024-06-01'),
(1070, 'Roswitha','Lechner','1973-01-05','Kirchplatz','2','4663','Laakirchen', 0, '2024-09-01'),
(1071, 'Moritz','Höller','1998-07-21','Wiesenstraße','14','4490','St. Florian', 0, '2024-12-01'),
(1072, 'Daniel','Kogler','1985-06-02','Auweg','6','8451','Heimschuh', 0, '2025-02-01'),
(1073, 'Christine','Posch','1992-12-16','Stegweg','8','9020','Klagenfurt', 0, '2025-04-01'),
(1074, 'Raphael','Brenner','2000-10-30','Föhrenweg','12','6060','Hall in Tirol', 0, '2025-07-01'),
(1075, 'Ingrid','Bachinger','1967-04-04','Mayerhofstraße','27','3300','Amstetten', 0, '2025-10-01'),
(1076, 'Samuel','Daxberger','1996-08-11','Neudorf','5','4675','Weibern', 0, '2026-01-01'),
(1077, 'Daniela','Kühn','1981-02-19','Quellenweg','9','9545','Radenthein', 0, '2026-02-01'),
(1078, 'Lorenz','Steinberger','1994-05-23','Panoramaweg','18','6176','Völs', 0, '2026-03-01'),
(1079, 'Anita','Eisl','1988-11-14','Schmiedgasse','7','4600','Wels', 0, '2026-04-01'),
(1080, 'Florian','Kleindienst','2002-01-27','Bergblick','22','8344','Bad Gleichenberg', 0, '2018-01-01'),
(1081, 'Susanne','Riegler','1979-06-09','Hofstraße','11','2120','Wolkersdorf', 0, '2018-03-01'),
(1082, 'Noah','Putz','2004-09-03','Regenbogenweg','3','4810','Gmunden', 0, '2018-06-01'),
(1083, 'Gerhard','Schachner','1971-12-31','Marktstraße','15','8712','Niklasdorf', 0, '2018-09-01'),
(1084, 'Miriam','Fasching','1990-03-06','Kellerweg','8','7071','Rust', 0, '2018-12-01'),
(1085, 'Niklas','Bittner','1999-10-18','Schönbrunnerstraße','44','1050','Wien', 0, '2019-02-01'),
(1086, 'Helmut','Kandler','1965-05-01','Weinbergstraße','20','2170','Poysdorf', 0, '2019-04-01'),
(1087, 'Julia','Haid','1995-07-13','Sonnleiten','6','5760','Saalfelden', 0, '2019-07-01'),
(1088, 'Patrick','Strobl','1987-11-22','Moorweg','17','3264','Gresten', 0, '2019-10-01'),
(1089, 'Evelyn','Kopetzky','2003-02-08','Himmelreich','4','2620','Neunkirchen', 0, '2020-01-01'),
(1090, 'Robert','Tiefenbacher','1978-09-26','Steinfeldgasse','29','2320','Schwechat', 0, '2020-03-01'),
(1091, 'Sarah','Pöschl','1991-04-15','Edelweißstraße','13','6112','Wattens', 0, '2020-06-01'),
(1092, 'Dominik','Schuster','1997-06-27','Hintergasse','5','3580','Horn', 0, '2020-09-01'),
(1093, 'Beate','Kriegl','1984-01-03','Lange Gasse','19','2136','Laa an der Thaya', 0, '2020-12-01'),
(1094, 'Jonas','Rammer','2000-12-09','Feldweg','8','8322','Studenzen', 0, '2021-02-01'),
(1095, 'Gabriele','Stadlmann','1972-08-20','Schulweg','10','4655','Vorchdorf', 0, '2021-04-01'),
(1096, 'Leonhard','Wurzer','1998-03-28','Koglweg','21','5310','Mondsee', 0, '2021-07-01'),
(1097, 'Nadine','Sperl','2001-11-11','Am See','2','7141','Podersdorf', 0, '2021-10-01'),
(1098, 'Walter','Feldner','1966-06-18','Bahnweg','14','6460','Imst', 0, '2022-01-01'),
(1099, 'Alina','Kerschbaumer','2004-05-25','Römerstraße','9','4060','Leonding', 0, '2022-03-01');
