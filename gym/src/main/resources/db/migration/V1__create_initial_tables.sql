-- USERS
CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(120)  NOT NULL,
    email       VARCHAR(180)  NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- EXERCISES
CREATE TABLE exercises (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name              VARCHAR(120) NOT NULL,
    description       TEXT,
    muscle_group      VARCHAR(80),
    image_url         TEXT,
    video_url         TEXT,
    is_default        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_by_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    deleted_at        TIMESTAMP,
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at        TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- WORKOUTS
CREATE TABLE workouts (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name        VARCHAR(120) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- WORKOUT_EXERCISES
CREATE TABLE workout_exercises (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    workout_id  UUID    NOT NULL REFERENCES workouts(id) ON DELETE CASCADE,
    exercise_id UUID    NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    position    INTEGER,
    target_sets INTEGER,
    target_reps VARCHAR(30),
    notes       TEXT
);

-- LOAD_ENTRIES
CREATE TABLE load_entries (
    id           UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    exercise_id  UUID         NOT NULL REFERENCES exercises(id) ON DELETE CASCADE,
    workout_id   UUID         REFERENCES workouts(id) ON DELETE SET NULL,
    performed_at DATE         NOT NULL,
    load_kg      NUMERIC(6,2) NOT NULL,
    sets         INTEGER,
    reps         INTEGER,
    notes        TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);