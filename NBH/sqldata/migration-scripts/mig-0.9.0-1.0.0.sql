/* URL / API Aufrufstatistik */
create table if not exists API_REQUEST_STATISTICS (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    request_date DATE /* Datum des Aufrufs */,
    http_method VARCHAR(10),
    url_pattern TEXT,
    status_code INTEGER,
    request_count NUMERIC,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ,
    version INTEGER NOT NULL
);

alter table API_REQUEST_STATISTICS
    add constraint uc_data_api_request_statistics unique (request_date, http_method, url_pattern, status_code)
;

