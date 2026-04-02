# 🎮 GameApp - Projecte PMDM

Una aplicació Android per als entusiastes dels videojocs que permet explorar el catàleg global de la API de RAWG, gestionar una col·lecció personal i mantenir un blog d'activitat per a cada joc.

## 🚀 Característiques Principals

- **Explorador de Jocs:** Llistat dinàmic amb dades en temps real de la API RAWG.
- **Filtres Avançats:** Cerca jocs per plataforma (PC, PS5, Xbox, etc.) i ordena els resultats per puntuació, nom o data de llançament.
- **Detall del Joc:** Pantalla completa amb descripció, imatges d'alta qualitat i ràtings.
- **My Games (Persistència):** Gestió de col·lecció personal mitjançant base de dades local **Room**.
- **Blog d'Activitat:** Possibilitat d'afegir entrades amb text i captures de pantalla per a cada joc guardat.
- **Exportació:** Sistema per compartir les dades del blog en format **Excel (.xlsx)**.

## 🏗️ Arquitectura

L'aplicació segueix el patró **MVVM (Model-View-ViewModel)** per garantir un codi net, testejable i fàcil de mantenir:

- **UI:** Fragments i Activities que només s'encarreguen de mostrar dades i capturar interaccions.
- **ViewModel:** Gestiona l'estat de la interfície i es comunica amb el repositori.
- **Repository:** Actua com a mediador entre la xarxa (Retrofit) i la base de dades (Room).
- **Data:** Models de dades compartits i constants del sistema.

## 🛠️ Stack Tecnològic

- **Llenguatge:** Java
- **Xarxa:** Retrofit 2 + RxJava 3
- **Base de Dades:** Room Persistence Library
- **Imatges:** Glide
- **Navegació:** Jetpack Navigation Component
- **UI:** Material Design 3 + ViewBinding


## ⚙️ Configuració del Projecte

1. **API Key:** L'aplicació utilitza la API de RAWG. Assegura't de tenir una clau vàlida a `GameApi.java`.
2. **SDK:** Requereix un `compileSdk` mínim de 35.
3. **Build:** Utilitza Gradle amb Version Catalog (`libs.versions.toml`).

---
Desenvolupat com a projecte per al mòdul de Disseny d'Interfícies.