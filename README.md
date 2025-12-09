**_<h1 align="center">:vulcan_salute: Proyecto Realizado con Andoid Studio :computer:</h1>_**

**<h3>:blue_book: Contexto:</h3>**

<p>Un gimnasio local necesita una app de temporizador para que sus usuarios realicen circuitos de ejercicio por tiempo. El principal problema es que, al rotar la pantalla o al minimizar la app, el temporizador se reinicia o se detiene incorrectamente.</p>
<p>La app debe garantizar que el temporizador:</p>
<ul>
    <li>Continúe funcionando correctamente si la pantalla rota</li>
    <li>Se detenga de forma segura si la app pasa a segundo plano o se cierra</li>
    <li>Retome la cuenta cuando vuelva a primer plano (si corresponde)</li>
</ul>

<p><b>negritas</b> y otros</p>

<!-- EJEMPLO DE CODIGO EN EL TEXTO -->
<p>Para la persistencia local utilicé la librería <b>Room</b>, definiendo una entidad <code>NoteEntity</code>, un <code>NoteDao</code> y una base de datos <code>NotesDatabase</code>.Con este enfoque, la nota se conserva aunque la aplicación se cierre por completo.</p>
<!-- --------------------------------------------------------- -->



**<h3>:orange_book: Requerimientos:</h3>**

<p>Desarrolla una app de temporizador de ejercicio con las siguientes características:</p>
<ul>
    <li>Un TextView que muestre el tiempo restante</li>
    <li>Un botón Iniciar/Detener</li>
    <li>Control del temporizador con ViewModel</li>
    <li>Uso de los métodos onStart(), onStop(), onPause(), onResume() para gestionar la ejecución del temporizador</li>
    <li>Registrar en consola cada evento del ciclo de vida para el análisis posterior</li>
</ul>

**<h3>:green_book: Criterios técnicos obligatorios:</h3>**

<ul>
    <li>Implementar ViewModel para manejar la lógica y evitar reiniciar el temporizador en rotación</li>
    <li>Uso de CountDownTimer gestionado desde ViewModel</li>
    <li>Registrar con Log.d() los métodos del ciclo de vida ejecutados</li>
    <li>Implementar el manejo correcto de onStop() y onResume() para pausar y retomar el temporizador</li>
</ul>

**<h3>📁 Estructura del Proyecto Android:</h3>**

```Android
📘 README.md
📁 app/src/main/
├── 🟧 AndroidManifest.xml
├── 📁 java
│   ├── 📁 com.example.circuitoejercicio
│   │   ├── 📁 data
│   │   │   ├── 📁 local
│   │   │   │    ├── 📁 dao
│   │   │   │    │    └── 🟦 TimerDao.kt
│   │   │   │    ├── 📁 entity
│   │   │   │    │    └── 🟦 TimerEntity.kt
│   │   │   │    └── 🟦 AppDatabase.kt
│   │   │   └── 📁 repository
│   │   │        └── 🟦 TimerRepository.kt
│   │   ├── 📁 nmodel
│   │   │   └── 🟦 TimerState.kt
│   │   ├── 📁 ui
│   │   │   ├── 📁 theme
│   │   │   │    ├── 🟦 Color.kt
│   │   │   │    ├── 🟦 Theme.kt
│   │   │   │    └── 🟦 Type.kt
│   │   │   └── 📁 timer
│   │   │        ├── 📁 compose
│   │   │        │    └── 🟦 TimerScreen.kt
│   │   │        ├── 🟦 TimerActivity.kt
│   │   │        ├── 🟦 TimerViewModel.kt
│   │   │        └── 🟦 TimerViewModelFactory.kt
│   │   └── 🟦 MainActivity.kt
│   ├── 📁 com.example.circuitoejercicio (android Test)
│   │   └── 🟦 ExampleInstrumentedTest.java
│   └── 📁 com.example.circuitoejercicio (test)
│       └── 🟦 ExampleUnitTest.java
├── 📁 java (generated)
├── 📁 res
│   ├── 📁 drawable
│   │   ├── 🖼️ circuitos_ejercicio.jpg
│   │   ├── 🟧 ic_launcher_background.xml
│   │   └── 🟧 ic_launcher_foreground.xml
│   ├── 📁 layout
│   │   └── 🟧 activity_main.xml
│   ├── 📁 mipmap
│   │   ├── 📁 ic_launcher
│   │   └── 📁 ic_launcher_round
│   ├── 📁 values
│   │   ├── 📁themes
│   │   │   ├── 🟧 themes.xml
│   │   │   └── 🟧 themes.xml (night)
│   │   ├── 🟧 colors.xml
│   │   └── 🟧 strings.xml
│   └── 📁 xml
📁 Gradle Scripts
├── 🟦 build.gradle.kts (Project: CircuitoEjercicio)
├── 🟦 build.gradle.kts (Module: app)
├── 🟦 proguard-rules.pro (ProGuard rules for ":app")
├── 🟦 gradle.properties (Project properties)
├── 🟦 gradle-wrapper.properties (Gradle Version)
├── 🟦 libs.versions.toml (version Catalog "libs")
├── 🟦 local.properties (SDK Location)
└── 🟦 settings.gradle.kts (Project Settings)
```

**<h3>:blue_book: Resumen del desarrollo del proyecto:</h3>**

<p>Desarrollé una app de temporizador para circuitos de ejercicio pensada para un contexto real de gimnasio, donde el problema principal era que el contador se reiniciaba o quedaba “loco” al rotar la pantalla o al enviar la app a segundo plano. Para resolverlo, saqué toda la lógica del temporizador desde la Activity y la moví a un <code>ViewModel</code>, usando un  <code>CountDownTimer</code> controlado desde ahí para que el estado sobreviva a cambios de configuración.</p>

<p>Para garantizar persistencia incluso si el sistema mata el proceso, implementé una capa de datos con Room. Allí guardo un único registro con el estado actual del temporizador (duración total, tiempo restante, estado lógico y marca de tiempo). Así, cuando la app vuelve, puedo reconstruir el estado y decidir si debo reanudar, pausar o marcar el circuito como terminado.
</p>

<p>Además, integré el ciclo de vida de la Activity usando <code>onStart()</code>, <code>onStop()</code>, <code>onPause()</code> y <code>onResume()</code> de forma explícita: en <code>onStop()</code> pauso y persisto el estado, y en <code>onResume()</code> reanudo solo si el temporizador estaba corriendo antes de ir al fondo. Todo esto lo complemento con logs en consola para analizar el comportamiento. La interfaz la construí con Jetpack Compose, aprovechando las vistas previas ( <code>@Preview</code> ) para poder iterar el diseño sin depender del emulador.</p>

**<h3>:book: Imagen general del proyecto:</h3>**

<p align="center">
  <img src="./app/src/main/res/drawable/circuitos_ejercicio.jpg" alt="Proyecto Vista General" style="width: 80%;">
</p>