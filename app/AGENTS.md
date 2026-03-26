# AGENTS.md# 🤖 AGENTS.md: Documentació d'Arquitectura Android (MVVM)

Aquest fitxer serveix com a guia de referència per a l'estructura de projectes Android basats en el patró **Model-View-ViewModel**.

Nom del projecte: PMDM_GameApp
Llenguatge: Català

Context de Gradle: Abans de suggerir una llibreria nova, verifica el fitxer libs.versions.toml (Version Catalog).

Documenta tot el codi per a que s'entengui què fa cada mètode i per a què serveix cada classe.


---

## 🏗️ 1. Estructura de Carpetes Genèrica (Reutilitzable)

En general s'utilitza un model d'Arquitectura MVVM. L'objectiu és separar la lògica de negoci de la interfície d'usuari per facilitar el manteniment i la testabilitat.

Utilitza aquest arbre de directoris per mantenir la coherència en qualsevol projecte:

```text
app/src/main/java/com/example/nomdelprojecte/
├── api/                  # Lògica de xarxa (Retrofit, RxJava)
│   ├── model/            # POJOs (Classes de resposta de l'API)
│   ├── NomApiService.java # Configuració del client (BaseURL, Interceptors)
│   └── NomAPI.java        # Definició dels Endpoints (@GET, @POST)
├── db/                   # Persistència local (Room)
│   ├── model/            # Entitats de la base de dades (@Entity)
│   ├── NomDao.java        # Queries SQL (DAO)
│   └── AppDatabase.java   # Singleton de la base de dades
├── repository/           # Mediador de dades (Font única de veritat)
├── viewmodel/            # Lògica de negoci i gestió d'estat (LiveData)
├── ui/                   # Capa de presentació (Vista)
│   ├── adapter/          # Adaptadors per a RecyclerView
│   ├── fragments/        # Fragments de la interfície
│   └── activities/       # Activitat principal i navegació
└── utils/                # Classes auxiliars (NetworkUtils, Constants)
```

## 🛠️ 3. Tecnologies Clau i Snippets Reutilitzables

### 📡 Xarxa i Reactivitat
* **Retrofit + RxJava 3**: Per fer crides asíncrones sense bloquejar la UI.
> **Tip**: Recorda utilitzar `.subscribeOn(Schedulers.io())` i `.observeOn(AndroidSchedulers.mainThread())` per gestionar els fils correctament.

### Flux de dades
UI ➔ ViewModel ➔ Repository ➔ DAO ➔ SQLite.

### 🖼️ UI Avançada
* **Shared ViewModel**: Ideal per passar informació entre el Fragment de llista i el de detall o el blog sense necessitat de passar Bundles complexos.
* **Glide**: Llibreria optimitzada per carregar les portades dels jocs des de l'API de RAWG de forma eficient.

### 🔑 Tips Tècnics
* **Mappers:** Crea funcions per convertir objectes de `api/model` a `db/model`. Mai guardis directament la resposta de la xarxa a Room si conté camps que no necessites.
* **Permissions:** Si suggereixes codi que requereix permisos (ex. càmera, localització), recorda incloure la modificació necessària al AndroidManifest.xml.
* Posar Logs amb el nom de la classe on ens trobem per a poder debugar


---

# Espeficicacions del projecte de MyGames

### Exportació
Generació de fitxers Excel per compartir dades.

## 🎮 Especificacions: Secció "My Games"

Aquesta secció gestiona la col·lecció personal de l'usuari i la seva activitat (blog), combinant dades locals i funcionalitats de compartició.

### 📋 Requeriments de la Interfície (UI)
* **Accés:** Mitjançant el *Navigation Drawer*.
* **Visualització Dinàmica:**
    - **Portrait:** Disseny partit en dos (zona superior per a la llista de jocs, zona inferior per al blog).
    - **Landscape:** Disseny en columnes (esquerra per a la llista de jocs, dreta per al blog del joc seleccionat).
* **Llistat de Jocs:** Mostra els jocs que l'usuari ha marcat des de l'explorador general.
* **Navegació:** L'accés principal a les seccions (Home, My games) s'ha de fer mitjançant un **Navigation Drawer** lateral

### 💾 Persistència de Dades (Room / Local)
Tota la informació d'aquesta secció ha de ser persistent utilitzant **Room**:
* **Entitat Game:** Emmagatzema les dades bàsiques del joc (Títol, any, nota, imatge).
* **Entitat Blog/Entry:** - Relacionada amb un joc específic.
    - Camps: Data de l'entrada, text del comentari i ruta de la captura de pantalla (*screenshot*).
* **Multimèdia:** Possibilitat d'afegir imatges a les entrades del blog seleccionant-les des de la galeria del dispositiu.

## 🛠️ Funcionalitats Especials i Requeriments

### 🚀 Característiques Principals
1. **Explorador de Jocs:** - Llistat dinàmic amb dades de l'API RAWG.
   - Filtres actius per plataforma (PC, PS4, Xbox, etc.).
   - Sistema d'ordenació per puntuació mitjana (*Average rating*).
2. **Detall del Joc:** - Pantalla amb informació completa i ràtings.
   - Carrousel d'screenshots interactiu que permeti lliscar entre imatges.
   - Opció **"Play full video"** per visualitzar contingut multimèdia.
3. **Gestió de "My Games" (Persistència Room):**
   - Permetre afegir/eliminar jocs de la llista de preferits local.
   - Crear entrades de blog personalitzades per a cada joc.
   - Cada entrada de blog admet text i una imatge (screenshot) seleccionada de la galeria.

### 📤 Sistema de Compartició i Exportació
* **Botó Share:** Funcionalitat per generar i compartir dades externament.
* **Format:** Generació de fitxers **Excel** (.xls / .xlsx).
* **Contingut:** El fitxer exportat ha d'incloure totes les entrades del blog (data, text, referència) del joc que l'usuari estigui consultant en aquell moment.

### 📸 Gestió Multimèdia
* **Integració de Galeria:** Ús de l'API d'Android per seleccionar imatges del dispositiu i vincular-les a les entitats de la base de dades (Room).
* **Vídeo:** Implementació d'un reproductor o intent per visualitzar els tràilers del joc.

### 🧬 Arquitectura Recomanada per a "My Games"
* **Fragment:** `MyGamesFragment` (gestiona els dos estats de pantalla).
* **ViewModel:** `MyGamesViewModel` per carregar la llista de jocs des de la BD i filtrar les entrades del blog per ID de joc.
* **DAO:** `GameDao` amb mètodes per a:
    - `getFavoriteGames()`
    - `getBlogEntriesForGame(int gameId)`
    - `insertBlogEntry(BlogEntity entry)`
	
Arbre de directoris recomanat per al projecte de MyGames:
```text
app/src/m	
	app/src/main/java/com/example/gameapp/
├── api/                  # Retrofit (MagicApiService, RAWGApi)
├── db/                   # Room (AppDatabase, GameDao)
│   └── model/            # Entitats de la BD (@Entity)
├── repository/           # GameRepository (Gestiona API + Room)
├── viewmodel/            # GameViewModel, MyGamesViewModel
├── ui/                   # Activities i Fragments
│   ├── home/             # HomeFragment (Llista de jocs API)
│   ├── detail/           # DetailFragment (Detalls del joc)
│   └── mygames/          # MyGamesFragment (Llista Room + Blog)
├── adapter/              # GameAdapter (RecyclerView)
└── utils/                # NetworkUtils, ExcelExporter
```

## dependencies de preferencia
    implementation("androidx.lifecycle:lifecycle-extensions:2.1.0")

    implementation("androidx.fragment:fragment:${fragment_version}")


    dependencies {
        // For Kotlin use navigation-fragment-ktx, for Java use navigation-fragment
        implementation("androidx.navigation:navigation-fragment:2.8.8")
        implementation("androidx.navigation:navigation-ui:2.8.8")
    }