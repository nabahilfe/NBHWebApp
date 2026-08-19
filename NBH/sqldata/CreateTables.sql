

/**********************************************************/
/* ATTENTION! DO NOT OVERWRITE THE SPRING_SESSION TABLES! */
/**********************************************************/

/* für die Persistierung der SPRING_SESSION Funktionalität von Spring Security */

CREATE TABLE SPRING_SESSION (
    PRIMARY_ID CHAR(36) NOT NULL,
    SESSION_ID CHAR(36) NOT NULL,
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    EXPIRY_TIME BIGINT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
    SESSION_PRIMARY_ID CHAR(36) NOT NULL,
    ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES BYTEA NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID)
        REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
);

CREATE TABLE PERSISTENT_LOGINS (
    username VARCHAR(64) NOT NULL,
    series VARCHAR(64) PRIMARY KEY,
    token VARCHAR(64) NOT NULL,
    last_used TIMESTAMP NOT NULL
);

/**********************************************************/
/* ATTENTION! DO NOT OVERWRITE THE SPRING_SESSION TABLES! */
/**********************************************************/

/* Invalidate all Sessions and Logons */

TRUNCATE TABLE PERSISTENT_LOGINS;

TRUNCATE TABLE SPRING_SESSION_ATTRIBUTES;

TRUNCATE TABLE SPRING_SESSION;

/**************************************/



/****************************************************************************************/
/* ESSENTIAL! Set created_by_id in Table  REGISTRATION_CODE to nullable after creation! */
/****************************************************************************************/

ALTER TABLE REGISTRATION_CODES ALTER COLUMN created_by_id DROP NOT NULL;






/*
 * Generated with Xtext EntityModeller from file "nbh.emodel"
 * Generated at 2026-08-19 16:35:51
 * ModelDescription: NBH Entity Model
 */


/*
 * Create table statements
 */

/* Basisinformationen zum Verein, da fehlt sicher noch einiges, zB Vereinsnummer aus dem Vereinsregister */
create table if not exists ASSOCIATIONS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(80) not null /* Vereinsname */,
    md_description TEXT,
    html_description TEXT,
    street VARCHAR(80) not null /* Offizielle Adresse des Vereins */,
    number VARCHAR(20) not null,
    zip VARCHAR(10) not null,
    city VARCHAR(80) not null,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
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
    created_by_id BIGINT NOT NULL,
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
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Fachliche konfigurierbare Parameter für Kosten Mitgliedsbeitrag oder Zeitschecks. Der Code wird in einem ENUM definiert */
create table if not exists AMOUNT_DOMAIN_VALUES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(20) not null /* aus Enum, z.B TIMECHEQUE, MEMBER_FEE, ... */,
    amount NUMERIC(12,2) not null /* Kosten der jeweiligen Leistung im Gültigkeitszeitraum */,
    valid_from DATE not null,
    valid_to DATE not null /* letzter Eintrag hat immer 9999-12-31 */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Termine und Veranstaltungen, muss noch ausgearbeitet werden! */
create table if not exists EVENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date DATE /* Datum der Veranstaltung */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Texte für die HP. Erfassung als MarkDown, angezeigt wird daraus generiertes HTML */
create table if not exists TEXT_CONTENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content_code VARCHAR(20) /* Aus ENUM - Für welches Element gilt der Text */,
    md_text TEXT /* Text mit Markdoen formatiert */,
    html_text TEXT /* Aus dem Markdown Text generierter HTML Text */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Einmal-Codes für die Registrierung mit E-Mail und Code. */
create table if not exists REGISTRATION_CODES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    code VARCHAR(10) not null /* Zufällige 6-stellige Zahl */,
    email VARCHAR(80) not null /* E-Mail zum Code */,
    expires_at TIMESTAMP not null /* Gültigkeitsdauer des Codes */,
    failed_attempts INTEGER /* Anzahl der Retries */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
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
    phone_number VARCHAR(20) /* Telefonnummer des Mitglieds */,
    password VARCHAR(250),
    joining_date DATE not null /* Eintrittsdatum in den Verein */,
    resignation_date DATE /* Austrittsdatum aus dem Verein */,
    street VARCHAR(80) /* Adressdaten des Mitglieds - Straße */,
    number VARCHAR(20) /* Hausunummer */,
    stair VARCHAR(20) /* Stiege */,
    door VARCHAR(20) /* Tür */,
    zip VARCHAR(10),
    city VARCHAR(80),
    latitude DOUBLE PRECISION /* Aus der Adressvalidierung */,
    longitude DOUBLE PRECISION /* Aus der Adressvalidierung */,
    direct_debit_authorization BOOLEAN not null DEFAULT FALSE /* Wenn Einziehungsauftrag vorhanden kann Mitglied sebständig Zeitschecks bestellen */,
    is_imported_member BOOLEAN not null DEFAULT TRUE /* Für importierte, bestehende Mitglider muss das TRUE sein, damit ihnen kein Gratis-Zeitschecks zugeteilt werden kann */,
    is_system_account BOOLEAN not null DEFAULT FALSE /* Für SystemAccounts wie SysAdmin und Sozialkonto muss TRUE verwendet werden */,
    accumulated_hours INTEGER /* Gut-Stunden - kommt aus Gutschrift bei Eintritt, Stundenkauf, Stundenerwerb durch Hilfestellung, ... */,
    role_id BIGINT /* FK id from ROLES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Dokumentation des jährlichen Mitgliedsbeitrag. TransactionType ist immer INCOME */
create table if not exists MEMBERSHIP_FEES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    for_year INTEGER not null,
    do_not_charge BOOLEAN not null /* z.B. für Ehrenmitglieder */,
    transaction_date DATE not null,
    amount NUMERIC(12,2) not null,
    liable_member_name VARCHAR(80) not null /* Wer hat das veranlasst oder angeordnet -> Name von cretaedBy Member */,
    member_id BIGINT /* FK id from MEMBERS(id) */,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
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
    created_by_id BIGINT NOT NULL,
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
    liable_member_name VARCHAR(80) not null /* Wer hat das veranlasst oder angeordnet -> Name von cretaedBy Member */,
    assigned_to_id BIGINT /* FK id from MEMBERS(id) */,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Buchungsdatensatz zu Zeitscheck-Kauf, Mitgliedschaft, Weihnachtsessen, usw. */
create table if not exists ACCOUNTING_ENTRIES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    accountable_name VARCHAR(80) /* MemberFee, TimeCheque, Transaction, ... */,
    accountable_id BIGINT /* id zur Klasse bzw. Tabelle */,
    accountable_member_id BIGINT /* FK id from MEMBERS(id) */,
    transaction_type VARCHAR(10) not null /* INCOME oder EXPENSE - muss aus Enum TransactionType kommen */,
    transaction_date DATE not null /* Buchungsdatum */,
    transaction_amount NUMERIC(12,2) not null /* Betrag */,
    accounting_date DATE not null /* Verrechnungsdatum */,
    description VARCHAR(250) /* Verpflichtend wenn kein fix definierter Name wie 'Zeitscheck', 'Mitgliedsgebühr' verwendet wird, sondern 'Sonstiges' */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
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
    liable_member_name VARCHAR(80) not null /* Wer hat das veranlasst oder angeordnet -> Name von cretaedBy Member */,
    description VARCHAR(250) not null,
    accounted_by_id BIGINT /* FK id from ACCOUNTING_ENTRIES(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Foto- oder Bildergalerie */
create table if not exists GALLERYS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    gallery_date DATE /* use it only for images from one specific date, eg. Flohmarkt */,
    show_gallery_from DATE /* Starting Date for displaying Gallery to Public - if null, do not display */,
    show_gallery_to DATE /* if set, do not show after this date */,
    description VARCHAR(250) not null,
    remark VARCHAR(250) /* Anmerkung für Editor, wird in der Gallery-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* wenn nicht public dann nur für angemeldete Mitglieder sichtbar */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Bild zu einer Galerie */
create table if not exists IMAGES (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(250) not null,
    image_width INTEGER not null,
    image_height INTEGER not null,
    image_size INTEGER not null,
    image BYTEA not null /* max 1920 x 1920 - scale down at upload */,
    thumbnail BYTEA not null /* max 400 x 400 - scale down at upload */,
    content_type VARCHAR(20) not null /* image/jpeg, image/png ... */,
    description VARCHAR(250),
    is_gallery_cover BOOLEAN not null DEFAULT FALSE /* Das repräsentative Bild für die Galerie das in der Galerieübersicht angezeigt wird */,
    gallery_id BIGINT /* FK id from GALLERYS(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Sammlung von Dokumenten */
create table if not exists LIBRARYS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    show_library_from DATE /* Starting Date for displaying Library to Public - if null, do not display */,
    show_library_to DATE /* if set, do not show after this date */,
    description VARCHAR(250) not null,
    remark VARCHAR(250) /* Anmerkung für Editor, wird in der Library-Ansicht nicht angezeigt */,
    is_public BOOLEAN not null DEFAULT FALSE /* public zugänglich oder nur für Mitglieder */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
    updated_at TIMESTAMPTZ,
    updated_by_id BIGINT,
    version INTEGER NOT NULL
);


/* Dokument - PDF erlauben, sonst nix */
create table if not exists DOCUMENTS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name VARCHAR(250) not null,
    document_size INTEGER not null,
    document_data BYTEA not null,
    content_type VARCHAR(20) not null /* application/pdf */,
    description VARCHAR(250),
    lobrary_id BIGINT /* FK id from LIBRARYS(id) */,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_by_id BIGINT NOT NULL,
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


alter table IMAGES
    add constraint fk_IMAGES_gallery_id foreign key (gallery_id) references GALLERYS(id)
;


alter table DOCUMENTS
    add constraint fk_DOCUMENTS_lobrary_id foreign key (lobrary_id) references LIBRARYS(id)
;




/*
 * Generate the unique constraints
 */

alter table ASSOCIATIONS
    add constraint uc_name_associations unique (name)
;


alter table TEXT_CONTENTS
    add constraint uc_content_code_text_contents unique (content_code)
;


alter table MEMBERS
    add constraint uc_nmbr_members unique (member_nmbr),
    add constraint uc_email_members unique (email)
;


alter table MEMBERSHIP_FEES
    add constraint uc_year_member_membership_fees unique (for_year, member_id)
;


alter table ACCOUNTING_ENTRIES
    add constraint uc_name_id_accounting_entries unique (accountable_name, accountable_id)
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

drop table if exists TEXT_CONTENTS cascade;

drop table if exists REGISTRATION_CODES cascade;

drop table if exists MEMBERS cascade;

drop table if exists MEMBERSHIP_FEES cascade;

drop table if exists TIME_TRANSFERS cascade;

drop table if exists TIME_CHEQUES cascade;

drop table if exists ACCOUNTING_ENTRIES cascade;

drop table if exists TRANSACTIONS cascade;

drop table if exists GALLERYS cascade;

drop table if exists IMAGES cascade;

drop table if exists LIBRARYS cascade;

drop table if exists DOCUMENTS cascade;


/* end of generated file */

