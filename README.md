# 🎮 GameApp - Projecte PMDM

Una aplicació Android completa per als entusiastes dels videojocs que permet explorar el catàleg global de la API de RAWG, gestionar una col·lecció personal i mantenir un blog d'activitat per a cada joc. Aquest projecte implementa una arquitectura robusta i funcionalitats avançades de persistència i xarxa.

## 🚀 Característiques Principals

- **Explorador de Jocs:** Llistat dinàmic amb dades en temps real de la API RAWG.
- **Filtres Avançats:** Cerca jocs per plataforma (PC, PS5, Xbox, etc.) i ordena els resultats per puntuació, nom o data de llançament.
- **Detall del Joc:** Pantalla completa amb descripció, imatges d'alta qualitat, carrousel de captures i ràtings detallats.
- **My Games (Persistència):** Gestió de col·lecció personal mitjançant base de dades local **Room**.
- **Blog d'Activitat:** Possibilitat d'afegir entrades amb text i captures de pantalla seleccionades de la galeria per a cada joc guardat.
- **Exportació:** Sistema per compartir les dades del blog en format **Excel (.xlsx)**.

## 🏗️ Arquitectura i Patrons

L'aplicació segueix el patró **MVVM (Model-View-ViewModel)** per garantir un codi net, testejable i fàcil de mantenir, complint amb les recomanacions oficials d'Android:

- **UI:** Fragments i Activities modulars que utilitzen **ViewBinding** per a una interacció segura amb les vistes.
- **ViewModel:** Gestiona l'estat de la interfície mitjançant **LiveData**, assegurant que la UI reaccioni automàticament als canvis de dades.
- **Repository:** Actua com a **Font Única de Veritat (Single Source of Truth)**, mediant entre la xarxa (Retrofit) i la base de dades local (Room).
- **Data Persistence:** Implementació de **Room** amb entitats relacionades per gestionar jocs guardats i entrades de blog.
- **Xarxa:** Consum d'API REST amb **Retrofit**, configurat amb timeouts optimitzats per a una millor experiència d'usuari.

## 🛠️ Stack Tecnològic

- **Llenguatge:** Java (JDK 11)
- **Xarxa:** Retrofit 2 + Gson Converter + OkHttpClient (Timeouts personalitzats).
- **Base de Dades:** Room Persistence Library (DAOs, Entities, Database).
- **Imatges:** Glide per a la càrrega eficient de portades i captures.
- **Navegació:** Jetpack Navigation Component amb **Navigation Drawer** lateral.
- **UI:** Material Design 3, RecyclerView (GridLayout), ViewPager2 i ConstraintLayout.
- **Multimèdia:** Integració amb la galeria del sistema per a la selecció d'imatges.

## ⚙️ Millores Recents

1. **API Key:** L'aplicació utilitza la API de RAWG. Assegura't de tenir una clau vàlida a `GameApi.java`.
2. **SDK:** Requereix un `compileSdk` mínim de 35.
3. **Build:** Utilitza Gradle amb Version Catalog (`libs.versions.toml`).
4. **Optimització de Xarxa:** S'ha configurat un timeout de 30 segons en el client OkHttp per evitar talls en connexions lentes amb l'API de RAWG.
5. **Robustesa de la UI:** S'han afegit logs de depuració detallats i control d'errors en la càrrega de dades per facilitar el manteniment futur.
6. **Correccions visuals:** Millora en la coherència de l'estat dels botons (ex. "In my collection") i en els textos de navegació.


## Avís de disponibilitat de la API de RAWG
**Estat del servei:** Actualment l'aplicació pot presentar errors de càrrega o temps d'espera esgotats. Aquest problema és aliè a l'aplicació i es deu a una caiguda temporal dels servidors de l'API externa de [RAWG](https://rawg.io/) d'on s'obtenen les dades.

Algunes possibilitats per futures migracions son:
- IGDB (de Twitch)
- Steam Web API / Storefront API
- Giant Bomb API: Otra base de datos veterana con API gratuita para uso no comercial.

---
**Projecte finalitzat** com a part del mòdul de Programació de Dispositius Mòbils (PMDM) i Disseny d'Interfícies (DI).