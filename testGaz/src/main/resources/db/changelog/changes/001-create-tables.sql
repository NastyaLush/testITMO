CREATE TABLE patients
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255)                NOT NULL,
    email      VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE doctors
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255)                NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE appointments
(
    id          BIGSERIAL PRIMARY KEY,
    doctor      BIGINT       NOT NULL REFERENCES doctors (id) ON DELETE CASCADE,
    patient     BIGINT       REFERENCES patients (id)  ON DELETE CASCADE,
    started_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMP    NOT NULL DEFAULT NOW(),
    status      VARCHAR(255) NOT NULL default 'FREE'
);