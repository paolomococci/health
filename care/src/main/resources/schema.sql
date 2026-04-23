-- patients
-- local.health.care.models.Patient
CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL,
    -- "MALE", "FEMALE", "OTHER"
    gender VARCHAR(7)
);

-- episodes
-- local.health.care.models.Episode
CREATE TABLE IF NOT EXISTS episodes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    started_at TIMESTAMP NOT NULL,
    kind VARCHAR(255) NOT NULL,
    -- FK to patients.id nullable until set
    patient_id  BIGINT,
    CONSTRAINT fk_episode_patient
        FOREIGN KEY (patient_id)
        REFERENCES patients(id)
        ON DELETE SET NULL
);

-- observations
-- local.health.care.models.Observation
CREATE TABLE IF NOT EXISTS observations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(31) NOT NULL,
  kind VARCHAR(255) NOT NULL,
  unit VARCHAR(31),
  recorded_at TIMESTAMP NOT NULL,
  episode_id BIGINT NOT NULL,
  CONSTRAINT fk_observation_episode 
    FOREIGN KEY (episode_id) 
    REFERENCES episodes(id) 
    ON DELETE CASCADE
);

-- Support Indices
CREATE INDEX IF NOT EXISTS idx_episode_patient ON episodes(patient_id);
CREATE INDEX IF NOT EXISTS idx_observation_episode ON observations(episode_id);
CREATE INDEX IF NOT EXISTS idx_observation_code ON observations(code);