-- CharacterInfo table
CREATE TABLE IF NOT EXISTS character_info (
                                              id SERIAL PRIMARY KEY,
                                              name VARCHAR(255) NOT NULL,
    guild VARCHAR(255),
    region VARCHAR(255),
    realm VARCHAR(255),
    ranking VARCHAR(255),
    commentary VARCHAR(2000),
    languages VARCHAR(255),
    is_russian BOOLEAN DEFAULT FALSE,
    raider_io_data_id BIGINT,
    FOREIGN KEY (raider_io_data_id) REFERENCES raider_io_data(id)
    );

-- RaiderIOData table
CREATE TABLE IF NOT EXISTS raider_io_data (
                                              id SERIAL PRIMARY KEY,
                                              name VARCHAR(255),
    race VARCHAR(255),
    character_class VARCHAR(255),
    active_spec_name VARCHAR(255),
    active_spec_role VARCHAR(255),
    gender VARCHAR(255),
    faction VARCHAR(255),
    achievement_points INT,
    honorable_kills INT,
    thumbnail_url VARCHAR(255),
    region VARCHAR(255),
    realm VARCHAR(255),
    last_crawled_at VARCHAR(255),
    profile_url VARCHAR(255),
    profile_banner VARCHAR(255),
    raid_progressions_json TEXT
    );

-- RaidProgression table
CREATE TABLE IF NOT EXISTS raid_progression (
                                                id SERIAL PRIMARY KEY,
                                                raid_name VARCHAR(255),
    summary VARCHAR(255),
    character_info_id BIGINT,
    FOREIGN KEY (character_info_id) REFERENCES character_info(id)
    );

-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );
