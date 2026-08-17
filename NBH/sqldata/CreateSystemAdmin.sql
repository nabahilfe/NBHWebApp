/***********************************************************************************************/
/* Zuerst muss der SysAdmin manuell angelegt werden - erst danach kann die App getartet werden */
/* für andere DB-Namen / Subdomains muss die E-Mail Subdomain angepasst werden!                */


insert into members
(member_nmbr,      first_name, last_name,    birthdate, street, number, zip, city, email,                      is_imported_member, is_system_account, created_by_id, version, joining_date) values
(          1, 'Administrator',  'System', '2000-01-01', '-',    '-',    '-',  '-', 'sysadmin.ma@nabahilfe.eu', TRUE,               TRUE,              1,             0,      '2020-01-01');


/* prüfen ob alles geklappt hat und ob die created_by_id der id entspricht */
/***************************************************************************/