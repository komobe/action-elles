# Action'Elles API - Configuration de Sécurité

Cette documentation explique comment configurer la sécurité de l'API Action'Elles.

## Configuration des Serveurs et des Rôles

La sécurité est gérée via le fichier `security.properties`. Ce fichier permet de :
- Définir les URLs publiques (accessibles sans authentification)
- Configurer les serveurs autorisés (CORS)
- Définir les rôles et leurs permissions

### 1. URLs Publiques

Les URLs publiques sont définies dans la liste blanche :

```properties
security.white-list=/api/v1/public/**,/api/v1/health,/api/v1/auth/**
```

### 2. Configuration des Serveurs

Chaque serveur est configuré avec son origine, son rôle et ses paramètres CORS :

```properties
# Configuration d'un serveur web public
security.servers.web.origin=https://actionelles.ci
security.servers.web.role=restricted
security.servers.web.enabled=true

# Configuration d'un panel admin
security.servers.admin.origin=https://admin.actionelles.ci
security.servers.admin.role=full
security.servers.admin.enabled=true
```

### 3. Configuration des Rôles

Les rôles définissent les permissions d'accès aux URLs :

```properties
# Rôle avec accès restreint
security.roles.restricted.urls[/api/v1/products/**]=GET
security.roles.restricted.urls[/api/v1/users/**]=GET
security.roles.restricted.urls[/api/v1/orders/**]=GET,POST

# Rôle avec accès complet
security.roles.full.urls[/**]=GET,POST,PUT,DELETE,PATCH,OPTIONS
```

### 4. Configuration par Défaut des Serveurs

Les paramètres CORS par défaut sont appliqués à tous les serveurs :

```properties
security.default-server-config.allowed-methods=GET,POST,PUT,DELETE,OPTIONS,PATCH
security.default-server-config.allowed-headers=Authorization,Content-Type
security.default-server-config.exposed-headers=Authorization
security.default-server-config.max-age=3600
```

## Ajouter un Nouveau Serveur

Pour ajouter un nouveau serveur :

1. Définir la configuration du serveur :
```properties
security.servers.mon-serveur.origin=https://mon-serveur.com
security.servers.mon-serveur.role=mon-role
security.servers.mon-serveur.enabled=true
```

2. Définir les permissions du rôle :
```properties
security.roles.mon-role.urls[/api/v1/endpoint1/**]=GET,POST
security.roles.mon-role.urls[/api/v1/endpoint2/**]=GET
```

## Configuration JWT

La configuration JWT est définie dans les fichiers de profil :

### Développement (`application-dev.properties`)
```properties
# 10 jours en millisecondes
jwt.expiration=864000000
```

### Production (`application-prod.properties`)
```properties
# 12 heures en millisecondes
jwt.expiration=43200000
```

## Sécurité par Défaut

- Toutes les URLs non listées dans la liste blanche nécessitent une authentification
- Les origines CORS non configurées sont refusées
- Les méthodes HTTP non autorisées sont refusées
- Les tokens JWT invalides ou expirés sont rejetés

## Swagger UI

La documentation Swagger est accessible via :
- Développement : http://localhost:9083/swagger-ui.html
- Production : https://api.actionelles.ci/swagger-ui.html