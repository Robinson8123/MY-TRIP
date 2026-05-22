# Mi Destino — Plataforma de Turismo Inteligente

Proyecto de grado, Tecnología en Desarrollo de Software
Fundación Universitaria Tecnológico Comfenalco — Cartagena, Colombia

**Estudiantes:** Jaider David Cajigas Morales, Leider Arrieta Serje, Robinson Andrés Santiago López
**Docente:** Johhany Enrique Valencia

---

## Qué es el proyecto

Mi Destino es una plataforma multicanal de turismo que permite a viajeros colombianos explorar, comparar y comprar planes turísticos. Evolucionó de "My Trip" (VII semestre, app web) a una solución completa con:

- App web React (con chatbot IA, carrito, mapas)
- App móvil Flutter para Android
- Backend Spring Boot REST API
- Sistema de recomendación con ML (filtrado colaborativo)
- Despliegue en Railway (cloud)

---

## Estructura del monorepo

```
/
├── midestinoback/      → Spring Boot 3 + Java 21 (API REST)
├── midestinofront/     → React 18 + Vite + Tailwind (PWA)
├── midestinomovil/     → Flutter 3 + Dart (Android/iOS)
├── mineria/            → Python FastAPI + scikit-learn (ML)
├── railway.json        → Configuración de despliegue cloud
├── docker-compose.yml  → Para desarrollo local
└── INFORME_VIII_SEMESTRE.md → Informe académico completo
```

---

## Backend — midestinoback

- **Puerto:** 9999
- **Base de datos:** MySQL (`midestinobd`)
- **ORM:** Spring Data JPA (Hibernate), DDL auto=update
- **IA:** Groq API con LLaMA 3.3-70b (chatbot de viajes)
- **Pagos:** PayU Sandbox
- **Config:** `src/main/resources/application.properties`

**Endpoints principales:**
- `POST /api/clientes/login` — autenticación
- `GET /api/planes/todos` — listar planes turísticos
- `GET /api/recomendaciones/{clienteId}` — recomendaciones IA
- `GET /api/planes/{id}/imagen` — imagen del plan
- `POST /api/carritos/{clienteId}/agregar` — carrito
- `POST /api/chat` — chatbot

**Variables de entorno necesarias:**
- `GROQ_API_KEY` — clave de Groq (NO está en el código, va solo en Railway)
- `MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`

---

## Frontend — midestinofront

- **Puerto:** 80 (producción) / 5173 (dev)
- **Stack:** React 18, React Router, Tailwind CSS, Leaflet, Flowbite
- **PWA:** vite-plugin-pwa configurado, instalable en cualquier celular
- **Proxy:** nginx.conf usa `${BACKEND_URL}` (variable de entorno en Railway)
- **Dockerfile:** multi-stage (Node build → Nginx serve)

**Para desarrollo local:**
```bash
cd midestinofront
npm install --legacy-peer-deps
npm run dev
```

---

## App Móvil — midestinomovil

- **Framework:** Flutter 3, Dart
- **Estado:** Provider pattern
- **Android:** AGP 8.7.3, Kotlin 2.1.0, Gradle 8.9, minSdk=21, compileSdk=36
- **Backend URL:** configurado en `lib/config/app_config.dart`
  - Emulador Android: `http://10.0.2.2:9999/api`
  - Producción: URL de Railway

**Pantallas:**
- SplashScreen → Login/Registro → Home → PlanDetail → Cart → Chat → Profile

**Sección "Recomendados para ti":**
- Provider: `lib/providers/recomendacion_provider.dart`
- Servicio: `lib/services/recomendacion_service.dart`
- Modelo: `lib/models/recomendacion_model.dart`
- Se carga automáticamente en Home cuando el usuario está autenticado

**Para compilar APK:**
```bash
cd midestinomovil
flutter clean
flutter build apk --debug
```

**Problema conocido:** Flutter auto-modifica `build.gradle.kts` cambiando
`minSdk = 21` a `minSdk = flutter.minSdkVersion`. La solución está aplicada:
se usan variables locales `val appMinSdk = 21` para evitar que el migrador las detecte.

---

## Minería de Datos — mineria/

- **Stack:** Python 3.11, FastAPI, scikit-learn, pandas, SQLAlchemy
- **Puerto:** 8000
- **Algoritmo:** Filtrado colaborativo (similitud coseno) + basado en contenido (tipoSitio)
- **Métricas:** MAE=0.73, RMSE=0.91 (train/test 80/20)
- **Endpoints:** `GET /recomendaciones/{cliente_id}`, `GET /metricas`, `GET /health`

---

## Despliegue — Railway

El `railway.json` define 3 servicios + plugin MySQL:
- `mytrip-backend` → carpeta `midestinoback/`
- `mytrip-frontend` → carpeta `midestinofront/`
- `mytrip-mineria` → carpeta `mineria/`

**IMPORTANTE:** Después de conectar el repo en Railway, agregar manualmente:
- Variable `GROQ_API_KEY` en el servicio `mytrip-backend`

La comunicación interna entre servicios usa red privada Railway:
`http://mytrip-backend.railway.internal:9999`

---

## Base de datos

MySQL, tablas principales:
- `Cliente` — usuarios registrados
- `PlanEmpresa` — planes turísticos ofrecidos
- `ValoracionPlan` — calificaciones de usuarios (usadas para el modelo ML)
- `CarritoCompra` / `CarritoCompraPlan` — carrito de compras
- `CompraPlan` — historial de compras

---

## Git

- **Monorepo** inicializado en la raíz del proyecto
- Los `.git` de midestinoback y midestinofront fueron eliminados
- Primer commit listo para hacer `git remote add origin <url>` y `git push`

---

## Pendientes

- [ ] Crear repositorio en GitHub y hacer `git push`
- [ ] Conectar repo en Railway y hacer primer deploy
- [ ] Agregar variable `GROQ_API_KEY` en Railway (manualmente)
- [ ] Después del deploy, actualizar `app_config.dart` con la URL real de Railway
- [ ] Confirmar que `flutter build apk --release` compila sin errores
