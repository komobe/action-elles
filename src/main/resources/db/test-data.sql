-- Génération d'UUID : SELECT gen_random_uuid();

-- 1. TABLE : categorie_vehicules
INSERT INTO categorie_vehicules (id, code, libelle, description) VALUES
('1a2b3c4d-1234-5678-9abc-def012345678', '201', 'Promenade et Affaire', 'Usage personnel'),
('2b3c4d5e-2345-6789-abcd-ef0123456781', '202', 'Véhicules Motorisés à 2 ou 3 roues', 'Motocycle, tricycles'),
('3c4d5e6f-3456-789a-bcde-f0123456782a', '203', 'Transport public de voyage', 'Véhicule transport de personnes'),
('4d5e6f70-4567-89ab-cdef-0123456783ab', '204', 'Véhicule de transport avec taximètres', 'Taxis');

-- 2. TABLE : garanties
INSERT INTO garanties (
    id, libelle, description, code, puissance_fiscale,
    base_de_calcul, prime_valeur, prime_type,
    prime_minimum_valeur, prime_minimum_type, max_age, plafonne
) VALUES
-- RC
('c1f7f5a6-9d5b-4d0a-9f57-9f872b92115a', 'Responsabilité Civile', 'RC 2 CV', 'RC', '2',
 'PUISSANCE_FISCALE', 37601, 'MONTANT', NULL, NULL, -1, false),
('a5b6e7c2-7a2d-4f18-9b4a-2f3f8d7217d0', 'Responsabilité Civile', 'RC 3-6 CV', 'RC', '3 à 6',
 'PUISSANCE_FISCALE', 45181, 'MONTANT', NULL, NULL, -1, false),
('d2e94f18-5f42-4a90-82d6-c6b33a43bd63', 'Responsabilité Civile', 'RC 7-10 CV', 'RC', '7 à 10',
 'PUISSANCE_FISCALE', 51078, 'MONTANT', NULL, NULL, -1, false),
('e1c5b398-fd3e-48e0-b5bf-c50df04d5092', 'Responsabilité Civile', 'RC 11-14 CV', 'RC', '11 à 14',
 'PUISSANCE_FISCALE', 65677, 'MONTANT', NULL, NULL, -1, false),
('fa28a9e3-0134-4f45-b8a9-c8b64c20c5ad', 'Responsabilité Civile', 'RC 15-23 CV', 'RC', '15 à 23',
 'PUISSANCE_FISCALE', 86456, 'MONTANT', NULL, NULL, -1, false),
('3e614d13-8c9f-4f94-9f55-7e6820fdfc10', 'Responsabilité Civile', 'RC > 24 CV', 'RC', '>24',
 'PUISSANCE_FISCALE', 104143, 'MONTANT', NULL, NULL, -1, false),
-- Autres garanties
('99fba678-2d48-4a0c-bd2a-80a5a55f2364', 'Dommages', 'Dommages 0-5 ans', 'DOMMAGE', NULL, 'VALEUR_A_NEUF', 2.60, 'POURCENTAGE', NULL, NULL, 5, false),
('a7a3cb48-3940-47ee-9c36-c8d7db178e70', 'Tierce Collision', 'Tierce Collision 0-8 ans', 'TIERCE_COLLISION', NULL, 'VALEUR_A_NEUF', 1.65, 'POURCENTAGE', NULL, NULL, 8, false),
('7b76c6d1-4ecb-420b-b197-fbd3608e9d31', 'Tierce Plafonnée', 'Tierce Plafonnée 0-10 ans', 'TIERCE_PLAFONNEE', NULL, 'VALEUR_ASSUREE', 4.20, 'POURCENTAGE', 100000, 'MONTANT', 10, true),
('d89ac441-9c1c-4a0e-bde1-962dbe1b41bc', 'Vol', 'Garantie vol', 'VOL', NULL, 'VALEUR_VENALE',
 0.14, 'POURCENTAGE', NULL, NULL, -1, false),
('42b53b8f-f715-4fa7-81f9-645b5b1c7096', 'Incendie', 'Garantie incendie', 'INCENDIE', NULL,
 'VALEUR_VENALE', 0.15, 'POURCENTAGE', NULL, NULL, -1, false);

-- 3. TABLE : produits
INSERT INTO produits (id, nom, description) VALUES
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'Papillon', 'Produit incluant RC, DOMMAGE, VOL'),
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', 'Douby', 'Produit incluant RC, DOMMAGE, TIERCE COLLISION'),
('cb5ff3a2-2d90-4b93-93dc-001000000003', 'Douyou', 'Produit incluant RC, DOMMAGE, COLLISION, INCENDIE'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'Toutourisquou', 'Produit incluant toutes les garanties');

-- 4. TABLE : produit_garanties
INSERT INTO produit_garanties (produit_id, garantie_id) VALUES
-- Papillon
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'c1f7f5a6-9d5b-4d0a-9f57-9f872b92115a'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'a5b6e7c2-7a2d-4f18-9b4a-2f3f8d7217d0'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'd2e94f18-5f42-4a90-82d6-c6b33a43bd63'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'e1c5b398-fd3e-48e0-b5bf-c50df04d5092'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'fa28a9e3-0134-4f45-b8a9-c8b64c20c5ad'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', '3e614d13-8c9f-4f94-9f55-7e6820fdfc10'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', '99fba678-2d48-4a0c-bd2a-80a5a55f2364'),
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', 'd89ac441-9c1c-4a0e-bde1-962dbe1b41bc'),

-- Douby
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', 'c1f7f5a6-9d5b-4d0a-9f57-9f872b92115a'),
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', 'a5b6e7c2-7a2d-4f18-9b4a-2f3f8d7217d0'),
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', 'd2e94f18-5f42-4a90-82d6-c6b33a43bd63'),
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', '99fba678-2d48-4a0c-bd2a-80a5a55f2364'),
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', 'a7a3cb48-3940-47ee-9c36-c8d7db178e70'),

-- Douyou
('cb5ff3a2-2d90-4b93-93dc-001000000003', 'c1f7f5a6-9d5b-4d0a-9f57-9f872b92115a'),
('cb5ff3a2-2d90-4b93-93dc-001000000003', '99fba678-2d48-4a0c-bd2a-80a5a55f2364'),
('cb5ff3a2-2d90-4b93-93dc-001000000003', 'a7a3cb48-3940-47ee-9c36-c8d7db178e70'),
('cb5ff3a2-2d90-4b93-93dc-001000000003', '42b53b8f-f715-4fa7-81f9-645b5b1c7096'),

-- Toutourisquou (toutes garanties)
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'c1f7f5a6-9d5b-4d0a-9f57-9f872b92115a'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'a5b6e7c2-7a2d-4f18-9b4a-2f3f8d7217d0'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'd2e94f18-5f42-4a90-82d6-c6b33a43bd63'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'e1c5b398-fd3e-48e0-b5bf-c50df04d5092'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'fa28a9e3-0134-4f45-b8a9-c8b64c20c5ad'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', '3e614d13-8c9f-4f94-9f55-7e6820fdfc10'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', '99fba678-2d48-4a0c-bd2a-80a5a55f2364'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'a7a3cb48-3940-47ee-9c36-c8d7db178e70'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', '7b76c6d1-4ecb-420b-b197-fbd3608e9d31'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', 'd89ac441-9c1c-4a0e-bde1-962dbe1b41bc'),
('cb5ff3a2-2d90-4b93-93dc-001000000004', '42b53b8f-f715-4fa7-81f9-645b5b1c7096');

-- Table produits_categorie_vehicules
-- Structure: produit_id (UUID), categorie_vehicule_id (UUID)

-- Papillon (201)
INSERT INTO produit_categorie_vehicules (produit_id, categorie_vehicule_id) VALUES
 -- Papillon (201)
('6bbd8f1a-28ae-4dff-b1f1-8d9a8f917e17', '1a2b3c4d-1234-5678-9abc-def012345678'),
-- Douby (201)
('cf4c4f7b-eccc-4d0d-8d1f-0267a120e7c7', '1a2b3c4d-1234-5678-9abc-def012345678'),
-- Douyou (201, 202)
('cb5ff3a2-2d90-4b93-93dc-001000000003', '1a2b3c4d-1234-5678-9abc-def012345678'),
('cb5ff3a2-2d90-4b93-93dc-001000000003', '2b3c4d5e-2345-6789-abcd-ef0123456781'),

-- Toutourisquou (201)
('cb5ff3a2-2d90-4b93-93dc-001000000004', '1a2b3c4d-1234-5678-9abc-def012345678');