/*
 * Generated with Xtext DataModeler from file "nbh.emodel"
 * Generated at 2026-01-15 17:26:59
 * ModelDescription: NBH Entity Model with Postgres Definitions - Neu!
 */


/*
 * Cretae table statements
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
    is_board_member BOOLEAN not null /* Hat eine Funktion wie 'Vorstand', 'Kassier' usw. Muss bei der Rolle vergeben werden */,
    is_admin BOOLEAN not null /* Hat weitgehende Rechte, kann Mitglieder verwalten und Zeitschecks ausstellen */,
    is_treasurer BOOLEAN not null /* Verwaltet das Geld, Kassier */,
    is_secretary BOOLEAN not null /* Schriftführer */,
    is_auditor BOOLEAN not null /* Rechnungsprüfer, muss unabhängig vom Vorstand sein, darf also kein Board Meber sein oder sonstige rollen haben */,
    is_time_keeper BOOLEAN not null /* Kann Zeit-Schescks vergeben / verkaufe und Zeiteschecks verbuchen */,
    is_miscellaneous BOOLEAN not null /* Sonstiges, z.B. Ehrenmitglied */,
    role_name VARCHAR(80) not null /* Mitglied, Vorstand, stv. Vorstand, Kassier, stv. Kassier, Rechnungsprüfer, Schriftführer, .... */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,                    
    version INTEGER NOT NULL
);


/* Fachliche konfigurierbare Parameter wie Mitgliedsbeitrag oder Kosten eines Zeitschecks. Der Code wird in einem ENUM definiert */
create table if not exists DOMAIN_VALUES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(20) not null,
    amount NUMERIC(12,2) not null,
    valid_from DATE not null,
    valid_to DATE not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,                    
    version INTEGER NOT NULL
);


/* Die Mitglieder des Vereins. Eine Mitgliedsnummer muss bei Neuanlage automatisch vergeben werden. */
create table if not exists MEMBERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_nmbr INTEGER not null /* Member ID wird automatisch erzeugt, beginnen mit erstem Wert 1000 wenn noch nichts vorhanden ist */,
    first_name VARCHAR(80) not null,
    last_name VARCHAR(80) not null,
    birthdate DATE not null,
    email VARCHAR(80),
    password VARCHAR(250),
    joining_date DATE not null /* Eintrittsdatum in den Verein */,
    resignation_date DATE /* Austrittsdatum aus dem Verein */,
    street VARCHAR(80) /* Adressdaten des Mitglieds */,
    number VARCHAR(20),
    zip VARCHAR(10),
    city VARCHAR(80),
    accumulated_hours INTEGER /* Gut-Stunden - kommt aus Gutschrift bei Eintritt, Stundenkauf, Stundenerwerb durch Hilfestellung, ... */,
    role_id BIGINT /* FK id from ROLES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,                    
    version INTEGER NOT NULL
);


/* Dokumentation des jährlicher Mitgliedsbeitrag */
/* Extends Table ACCOUNTABLES so no fields created_at and created_by_id in this table */
create table if not exists MEMBERSHIP_FEES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    for_year DATE not null,
    member_id BIGINT /* FK id from MEMBERS(id) */,
    accounting_entry_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    version INTEGER NOT NULL
);


/* Angebote des Mitglieds. Klären, brauchen wir das überhaupt? */
create table if not exists MEMBER_OFFERS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
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


/* Zeitscheck - wird gekauft, zuerst angelegt und dann später verbucht vom Kassier */
/* Extends Table ACCOUNTABLES so no fields created_at and created_by_id in this table */
create table if not exists TIME_CHEQUES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hours SMALLINT not null /* Anzahl der Stunden, üblicherweise 5 (Beitritt zum Verein) oder 10 */,
    assigned_to_id BIGINT /* FK id from MEMBERS(id) */,
    accounting_entry_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    version INTEGER NOT NULL
);


/* Superklasse für alles was verbucht wird (Zeitscheck kauf, Mitgliedschaft...) */
create table if not exists ACCOUNTABLES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    amount NUMERIC(12,2) not null,
    order_date DATE not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,                    
    version INTEGER NOT NULL
);


/* Buchungsdatensatz zu Zeitscheck-Kauf, Mitgliedschaft, Weihnachtsessen, usw. */
create table if not exists ACCOUNTING_ENTRIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    accounting_date DATE not null /* Buchungsdatum */,
    accounting_type VARCHAR(10) not null /* INCOMING oder OUTGOING */,
    description VARCHAR(250) /* Verpflichtend wenn keine accountableEntity eingetragen ist */,
    amount NUMERIC(12,2) not null /* Betrag */,
    accountable_id BIGINT /* FK id from ACCOUNTABLES(id) */,
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
    add constraint fk_MEMBERS_role foreign key (role_id) references ROLES(id)
    ;


alter table MEMBERSHIP_FEES
    add constraint fk_MEMBERSHIP_FEES_member foreign key (member_id) references MEMBERS(id),
    add constraint fk_MEMBERSHIP_FEES_accounting_entry foreign key (accounting_entry_id) references ACCOUNTING_ENTRIES(id),
    /* fk ref to Hibernate parent table */
    add constraint fk_MEMBERSHIP_FEES_ACCOUNTABLES_parent foreign key (id) references ACCOUNTABLES(id)
    ;


alter table MEMBER_OFFERS
    add constraint fk_MEMBER_OFFERS_offer foreign key (offer_id) references OFFERS(id),
    add constraint fk_MEMBER_OFFERS_member foreign key (member_id) references MEMBERS(id)
    ;


alter table TIME_TRANSFERS
    add constraint fk_TIME_TRANSFERS_offer foreign key (offer_id) references OFFERS(id),
    add constraint fk_TIME_TRANSFERS_from_member foreign key (from_member_id) references MEMBERS(id),
    add constraint fk_TIME_TRANSFERS_to_member foreign key (to_member_id) references MEMBERS(id)
    ;


alter table TIME_CHEQUES
    add constraint fk_TIME_CHEQUES_assigned_to foreign key (assigned_to_id) references MEMBERS(id),
    add constraint fk_TIME_CHEQUES_accounting_entry foreign key (accounting_entry_id) references ACCOUNTING_ENTRIES(id),
    /* fk ref to Hibernate parent table */
    add constraint fk_TIME_CHEQUES_ACCOUNTABLES_parent foreign key (id) references ACCOUNTABLES(id)
    ;


alter table ACCOUNTING_ENTRIES
    add constraint fk_ACCOUNTING_ENTRIES_accountable foreign key (accountable_id) references ACCOUNTABLES(id)
    ;




/*
 * Generate the unique constraints
 */

alter table ASSOCIATIONS
add
    constraint uc_ASSOCIATIONS unique (name)
;


alter table MEMBERSHIP_FEES
add
    constraint uc_MEMBERSHIP_FEES unique (for_year, member_id)
;


alter table MEMBER_OFFERS
add
    constraint uc_MEMBER_OFFERS unique (offer_id, member_id)
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

drop table if exists DOMAIN_VALUES cascade;

drop table if exists MEMBERS cascade;

drop table if exists MEMBERSHIP_FEES cascade;

drop table if exists MEMBER_OFFERS cascade;

drop table if exists TIME_TRANSFERS cascade;

drop table if exists TIME_CHEQUES cascade;

drop table if exists ACCOUNTABLES cascade;

drop table if exists ACCOUNTING_ENTRIES cascade;


/* end of generated file */
