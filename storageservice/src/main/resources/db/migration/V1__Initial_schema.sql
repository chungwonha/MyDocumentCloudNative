CREATE TABLE upload_record (
      id                  BIGSERIAL PRIMARY KEY NOT NULL,
      doc_id               varchar(255) NOT NULL,
      user_id              varchar(255) NOT NULL,
      created_date        bigint NOT NULL,
      last_modified_date  bigint NOT NULL,
      version             integer NOT NULL
);