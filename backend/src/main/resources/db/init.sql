-- Script d'initialisation de la base de données
-- Auteur: Moro KONÉ

-- Suppression des tables si elles existent déjà
DROP TABLE IF EXISTS produit_garanties CASCADE;
DROP TABLE IF EXISTS produit_categorie_vehicules CASCADE;
DROP TABLE IF EXISTS souscriptions CASCADE;
DROP TABLE IF EXISTS vehicules CASCADE;
DROP TABLE IF EXISTS assures CASCADE;
DROP TABLE IF EXISTS garanties CASCADE;
DROP TABLE IF EXISTS produits CASCADE;
DROP TABLE IF EXISTS categorie_vehicules CASCADE;

-- Création des tables
CREATE TABLE categorie_vehicules (
    id VARCHAR(36) PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    libelle VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE produits (
    id VARCHAR(36) PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE garanties (
    id VARCHAR(36) PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL,
    description TEXT,
    code VARCHAR(50) NOT NULL,
    puissance_fiscale VARCHAR(50),
    base_de_calcul VARCHAR(50),
    prime_valeur DECIMAL(10,2),
    prime_type VARCHAR(50),
    prime_minimum_valeur DECIMAL(10,2),
    prime_minimum_type VARCHAR(50),
    max_age INTEGER,
    plafonne BOOLEAN DEFAULT false
);

CREATE TABLE assures (
    id VARCHAR(36) PRIMARY KEY,
    adresse VARCHAR(255),
    telephone VARCHAR(20),
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    numero_carte_identite VARCHAR(50) UNIQUE,
    ville VARCHAR(100)
);

CREATE TABLE vehicules (
    id VARCHAR(36) PRIMARY KEY,
    date_mise_en_circulation DATE,
    numero_immatriculation VARCHAR(50) UNIQUE NOT NULL,
    couleur VARCHAR(50),
    nombre_de_sieges INTEGER,
    nombre_de_portes INTEGER,
    categorie_id VARCHAR(36) REFERENCES categorie_vehicules(id)
);

CREATE TABLE souscriptions (
    id VARCHAR(36) PRIMARY KEY,
    numero VARCHAR(50) UNIQUE NOT NULL,
    assure_id VARCHAR(36) REFERENCES assures(id),
    vehicule_id VARCHAR(36) REFERENCES vehicules(id)
);

-- Tables de liaison pour les relations many-to-many
CREATE TABLE produit_garanties (
    produit_id VARCHAR(36) REFERENCES produits(id),
    garantie_id VARCHAR(36) REFERENCES garanties(id),
    PRIMARY KEY (produit_id, garantie_id)
);

CREATE TABLE produit_categorie_vehicules (
    produit_id VARCHAR(36) REFERENCES produits(id),
    categorie_vehicule_id VARCHAR(36) REFERENCES categorie_vehicules(id),
    PRIMARY KEY (produit_id, categorie_vehicule_id)
);

-- Création des index pour améliorer les performances
CREATE INDEX idx_vehicules_categorie ON vehicules(categorie_id);
CREATE INDEX idx_souscriptions_assure ON souscriptions(assure_id);
CREATE INDEX idx_souscriptions_vehicule ON souscriptions(vehicule_id);
CREATE INDEX idx_produit_garanties_produit ON produit_garanties(produit_id);
CREATE INDEX idx_produit_garanties_garantie ON produit_garanties(garantie_id);
CREATE INDEX idx_produit_categorie_vehicules_produit ON produit_categorie_vehicules(produit_id);
CREATE INDEX idx_produit_categorie_vehicules_categorie ON produit_categorie_vehicules(categorie_vehicule_id);

-- Commentaires sur les tables
COMMENT ON TABLE categorie_vehicules IS 'Table des catégories de véhicules';
COMMENT ON TABLE produits IS 'Table des produits d''assurance';
COMMENT ON TABLE garanties IS 'Table des garanties d''assurance';
COMMENT ON TABLE assures IS 'Table des assurés';
COMMENT ON TABLE vehicules IS 'Table des véhicules';
COMMENT ON TABLE souscriptions IS 'Table des souscriptions';
COMMENT ON TABLE produit_garanties IS 'Table de liaison entre produits et garanties';
COMMENT ON TABLE produit_categorie_vehicules IS 'Table de liaison entre produits et catégories de véhicules'; 