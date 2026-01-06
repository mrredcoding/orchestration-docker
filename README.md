# Project HOPE LSI2

Ce projet vise à développer une application Web destinée à transformer le fichier Excel actuellement utilisé pour la
gestion des activités pédagogiques (cours, travaux pratiques, projets, etc.).
L'objectif principal est de faciliter et d'homogénéiser les pratiques des enseignants et des étudiants à travers une
solution centralisée.

Nous avons développé deux composants distincts :

- **Un frontend en React**

Le fichier Excel fourni (*[HOPE_Excel.xlsx](backend/src/main/resources/HOPE_Excel.xlsx)*) contient toutes les données du système existant, et ces informations ont été
intégrées dans l'application. De nouveaux champs ont également pu être ajoutés pour enrichir l'expérience utilisateur.

### Profils d'utilisateurs

Trois types de profils peuvent utiliser l'application :

1. **Enseignant** : Gérer les cours et les activités pédagogiques.
2. **Étudiant** : Accéder aux informations des cours et activités.
3. **Administrateur** : Gérer les utilisateurs, les données, et les configurations du système.

Cette application représente une solution moderne et évolutive pour répondre aux besoins pédagogiques tout en améliorant
la gestion des données et des utilisateurs.

## Informations

### 2.1 Identifiants et mots de passe

| Login                      | Mot de passe                | Rôle       |
|----------------------------|-----------------------------|------------|
| admin.hope@efrei.net       | securePasswordAdmin123*     | Admin      |
| cedric.alonso@efrei.net    | securePasswordStudent123*   | Étudiant   |
| felix.brinet@efrei.net     | securePasswordStudent123*   | Étudiant   |
| jade.hatoum@efrei.net      | securePasswordStudent123*   | Étudiant   |
| ahmad.houhou@efrei.net     | securePasswordStudent123*   | Étudiant   |
| joseph.renno@efrei.net     | securePasswordStudent123*   | Étudiant   |
| jacques.augustin@efrei.net | securePasswordProfessor123* | Enseignant |

### 2.2 Environnements de développement

#### 2.2.1 IDE

- **API** : IntelliJ IDEA
- **Vue** : VSCode

#### 2.2.2 Système de gestion de base de données

- Version utilisée : **MongoDB**

## 2.3 Question complémentaire

### Clean code :

#### Respect des principes Clean Code

#### Respect des principes Clean Code

Dans ce projet, nous avons appliqué les principes de Clean Code pour garantir une structure cohérente, compréhensible et
maintenable du code.

#### 1. Nommage

Nous avons choisi des noms clairs et significatifs pour toutes les classes, méthodes et variables. Par exemple :

- **`GlobalExceptionHandler`** : Indique clairement qu'il gère les exceptions globales dans l'application.
- **`ClientService`** : Spécifie que cette classe traite la logique métier liée aux clients.
- **`BaseException`** : Indique une classe de base pour les exceptions personnalisées.

Ces noms suivent les conventions de style CamelCase et reflètent précisément leur rôle respectif.

#### 2. Gestion des erreurs

La gestion des erreurs est centralisée dans la classe **`GlobalExceptionHandler`**, ce qui garantit une uniformité dans
la manière de répondre aux exceptions. Voici un exemple de bonne pratique mise en œuvre :

```java

@ExceptionHandler(BaseException.class)
public ResponseEntity<String> handleBaseException(BaseException exception) {
    return ResponseEntity
            .status(exception.getClass().getAnnotation(ResponseStatus.class).value())
            .body(exception.getMessage());
}
```

De plus, les exceptions personnalisées (comme **`ResourceNotFoundException`** ou **`InvalidPasswordOrEmailException`**)
incluent des annotations comme `@ResponseStatus`, ce qui simplifie la définition des codes d'état HTTP.

```java 

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
```

#### 3. Simplicité et Lisibilité

Nous avons veillé à réduire la complexité des fonctions en respectant la règle du "une fonction égale une action". Les
blocs de code complexes sont extraits dans des méthodes privées bien nommées pour améliorer la lisibilité.

#### 4. Documentation

La documentation Javadoc ainsi que le swagger complètent les classes et méthodes pour aider à la compréhension du code
par d'autres développeurs.

---

### Respect des principes SOLID

Nous avons analysé et appliqué les principes **SOLID** dans le projet, tout en prenant en compte leur pertinence selon
nos technologies (API en Java et frontend en React). Voici un aperçu :

#### 1. **Single Responsibility Principle (SRP)**

Chaque classe a une seule responsabilité clairement définie :

- **`ClientService`** : Responsable de la logique métier liée aux clients, comme l'inscription et l'authentification.
- **`ToolService`** : Gère les opérations liées aux outils (tools), comme leur récupération ou modification.
- **`GlobalExceptionHandler`** : Centralise la gestion des erreurs.

#### 2. **Open/Closed Principle (OCP)**

Les classes sont ouvertes à l'extension et fermées à la modification. Par exemple, la hiérarchie des exceptions permet
d'ajouter de nouvelles exceptions spécifiques sans modifier les classes existantes :

- **`BaseException`** est la classe de base.
- Les sous-classes comme **`InvalidPasswordOrEmailException`** et **`ResourceNotFoundException`** étendent cette classe
  sans modifier le code d'origine.

#### 3. **Liskov Substitution Principle (LSP)**

Les objets des sous-classes peuvent remplacer ceux de la classe parent sans altérer le comportement attendu. Par
exemple, toute exception personnalisée peut être traitée comme une **`BaseException`**, ce qui simplifie la gestion des
erreurs.

#### 4. **Interface Segregation Principle (ISP)**

Nous avons évité la création d'interfaces géantes. Les interfaces sont concises et spécifiques, adaptées aux besoins des
implémentations. Par exemple, les référentiels (**`ClientRepository`**, **`ToolRepository`**) suivent cette philosophie
avec des méthodes ciblées.

#### 5. **Dependency Inversion Principle (DIP)**

Les services dépendent des abstractions, et non des implémentations concrètes. Par exemple :

- **`AuthenticationManager`** et **`PasswordEncoder`** sont injectés via le constructeur dans **`ClientService`**.
- Cette approche peut facilite les tests unitaires (non développés dans ce projet) et rend le code plus flexible face
  aux changements.

En conclusion, les principes de Clean Code et SOLID ont permis de structurer ce projet pour qu'il soit robuste,
maintenable et évolutif.

---

### Pertinence des principes dans notre contexte

---

En appliquant les principes Clean Code et SOLID, nous avons produit un code lisible, maintenable et évolutif tout en
respectant les contraintes de temps et de contexte.

### 2.4. Pré-requis

- **Git**
- **JDK 21+**
- **Node.js**
- **IntelliJ IDEA**
- **MongoDB**
- **Docker**

---

### Étapes d'installation et d'exécution

---

1. **Cloner le dépôt depuis GitHub :**

```bash
git clone https://github.com/Swiiiip/lola-java-project
```

### Pour setup chaque couche ... Très lentement ...

2. **Lancer l'API :**

- Ouvrir ```backend``` puis construire et lancer l'API via :

```bash
./mvnw spring-boot:run
```

3.**Exécuter le frontend Vue :**

- Ouvrir ```frontend``` puis installer les dépendances :

```bash
npm install
```

- Lancer le serveur de développement :

```bash
npm start
```

### Sinon, passons par docker !

2. **Lancer votre docker engine**
- Nous avons utilisé Docker Desktop qui lance l'engine docker en même temps.
- Vous pouvez vérfier cela via :
```bash
docker info
```

3. **Composer les images docker**
- Le script ```compose.yaml``` est déjà prêt pour vous.
- Il pullera les images nécessaires depuis notre registry docker public, avant de compose le tout.
- Il suffit de lancer la commande suivante depuis la racine du projet :
```bash
docker-compose up --build
```

4. **Arrêter les containers**
- Pour arrêter les containers, il suffit de lancer la commande suivante depuis la racine du projet :
```bash
docker-compose down
```

****
> C'est tout !\
> Commencez à explorer le [site](http://localhost:3000/) depuis votre navigateur.

---

## Participants

- **Cédric ALONSO**
- **Félix BRINET**
- **Joseph RENNO**
- **Jade HATOUM**
- **Ahmad HOUHOU**
