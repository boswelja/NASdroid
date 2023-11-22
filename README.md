# NASdroid

NASdroid is an unofficial Android client for [TrueNAS](https://www.truenas.com/) server management.

## Releases

You can get nightly builds from [our CI runs](https://github.com/boswelja/NASdroid/actions/workflows/build-nightly.yml).
Stay tuned for alpha, beta and stable releases!

## Screenshots

Screenshots coming soon.

## State of Development

Features correspond to the menu items you see in the TrueNAS SCALE web interface.

| Feature         | Status          |
|-----------------|-----------------|
| Dashboard       | Available       |
| Storage         | In progress     |
| Apps            | In progress     |
| Reporting       | In progress     |
| Datasets        | Not yet started |
| Shares          | Not yet started |
| Data Protection | Not yet started |
| Network         | Not yet started |
| Credentials     | Not yet started |
| Virtualization  | Not yet started |
| System Settings | Not yet started |

## Libraries

- Networking: [Ktor Client](https://ktor.io/)
- User Interface: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Dependency Injection: [Koin](https://insert-koin.io/)

Dependency versions (and artifact coordinates) are strictly managed via [Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html), with the one exception of the Android settings plugin located in [settings.gradle.kts](./settings.gradle.kts).
Take a look at our [libs.versions.toml](./gradle/libs.versions.toml) to see exact versions and a full list of libraries used.

## Architecture

At a high level the application is split into 3 levels, detailed below.

### [Core](./core/)

Core is a collection of modules that serve as "foundational" components. Most, if not all of these are planned to be moved to separate libraries in the near future.

### [Features](./features/)

The "meat" of NASdroid lives here. This is a collection of modules that build every feature within the app, which are further separated into a maximum of 3 modules per feature.

#### Data

If a feature requires its own data source (local repositories, APIs not found in the TrueNAS API, etc), its abstraction and implementation will be here. This is the only optional module in the set of modules for any given feature.

#### Logic

NASdroid adopts the "use case" pattern, where business logic (Create a pool, delete an application, etc) are all extracted and broken down into single-responsibility classes.

#### UI

All UI for any particular feature lives here. This includes screens, navigation graphs, ViewModels, etc.

### [App](./app/)

This is the application module. It's what depends on all the features before it, and puts it all together into a usable app for your phone.

## Other Stuff

### What

TrueNAS is an open-source NAS operating system. NASdroid is a native Android app that will (eventually) give you the same level of control over your TrueNAS install as the website.

### Why

Have you ever tried to use the TrueNAS website (or any reasonably complex management interface) on your phone? It's not much fun, so let's set out to fix that.
