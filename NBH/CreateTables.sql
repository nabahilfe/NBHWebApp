/*
 * Generated with Xtext EntityModeller from file "nbh.emodel"
 * Generated at 2026-02-12 10:01:43
 * ModelDescription: NBH Entity Modell
 */


/*
 * Create table statements
 */

/* Basisinformationen zum Verein, da fehlt sicher noch einiges, zB Vereinsnummer aus dem Vereinsregister */
create table if not exists ASSOCIATIONS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) not null /* Vereinsname */,
    description VARCHAR(4000),
    street VARCHAR(80) not null /* Offizielle Adresse des Vereins */,
    number VARCHAR(20) not null,
    zip VARCHAR(10) not null,
    city VARCHAR(80) not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Angebote der Mitglieder, wird bei der Verbuchung von Zeitschecks verwendet */
create table if not exists OFFERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(10) /* z.B. 200, 300, 400 */,
    description VARCHAR(250) /* z.B. Allgemein Hilfe im Haushalt */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Rollen im Verein. Über die Rollen werden auch die Berechtigungen vergeben. */
create table if not exists ROLES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    is_board_member BOOLEAN not null /* VEREINSROLLE - Vorstand */,
    is_treasurer BOOLEAN not null /* VEREINSROLLE - Kassier, Verwaltet die Buchungen */,
    is_secretary BOOLEAN not null /* VEREINSROLLE - Schriftführer */,
    is_auditor BOOLEAN not null /* VEREINSROLLE - Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf keine sonstige Rollen haben */,
    is_time_keeper BOOLEAN not null /* ZUSATZ-ROLLE - Kann Zeit-Schecks vergeben / verkaufen / verbuchen, muss Vereinsrolle haben */,
    is_admin BOOLEAN not null /* ZUSATZ-ROLLE - Hat alle Rechte, muss eine Vereinsrollen haben */,
    is_miscellaneous BOOLEAN not null /* SPEZIAL-ROLLE - z.B. Ehrenmitglied */,
    role_name VARCHAR(80) not null /* Mitglied, Vorstand, stv. Vorstand, Kassier, stv. Kassier, Rechnungsprüfer, Schriftführer, .... */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Fachliche konfigurierbare Parameter für Kosten Mitgliedsbeitrag oder Zeitschecks. Der Code wird in einem ENUM definiert */
create table if not exists AMOUNT_DOMAIN_VALUES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(20) not null /* aus Enum, z.B TIMECHEQUE MEMBER_FEE, ... */,
    amount NUMERIC(12,2) not null /* Kosten der jeweiligen Leistung im Gültigkeitszeitraum */,
    valid_from DATE not null,
    valid_to DATE not null /* letzter Eintrag hat immer 9999-12-31 */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Termine und Veranstaltungen, muss noch ausgearbeitet werden! */
create table if not exists EVENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date DATE /* Datum der Veranstaltung */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Einmal-Codes für die Registrierung mit E-Mail und Code. */
create table if not exists REGISTRATION_CODES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(10) not null /* Zufällige 2-stellige Zahl */,
    email VARCHAR(80) not null /* E-Mail zum Code */,
    expires_at TIMESTAMP not null /* Gültigkeitsdauer des Codes */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Die Mitglieder des Vereins. Eine Mitgliedsnummer muss bei Neuanlage automatisch vergeben werden. */
create table if not exists MEMBERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_nmbr INTEGER not null /* Member ID muss automatisch erzeugt werden, Startwert 1000 */,
    salutation VARCHAR(20) /* Aus Enum Salutation */,
    title VARCHAR(20) /* Titel, Freitext */,
    institution VARCHAR(80) /* Institution die das Mitglied vertritt */,
    first_name VARCHAR(80) not null,
    last_name VARCHAR(80) not null,
    birthdate DATE not null,
    email VARCHAR(80) /* muss immer in lower-case gespeichert werden! */,
    password VARCHAR(250),
    joining_date DATE not null /* Eintrittsdatum in den Verein */,
    resignation_date DATE /* Austrittsdatum aus dem Verein */,
    street VARCHAR(80) /* Adressdaten des Mitglieds */,
    number VARCHAR(20),
    zip VARCHAR(10),
    city VARCHAR(80),
    direct_debit_authorization BOOLEAN not null DEFAULT FALSE /* Wenn Einziehungsauftrag vorhanden kann Mitglied sebständig Zeitschecks bestellen */,
    accumulated_hours INTEGER /* Gut-Stunden - kommt aus Gutschrift bei Eintritt, Stundenkauf, Stundenerwerb durch Hilfestellung, ... */,
    role_id BIGINT /* FK id from ROLES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Dokumentation des jährlicher Mitgliedsbeitrag. TransactionType ist immer INCOME */
create table if not exists MEMBERSHIP_FEES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    for_year DATE not null,
    transaction_date DATE not null,
    amount NUMERIC(12,2) not null,
    member_id BIGINT /* FK id from MEMBERS(id) */,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Angebote des Mitglieds. Klären, brauchen wir das überhaupt? */
create table if not exists MEMBER_OFFERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    is_activ BOOLEAN not null /* Akuell aktiv? */,
    offer_id BIGINT /* FK id from OFFERS(id) */,
    member_id BIGINT /* FK id from MEMBERS(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Zeitgutschrift von Mitglied A an Mitglied B für erbrachte Leistung. */
create table if not exists TIME_TRANSFERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date_of_service DATE not null /* Wann wurde die Leistung erbracht */,
    hours SMALLINT not null /* Wie viele Stunden, mögliche Werte z.B. 1 .. 5 */,
    note VARCHAR(250) /* Anmerkung zur erbrachten Leistung */,
    offer_id BIGINT /* FK id from OFFERS(id) */,
    from_member_id BIGINT /* FK id from MEMBERS(id) */,
    to_member_id BIGINT /* FK id from MEMBERS(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Zeitscheck - zuerst angelegt und dann später verbucht vom Kassier. TransactionType ist immer INCOME */
create table if not exists TIME_CHEQUES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hours SMALLINT not null /* Anzahl der Stunden, üblicherweise 5 (Beitritt zum Verein) oder 10 */,
    transaction_date DATE not null,
    amount NUMERIC(12,2) not null,
    assigned_to_id BIGINT /* FK id from MEMBERS(id) */,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Buchungsdatensatz zu Zeitscheck-Kauf, Mitgliedschaft, Weihnachtsessen, usw. */
create table if not exists ACCOUNTING_ENTRIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    accountable_class VARCHAR(80) /* MemberFee, TimeCheque, Transaction, ... */,
    accountable_id BIGINT /* id zur Klasse bzw. Tabelle */,
    accountable_member_id BIGINT /* FK id from MEMBERS(id) */,
    transaction_type VARCHAR(10) not null /* INCOME oder EXPENSE - muss aus Enum TransactionType kommen */,
    transaction_date DATE not null /* Buchungsdatum */,
    transaction_amount NUMERIC(12,2) not null /* Betrag */,
    accounting_date DATE not null /* Buchungsdatum */,
    description VARCHAR(250) /* Verpflichtend wenn keine accountableClass / accountableId eingetragen ist */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Allgemeine Einnahme oder Ausgabe */
create table if not exists TRANSACTIONS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    transaction_type VARCHAR(10) not null /* INCOME oder EXPENSE - muss aus Enum TransactionType kommen */,
    transaction_date DATE not null,
    amount NUMERIC(12,2) not null,
    description VARCHAR(250) not null,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);



/*
 * Generate the foreign key constraints
 */

alter table MEMBERS
    add constraint fk_MEMBERS_role_id foreign key (role_id) references ROLES(id)
    ;


alter table MEMBERSHIP_FEES
    add constraint fk_MEMBERSHIP_FEES_member_id foreign key (member_id) references MEMBERS(id),
    add constraint fk_MEMBERSHIP_FEES_accounted_by_id foreign key (accounted_by_id) references ACCOUNTING_ENTRIES(id)
    ;


alter table MEMBER_OFFERS
    add constraint fk_MEMBER_OFFERS_offer_id foreign key (offer_id) references OFFERS(id),
    add constraint fk_MEMBER_OFFERS_member_id foreign key (member_id) references MEMBERS(id)
    ;


alter table TIME_TRANSFERS
    add constraint fk_TIME_TRANSFERS_offer_id foreign key (offer_id) references OFFERS(id),
    add constraint fk_TIME_TRANSFERS_from_member_id foreign key (from_member_id) references MEMBERS(id),
    add constraint fk_TIME_TRANSFERS_to_member_id foreign key (to_member_id) references MEMBERS(id)
    ;


alter table TIME_CHEQUES
    add constraint fk_TIME_CHEQUES_assigned_to_id foreign key (assigned_to_id) references MEMBERS(id),
    add constraint fk_TIME_CHEQUES_accounted_by_id foreign key (accounted_by_id) references ACCOUNTING_ENTRIES(id)
    ;


alter table ACCOUNTING_ENTRIES
    add constraint fk_ACCOUNTING_ENTRIES_accountable_member_id foreign key (accountable_member_id) references MEMBERS(id)
    ;


alter table TRANSACTIONS
    add constraint fk_TRANSACTIONS_accounted_by_id foreign key (accounted_by_id) references ACCOUNTING_ENTRIES(id)
    ;




/*
 * Generate the unique constraints
 */

alter table ASSOCIATIONS
add
    constraint uc_ASSOCIATIONS unique (name)
;


alter table MEMBERS
add
    constraint ucNmbr_MEMBERS unique (member_nmbr)
;

alter table MEMBERS
add
    constraint ucEamil_MEMBERS unique (email)
;

alter table MEMBERSHIP_FEES
add
    constraint uc_MEMBERSHIP_FEES unique (for_year, member_id)
;


alter table MEMBER_OFFERS
add
    constraint uc_MEMBER_OFFERS unique (offer_id, member_id)
;


alter table ACCOUNTING_ENTRIES
add
    constraint uc_ACCOUNTING_ENTRIES unique (accountable_class, accountable_id)
;




/*
 * Create the insert / update triggers - only AMA/Oracle
 */



/*
 * !!!! **** DANGEROUS **** !!!!
 * Ruthless drop table statemens
 */

drop table if exists ASSOCIATIONS cascade;

drop table if exists OFFERS cascade;

drop table if exists ROLES cascade;

drop table if exists AMOUNT_DOMAIN_VALUES cascade;

drop table if exists EVENTS cascade;

drop table if exists REGISTRATION_CODES cascade;

drop table if exists MEMBERS cascade;

drop table if exists MEMBERSHIP_FEES cascade;

drop table if exists MEMBER_OFFERS cascade;

drop table if exists TIME_TRANSFERS cascade;

drop table if exists TIME_CHEQUES cascade;

drop table if exists ACCOUNTING_ENTRIES cascade;

drop table if exists TRANSACTIONS cascade;


/* end of generated file */
