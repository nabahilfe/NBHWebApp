/*
 * Generated with Xtext DataModeler from file "nbh-db.dmodel"
 * Generated at 2026-01-03 23:40:42
 * ModelDescription: NBH Model with Postgres Definitions
 */


/*
 * Cretae table statements
 */

/* Enthält die wichtigsten Vereinsinformation */
create table if not exists associations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) not null /* Vereinsname */,
    description VARCHAR(4000),
    street VARCHAR(80) not null /* Offizielle Adresse des Vereins */,
    number VARCHAR(20) not null,
    zip VARCHAR(10) not null,
    city VARCHAR(80) not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Angebote der Mitglieder */
create table if not exists offers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(10) /* z.B. 200, 300, 400 */,
    description VARCHAR(250) /* z.B. Allgemein Hilfe im Haushalt */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Rollen im Verein */
create table if not exists roles (
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
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Mitglieder des Vereins - Member ID muss automatisch erzeugt werden, beginnen mit erstem Wert 1000 wenn noch nichts vorhanden ist */
create table if not exists members (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_nmbr INTEGER not null,
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
    role BIGINT /* FK id from roles(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* der jährliche Mitgliedsbeitrag */
create table if not exists membership_fee (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    for_year DATE not null,
    amount NUMERIC(12,2) not null,
    member BIGINT /* FK id from members(id) */,
    accounting_entry BIGINT /* FK id from accounting_entry(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Angebote die ein Mitglied macht */
create table if not exists member_offers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    offer BIGINT /* FK id from offers(id) */,
    member BIGINT /* FK id from members(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Zeitgutschrift von Mitglied X zu Mitglied Y */
create table if not exists time_transfers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date_of_service DATE not null /* Wann wurde die Leistung erbracht */,
    hours SMALLINT not null /* Wie viele Stunden, mögliche Werte z.B. 1 .. 5 */,
    note VARCHAR(250) /* Anmerkung zur erbrachten Leistung */,
    offer BIGINT /* FK id from offers(id) */,
    from_member BIGINT /* FK id from members(id) */,
    to_member BIGINT /* FK id from members(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Zeitscheck - wird gekauft, zuerst angelegt und dann später verbucht vom Kassier */
create table if not exists time_cheques (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    hours SMALLINT not null /* Anzahl der Stunden, üblicherweise 5 (Beitritt zum Verein) oder 10 */,
    amount NUMERIC(12,2) not null,
    assigned_to BIGINT /* FK id from members(id) */,
    accounting_entry BIGINT /* FK id from accounting_entry(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);


/* Buchungsdatensatz zu Zeitscheck-Kauf, Mitgliedschaft, Weihnachtsessen, usw. */
create table if not exists accounting_entry (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    accounting_date DATE not null /* Buchungsdatum */,
    accounting_type VARCHAR(10) not null /* INCOMING oder OUTGOING */,
    accountable_entity VARCHAR(80) /* TimeCheque oder MembershipFee oder .... wird automatisch generiert */,
    description VARCHAR(250) /* Verpflichtend wenn keine accountableEntity eingetragen ist */,
    amount NUMERIC(12,2) not null /* Betrag */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by BIGINT,
    version INTEGER NOT NULL
);



/*
 * Generate the foreign key constraints
 */

alter table members
    add constraint fk_members_role foreign key (role) references roles(id);


alter table membership_fee
    add constraint fk_membership_fee_member foreign key (member) references members(id),
    add constraint fk_membership_fee_accounting_entry foreign key (accounting_entry) references accounting_entry(id);


alter table member_offers
    add constraint fk_member_offers_offer foreign key (offer) references offers(id),
    add constraint fk_member_offers_member foreign key (member) references members(id);


alter table time_transfers
    add constraint fk_time_transfers_offer foreign key (offer) references offers(id),
    add constraint fk_time_transfers_from_member foreign key (from_member) references members(id),
    add constraint fk_time_transfers_to_member foreign key (to_member) references members(id);


alter table time_cheques
    add constraint fk_time_cheques_assigned_to foreign key (assigned_to) references members(id),
    add constraint fk_time_cheques_accounting_entry foreign key (accounting_entry) references accounting_entry(id);




/*
 * Generate the unique constraints
 */

alter table associations
add
    constraint uc_associations unique (name)
;


alter table membership_fee
add
    constraint uc_membership_fee unique (for_year, member)
;


alter table member_offers
add
    constraint uc_member_offers unique (offer, member)
;




/*
 * Create the insert / update triggers
 */



/*
 * !!!! **** DANGEROUS **** !!!!
 * Ruthless drop table statemens
 */

drop table associations cascade;

drop table offers cascade;

drop table roles cascade;

drop table members cascade;

drop table membership_fee cascade;

drop table member_offers cascade;

drop table time_transfers cascade;

drop table time_cheques cascade;

drop table accounting_entry cascade;


/* end of generated file */
