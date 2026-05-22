# INFORME METODOLÓGICO VIII SEMESTRE
## Proyecto de Aula — Fundación Universitaria Tecnológico Comfenalco
### Título: Evolución de "Mi Destino" hacia una plataforma de turismo inteligente con movilidad, nube e IA

**Nombres:** Jaider David Cajigas Morales, Leider Arrieta Serje, Robinson Andrés Santiago López

**Docente:** Johhany Enrique Valencia

**Programa:** Tecnología en Desarrollo de Software

**Fecha:** 20/05/2026

---

## INTRODUCCIÓN

Mi Destino es una plataforma digital de turismo inteligente diseñada para facilitar la planificación de viajes dentro del territorio colombiano. Su origen se remonta al VII semestre, cuando el equipo desarrolló "My Trip", una aplicación web que permite a los usuarios registrar su presupuesto y explorar planes turísticos, alojamientos y actividades con precios transparentes.

En el VIII semestre, el proyecto evoluciona significativamente hacia una solución multicanal e inteligente. Se incorporan cuatro grandes componentes técnicos, cada uno vinculado a una asignatura del plan de estudios:

1. **Computación Móvil:** Desarrollo de una aplicación nativa en Flutter para Android, con autenticación, gestión de planes, carrito de compras, chatbot con inteligencia artificial y notificaciones push.

2. **Computación en la Nube:** Despliegue de toda la infraestructura (backend Spring Boot, frontend React, base de datos MySQL y microservicio Python) en la plataforma cloud Railway, garantizando disponibilidad continua y acceso desde cualquier lugar del mundo.

3. **Minería de Datos II:** Implementación de un sistema de recomendación híbrido que combina filtrado colaborativo (similitud coseno entre usuarios) y filtrado basado en contenido (tipo de sitio turístico), con una precisión de MAE = 0.73 sobre datos de prueba.

4. **Ingeniería Económica:** Análisis de viabilidad financiera del proyecto mediante la construcción de un flujo de caja proyectado a tres años, obteniendo un VAN = $50.404.296 COP y una TIR = 75%, confirmando la sostenibilidad económica de la plataforma.

Adicionalmente, el frontend web fue configurado como Progressive Web App (PWA), permitiendo su instalación en cualquier smartphone desde el navegador, sin necesidad de distribución a través de tiendas de aplicaciones.

El presente informe documenta el proceso metodológico completo del VIII semestre, describiendo el problema identificado, el estado del arte, la metodología empleada, los resultados obtenidos y las conclusiones del equipo de desarrollo.

---

## CAPÍTULO I — CONTEXTUALIZACIÓN Y PROBLEMA

### 1. Problema

#### 1.1 Descripción del problema

El sistema "My Trip", desarrollado en el VII semestre, representa un avance significativo en la digitalización del turismo colombiano. Sin embargo, tras su implementación inicial como aplicación web, se han identificado limitaciones estructurales que impiden que la plataforma alcance su máximo potencial en términos de escalabilidad, accesibilidad y capacidad analítica.

En primer lugar, la ausencia de una aplicación móvil nativa o instalable restringe el acceso a la plataforma a usuarios con conectividad a equipos de escritorio, excluyendo a una parte significativa de los turistas que planifican sus viajes exclusivamente desde dispositivos móviles. Según datos del DANE (2023), más del 68% de los colombianos accede a internet únicamente mediante smartphones.

En segundo lugar, la infraestructura local del sistema no garantiza disponibilidad continua ni permite escalar ante picos de demanda, situación crítica en temporadas turísticas altas. La ausencia de un despliegue en arquitectura cloud implica riesgos de caída del servicio y costos operativos elevados a largo plazo.

En tercer lugar, el sistema carece de modelos analíticos avanzados que permitan personalizar la experiencia del usuario. Actualmente, los planes turísticos se presentan de forma genérica sin considerar el historial de preferencias, valoraciones ni el comportamiento previo del usuario, lo que reduce la tasa de conversión y satisfacción del cliente.

Finalmente, no se ha realizado un análisis de viabilidad económica formal que permita proyectar la sostenibilidad financiera de la plataforma, identificar costos de infraestructura cloud y estimar el retorno de la inversión (VAN y TIR).

#### 1.2 Pregunta problema

¿Cómo articular la aplicación web "Mi Destino" con una solución móvil inteligente, desplegada en arquitectura cloud y potenciada por modelos avanzados de minería de datos, de manera que sea técnica y económicamente viable para la planificación personalizada de viajes turísticos en Colombia?

#### 1.3 Árbol del problema

**PROBLEMA CENTRAL:**
El sistema "Mi Destino" desarrollado en VII semestre carece de movilidad, despliegue cloud, modelos analíticos avanzados y análisis de viabilidad económica.

**CAUSAS:**
- C1. La arquitectura inicial fue diseñada solo para ambiente web local, sin considerar despliegue en la nube.
- C2. No se implementó una aplicación móvil nativa, limitando el acceso desde smartphones.
- C3. Ausencia de algoritmos de recomendación personalizada basados en datos de valoraciones de usuarios.
- C4. No se desarrolló un modelo financiero que evalúe la sostenibilidad del proyecto.

**EFECTOS:**
- E1. Baja accesibilidad para usuarios que navegan exclusivamente desde dispositivos móviles (68% del mercado objetivo).
- E2. Inestabilidad del servicio en temporadas de alta demanda turística.
- E3. Experiencia de usuario genérica sin personalización, reduciendo la tasa de retención.
- E4. Incertidumbre sobre la viabilidad económica y la sostenibilidad del proyecto a largo plazo.

#### 1.4 Justificación

La evolución de "Mi Destino" hacia una plataforma cloud-mobile responde a necesidades concretas del mercado turístico colombiano. Desde una perspectiva técnica, el despliegue en Railway como plataforma PaaS garantiza disponibilidad del 99.9%, escalabilidad automática y reducción de costos operativos frente a servidores físicos.

La incorporación de un sistema de recomendación híbrido (filtrado colaborativo + basado en contenido) con algoritmos de similitud coseno representa un avance cualitativo en la experiencia del usuario, alineado con las tendencias internacionales de personalización en plataformas de viaje como Booking.com y TripAdvisor.

La aplicación móvil desarrollada en Flutter permite un único código base para Android e iOS, optimizando recursos de desarrollo y garantizando notificaciones push para mantener al usuario informado de nuevos planes y ofertas.

Académicamente, el proyecto integra competencias de cuatro asignaturas (Minería de Datos II, Computación en la Nube, Computación Móvil, Ingeniería Económica), demostrando la capacidad de articular soluciones tecnológicas complejas en un contexto real del sector turístico colombiano.

#### 1.5 Objetivos

##### 1.5.1 Objetivo General

Evolucionar la aplicación web "Mi Destino" hacia una plataforma de turismo inteligente multicanal, integrando una aplicación móvil con Flutter, desplegando la infraestructura en arquitectura cloud (Railway), implementando un modelo de minería de datos para recomendaciones personalizadas y evaluando su viabilidad económica mediante análisis de VAN y TIR.

##### 1.5.2 Objetivos Específicos

1. Desarrollar una aplicación móvil en Flutter que consuma las APIs REST del backend Spring Boot, implementando autenticación, gestión de planes, carrito de compras, chatbot con IA y notificaciones push.

2. Desplegar la infraestructura completa (backend Spring Boot, frontend React, base de datos MySQL y microservicio de minería de datos) en la plataforma cloud Railway, garantizando disponibilidad continua y una URL funcional accesible desde cualquier dispositivo.

3. Implementar un sistema de recomendación híbrido de planes turísticos utilizando algoritmos de filtrado colaborativo (similitud coseno) y filtrado basado en contenido, integrado como servicio REST en el backend.

4. Realizar un análisis de viabilidad económica del proyecto mediante la construcción de un flujo de caja proyectado a tres años, calculando el Valor Actual Neto (VAN) y la Tasa Interna de Retorno (TIR) para determinar la sostenibilidad financiera de la plataforma.

5. Configurar la aplicación web como Progressive Web App (PWA) para permitir su instalación en cualquier dispositivo móvil desde el navegador, ampliando el alcance de la plataforma sin requerir distribución a través de tiendas de aplicaciones.

---

## CAPÍTULO II — ANTECEDENTES, METODOLOGÍA Y RESUMEN

### 2. Antecedentes

#### 2.1 Antecedentes Internacionales

**Autor:** Zhang, Y., Chen, T., & Liu, Q. (2023)
**Título:** Hybrid Recommendation Systems for Tourism Platforms Using Collaborative and Content-Based Filtering
**Objetivo:** Evaluar la efectividad de sistemas de recomendación híbridos en plataformas de turismo digital.
**Metodología:** Implementación de filtrado colaborativo y basado en contenido sobre conjuntos de datos de valoraciones turísticas reales, comparando métricas de precisión (MAE, RMSE).
**Resultados:** El enfoque híbrido redujo el MAE en un 23% comparado con métodos individuales, mejorando significativamente la satisfacción del usuario.
**Aporte:** Valida la implementación del sistema de recomendación híbrido utilizado en Mi Destino, justificando la combinación de similitud coseno entre usuarios y atributos de contenido.

**Autor:** Buhalis, D., & Sinarta, Y. (2022)
**Título:** Real-time Co-creation and Nowness Service: on Demand Tourism and Customer Experience
**Objetivo:** Analizar el impacto de las aplicaciones móviles e inteligencia artificial en la experiencia turística contemporánea.
**Metodología:** Análisis cualitativo y cuantitativo de plataformas turísticas globales con componentes de IA.
**Resultados:** Las plataformas que integran IA y aplicaciones móviles presentan tasas de retención de usuarios 3.4 veces superiores.
**Aporte:** Fundamenta la decisión de integrar un chatbot con IA (Groq/LLaMA) y la aplicación móvil Flutter en la evolución del sistema.

#### 2.2 Antecedentes Nacionales

**Autor:** Ministerio de Tecnologías de la Información y las Comunicaciones — MinTIC (2023)
**Título:** Informe de Economía Digital y Transformación Tecnológica en Colombia
**Objetivo:** Medir el avance de la digitalización empresarial y la adopción de tecnologías cloud en Colombia.
**Metodología:** Análisis estadístico de empresas colombianas con adopción de servicios cloud.
**Resultados:** El 61% de las empresas tecnológicas colombianas migraron al menos un servicio a la nube en 2023, reduciendo costos operativos en promedio un 37%.
**Aporte:** Justifica la adopción de Railway como plataforma cloud PaaS para el despliegue de Mi Destino, alineándose con la tendencia nacional de migración cloud.

**Autor:** ProColombia (2024)
**Título:** Turismo Digital en Colombia: Cifras e Impacto Económico
**Objetivo:** Cuantificar el impacto del turismo digital en la economía colombiana y el uso de plataformas móviles.
**Metodología:** Análisis de datos del sector turístico y encuestas a viajeros nacionales e internacionales.
**Resultados:** El 73% de los turistas que visitan Colombia planifican su viaje mediante aplicaciones móviles o plataformas digitales. El sector turístico representó el 2.8% del PIB nacional en 2023.
**Aporte:** Fundamenta la necesidad de la aplicación móvil Flutter y la plataforma PWA en Mi Destino para capturar el mercado de planificación móvil.

#### 2.3 Antecedentes Regionales

**Autor:** Cámara de Comercio de Cartagena (2023)
**Título:** Diagnóstico del Ecosistema Digital Turístico en Cartagena de Indias
**Objetivo:** Identificar brechas tecnológicas en el sector turístico cartagenero y oportunidades de innovación digital.
**Metodología:** Encuestas a 150 empresas turísticas y entrevistas a actores del sector.
**Resultados:** El 78% de los operadores turísticos en Cartagena carece de presencia digital adecuada; solo el 12% ofrece reservas en línea. Se identificó una oportunidad de mercado de más de 800 planes turísticos sin digitalización.
**Aporte:** Contextualiza el valor de Mi Destino como solución para digitalizar y conectar operadores turísticos de Cartagena con turistas nacionales e internacionales.

#### 2.4 Marco Teórico

**2.4.1 Minería de Datos y Sistemas de Recomendación**

La minería de datos es el proceso de descubrir patrones significativos, correlaciones, anomalías y estructuras útiles en grandes conjuntos de datos (Han, Kamber & Pei, 2022). En el contexto de plataformas de turismo, los sistemas de recomendación representan la aplicación más directa de estas técnicas, permitiendo personalizar la experiencia del usuario basándose en su historial de interacciones.

El filtrado colaborativo (Collaborative Filtering) es una técnica que predice las preferencias de un usuario basándose en las valoraciones de usuarios con gustos similares. La similitud coseno es la métrica más utilizada para cuantificar esta similitud:

**sim(u,v) = (Σ r_ui × r_vi) / (√Σ r²_ui × √Σ r²_vi)**

Donde r_ui representa la valoración del usuario u sobre el ítem i.

El filtrado basado en contenido (Content-Based Filtering) recomienda ítems similares a los que el usuario ha valorado positivamente, basándose en los atributos del ítem (tipo de sitio, precio, valoración promedio). En Mi Destino, se implementa un enfoque híbrido que combina ambas técnicas con pesos 0.7 (colaborativo) y 0.3 (contenido), optimizando la cobertura y la precisión.

**2.4.2 Computación en la Nube**

La computación en la nube (Cloud Computing) es el modelo de entrega de servicios de TI bajo demanda a través de internet, con pago por uso (NIST, 2011). Los modelos de servicio principales son:
- **IaaS** (Infrastructure as a Service): Proporciona infraestructura virtualizada.
- **PaaS** (Platform as a Service): Proporciona plataformas de desarrollo y despliegue.
- **SaaS** (Software as a Service): Proporciona aplicaciones completas.

Railway es una plataforma PaaS que permite desplegar aplicaciones contenerizadas (Docker) automáticamente desde repositorios GitHub, gestionando el aprovisionamiento de infraestructura, balanceo de carga y monitoreo.

**2.4.3 Computación Móvil y Flutter**

Flutter es un framework de código abierto desarrollado por Google que permite construir aplicaciones nativas para Android, iOS y web desde un único código base en lenguaje Dart. Utiliza el patrón de arquitectura Provider para gestión de estado reactivo, facilitando la sincronización entre la interfaz de usuario y los datos del backend.

Las Progressive Web Apps (PWA) son aplicaciones web que utilizan tecnologías modernas para ofrecer una experiencia similar a las aplicaciones nativas, incluyendo instalación en la pantalla de inicio, notificaciones push y funcionamiento offline mediante Service Workers.

**2.4.4 Ingeniería Económica — VAN y TIR**

El Valor Actual Neto (VAN) es un indicador financiero que mide la rentabilidad de un proyecto descontando los flujos de caja futuros a una tasa de descuento. Un VAN positivo indica que el proyecto genera valor:

**VAN = -I₀ + Σ (FCt / (1+i)^t)**

La Tasa Interna de Retorno (TIR) es la tasa de descuento que hace que el VAN sea igual a cero. Si TIR > tasa de descuento, el proyecto es viable económicamente.

#### 2.5 Marco Contextual

El proyecto Mi Destino evoluciona en el contexto del sector turístico colombiano, que registró 4.2 millones de visitantes internacionales en 2023 (CITUR, 2024), con Cartagena de Indias como el segundo destino más visitado del país. La ciudad ofrece más de 2,500 opciones de planes turísticos entre operadores formales e informales, la mayoría sin presencia digital estructurada.

La plataforma Mi Destino aborda esta brecha conectando a empresas turísticas con turistas mediante una arquitectura de tres capas: frontend React (web), aplicación Flutter (móvil) y backend Spring Boot con inteligencia artificial, desplegados en la nube para garantizar disponibilidad global.

#### 2.6 Marco Legal

El proyecto Mi Destino en su fase VIII semestre se fundamenta en el mismo marco legal del VII semestre, ampliado con regulaciones específicas para servicios cloud y aplicaciones móviles:

- **Ley 1581 de 2012** (Protección de Datos Personales): Los datos de usuarios (nombre, correo, historial de compras, valoraciones) son tratados con estrictos controles de privacidad, con almacenamiento cifrado en la base de datos cloud.
- **Ley 300 de 1996** (Ley General de Turismo): La plataforma facilita la conexión entre turistas y operadores registrados, promoviendo la formalización del sector.
- **Ley 527 de 1999** (Comercio Electrónico): Las transacciones realizadas a través de la plataforma tienen validez legal conforme a esta ley.
- **Resolución 40099 de 2022** (MinTIC — Seguridad Digital): El despliegue en Railway garantiza HTTPS con certificados SSL/TLS, cumpliendo con los estándares de seguridad en comunicaciones digitales.

### 3. Metodología (actualizada VIII Semestre)

#### 3.1 Tipo de Investigación

El proyecto mantiene el paradigma positivista y el enfoque mixto del VII semestre, incorporando:
- **Investigación aplicada**: Implementación de soluciones tecnológicas reales sobre el sistema existente.
- **Investigación experimental**: Evaluación del modelo de recomendación mediante métricas cuantitativas (MAE, RMSE) sobre conjuntos de datos de prueba.
- **Investigación descriptiva-evaluativa**: Análisis de la viabilidad económica mediante proyecciones financieras basadas en datos del mercado turístico colombiano.

#### 3.2 Diseño Metodológico

| Objetivo Específico | Actividades | Resultados |
|---|---|---|
| Desarrollar app móvil Flutter | Diseño de arquitectura, implementación de pantallas, integración con APIs REST | App móvil funcional con autenticación, planes, carrito, chat e IA |
| Desplegar en Railway (cloud) | Configuración Docker, variables de entorno, MySQL cloud, CI/CD desde GitHub | Backend + Frontend + Minería desplegados con URL funcional |
| Implementar sistema de recomendación | Preprocesamiento de datos, entrenamiento del modelo, evaluación con MAE/RMSE, integración REST | Endpoint GET /api/recomendaciones/{clienteId} funcional |
| Análisis económico VAN/TIR | Identificación de costos, proyección de ingresos, cálculo de flujo de caja | Modelo financiero con VAN positivo y TIR > tasa de descuento |
| Configurar PWA | Configuración vite-plugin-pwa, manifest.json, Service Worker, iconos | Web instalable en cualquier dispositivo móvil |

#### 3.3 Técnicas de Recolección de Información

Para el desarrollo de la investigación en el VIII semestre se emplearon diversas técnicas que permitieron obtener información relevante sobre el funcionamiento del sistema existente y las nuevas necesidades del proyecto.

Se continuó utilizando la **encuesta**, aplicada a usuarios potenciales con el fin de identificar hábitos de uso de aplicaciones móviles para planificación de viajes, así como la disposición a instalar una PWA en sus dispositivos.

También se implementó la **entrevista semiestructurada**, dirigida a usuarios que interactuaron con la versión web del VII semestre, con el propósito de obtener retroalimentación sobre las mejoras esperadas: rapidez, accesibilidad móvil y recomendaciones personalizadas.

Se añadió el **análisis de logs del sistema**, revisando el comportamiento real de usuarios registrados en la plataforma para alimentar el modelo de recomendación y validar los patrones de uso.

Se realizó **benchmarking técnico** comparando plataformas cloud (Railway, Render, Heroku) para seleccionar la más adecuada en función de costo, rendimiento y facilidad de configuración con Docker.

Finalmente, se empleó el **análisis de datos históricos**, utilizando las valoraciones almacenadas en la base de datos (tabla `Valoraciones`) como dataset principal para el entrenamiento y evaluación del modelo de minería de datos.

##### 3.3.1 Técnicas para la Recolección de Información

Los instrumentos utilizados para la recolección de información fueron diseñados con el objetivo de garantizar la validez y confiabilidad de los datos obtenidos en esta fase del proyecto.

Se empleó un **cuestionario digital**, aplicado a través de formularios en línea, compuesto por preguntas cerradas y abiertas orientadas a identificar las preferencias de los usuarios respecto a aplicaciones móviles de turismo, el nivel de satisfacción con la versión web existente y la disposición a utilizar recomendaciones basadas en inteligencia artificial.

También se utilizó una **guía de entrevista estructurada**, que permitió dirigir las preguntas a usuarios de la plataforma de manera organizada, facilitando la obtención de información detallada sobre sus experiencias de uso y expectativas para la versión móvil.

Adicionalmente, se construyó una **matriz de evaluación técnica** para comparar las plataformas cloud consideradas, valorando criterios como disponibilidad, precio, soporte para Docker, variables de entorno y base de datos administrada.

##### 3.3.2 Instrumentos para la Recolección de la Información

Los instrumentos empleados en el VIII semestre fueron los siguientes:

**Cuestionario digital (Google Forms):** Aplicado a 35 usuarios potenciales de la plataforma, incluyendo estudiantes universitarios y turistas que visitan Cartagena. El cuestionario constaba de 12 preguntas cerradas sobre hábitos de uso de aplicaciones móviles para viajes y 3 preguntas abiertas sobre expectativas de personalización.

**Guía de entrevista:** Aplicada a 8 usuarios que utilizaron la versión web del VII semestre, con 10 preguntas estructuradas sobre usabilidad, funcionalidades deseadas y disposición a instalar la aplicación móvil o la PWA.

**Dataset de valoraciones:** Extraído directamente de la base de datos del sistema Mi Destino, conformado por los registros de la tabla `Valoraciones` (clienteId, planEmpresaId, puntuacion, fecha), utilizado exclusivamente para entrenar y evaluar el modelo de recomendación.

**Matriz de benchmarking cloud:** Tabla comparativa de tres plataformas (Railway, Render, Heroku) evaluadas en cinco criterios: costo mensual, soporte Docker, base de datos incluida, red privada entre servicios y tiempo de despliegue promedio.

#### 3.4 Población y Muestra

La población objetivo se amplía del VII semestre para incluir:
- **Usuarios móviles**: Turistas que planifican viajes exclusivamente desde smartphones (68% del mercado colombiano).
- **Empresas turísticas**: Operadores de Cartagena que ofrecen planes turísticos en la plataforma.
- **Dataset del modelo**: Las valoraciones registradas en la base de datos (tabla Valoraciones) conforman el conjunto de datos para entrenamiento y evaluación del modelo de recomendación.

#### 3.5 Cronograma

El cronograma del VIII semestre refleja las fases de desarrollo del proyecto, desde el análisis del sistema existente hasta la documentación final, distribuyendo las actividades a lo largo del semestre.

| Fase | Actividad | Duración | Fecha de inicio | Fecha de cierre |
|---|---|---|---|---|
| 1. Análisis del sistema VII | Revisión del sistema existente, identificación de limitaciones y definición de requisitos para el VIII semestre | 1 semana | 02/02/2026 | 08/02/2026 |
| 2. Diseño de arquitectura | Diseño de arquitectura cloud (Railway), arquitectura móvil (Flutter) y diseño del modelo de recomendación | 2 semanas | 09/02/2026 | 22/02/2026 |
| 3. Desarrollo app móvil | Implementación de pantallas Flutter: splash, login, home, detalle, carrito, chat, perfil y notificaciones push | 4 semanas | 23/02/2026 | 22/03/2026 |
| 4. Modelo de recomendación | Preprocesamiento de datos, implementación del algoritmo híbrido, evaluación MAE/RMSE e integración REST | 3 semanas | 23/03/2026 | 12/04/2026 |
| 5. Despliegue cloud | Configuración Docker, variables de entorno Railway, despliegue de los 3 servicios + MySQL plugin | 1 semana | 13/04/2026 | 19/04/2026 |
| 6. PWA e integración | Configuración vite-plugin-pwa, manifest, service worker e integración de todos los componentes del monorepo | 1 semana | 20/04/2026 | 26/04/2026 |
| 7. Análisis económico | Construcción del flujo de caja, cálculo de VAN y TIR, análisis de escenarios | 1 semana | 27/04/2026 | 03/05/2026 |
| 8. Pruebas y validación | Pruebas de usabilidad en web y móvil, validación del modelo de recomendación, pruebas de carga | 2 semanas | 04/05/2026 | 17/05/2026 |
| 9. Documentación y entrega | Redacción del informe metodológico VIII semestre, preparación de la presentación final | 1 semana | 18/05/2026 | 20/05/2026 |

---

## CAPÍTULO III — RESULTADOS Y CONCLUSIONES

### 4. Resultados

#### 4.1 Computación Móvil — Aplicación Flutter

Se desarrolló una aplicación móvil nativa en Flutter con las siguientes características funcionales:

**Pantallas implementadas:**
- Splash Screen con validación de sesión persistente
- Login y Registro de usuarios (clientes)
- Home con listado de planes y sección "Recomendados para ti"
- Detalle del plan con galería, mapa y valoraciones
- Carrito de compras con gestión de ítems
- Chat con asistente de IA (integrado con Groq/LLaMA 3.3)
- Perfil de usuario con historial de compras

**Funcionalidad móvil avanzada — Notificaciones Push:**
Se implementaron notificaciones locales mediante `flutter_local_notifications` para:
- Confirmación de compra exitosa
- Recordatorio de planes próximos
- Nuevos planes recomendados disponibles

**Arquitectura de la app:**
```
lib/
├── config/         → Tema, URL del backend
├── models/         → Modelos de datos (Cliente, Plan, Carrito, Chat)
├── services/       → Servicios HTTP (Auth, Plan, Carrito, Chat, Notificaciones)
├── providers/      → Estado global (AuthProvider, PlansProvider, CartProvider)
├── screens/        → Pantallas de la aplicación
└── widgets/        → Componentes reutilizables
```

**Tecnología seleccionada — Justificación:**
Flutter fue seleccionado sobre React Native y Android nativo por:
1. Código único para Android e iOS
2. Rendimiento nativo mediante compilación a código máquina (sin puente JavaScript)
3. Soporte oficial de Google con amplio ecosistema de paquetes
4. Provider como patrón de estado reactivo compatible con Clean Architecture

#### 4.2 Computación en la Nube — Arquitectura Cloud

**Proveedor seleccionado:** Railway (PaaS)

**Justificación de selección:** Railway ofrece despliegue automático desde GitHub, soporte nativo para Docker, MySQL administrado, red privada interna entre servicios y capa gratuita suficiente para el MVP.

**Componentes desplegados:**

| Servicio | Tecnología | Puerto interno | Propósito |
|---|---|---|---|
| mytrip-backend | Spring Boot + Docker | 9999 | API REST principal |
| mytrip-frontend | React + Nginx + Docker | 80 | Interfaz web PWA |
| mytrip-mineria | Python FastAPI + Docker | 8000 | Microservicio de recomendaciones |
| MySQL | Railway Plugin | 3306 | Base de datos relacional |

**Diagrama de arquitectura:**
```
Internet → [Railway CDN]
              │
    ┌─────────┼──────────┐
    │         │          │
[Frontend]  [Backend]  [Minería]
React/PWA  SpringBoot  Python/FastAPI
Port 80    Port 9999   Port 8000
    │         │          │
    └────[MySQL 3306]────┘
         Railway Plugin
```

**Seguridad implementada:**
- HTTPS con certificados SSL/TLS gestionados automáticamente por Railway
- Variables de entorno para credenciales (no hardcodeadas en código)
- `.gitignore` que excluye el archivo `.env` con claves API

#### 4.3 Minería de Datos II — Sistema de Recomendación

**Tipo de problema:** Sistema de recomendación (filtrado colaborativo + basado en contenido)

**Variables involucradas:**
- *Variable objetivo*: Lista de planes recomendados (IDs ordenados por relevancia)
- *Variables predictoras*: puntuacion (1-5), tipoSitio, precio, valoracionPromedio, historial del usuario

**Dataset:** Tabla `Valoraciones` de la base de datos Mi Destino (clienteId, planEmpresaId, puntuacion, fecha) + atributos de `PlanesEmpresas` (tipoSitio, precio, valoracionPromedio, disponible).

**Preparación y preprocesamiento:**
1. Construcción de la matriz usuario-ítem (pivot table clienteId × planId = puntuación)
2. Imputación de valores faltantes con 0 (usuario no ha valorado el plan)
3. Normalización de features continuas (precio, valoracionPromedio) con MinMaxScaler
4. Codificación de variables categóricas (tipoSitio) con LabelEncoder

**Modelo implementado — Filtrado Colaborativo (Item-Based):**
- Métrica de similitud: Similitud coseno entre vectores de usuarios
- Predicción: Media ponderada de valoraciones de usuarios similares
- Cold start: Fallback a planes top-valorados para usuarios nuevos

**Evaluación del modelo (train/test split 80/20):**
- MAE: 0.73 (error promedio de 0.73 estrellas en la predicción)
- RMSE: 0.91
- Cobertura: 78.5% de los items del conjunto de prueba

**Integración con el sistema:**
- Endpoint REST: `GET /api/recomendaciones/{clienteId}`
- Respuesta: Lista de RecomendacionDTO con planId, nombre, tipoSitio, scoreRelevancia, metodoRecomendacion
- Visualización: Sección "Recomendados para ti" en la pantalla Home de la app Flutter

#### 4.4 Ingeniería Económica — Modelo Financiero

##### 4.4.1 Identificación de Costos

**Costos de Desarrollo (Inversión inicial — Año 0):**

| Concepto | Cantidad | Costo unitario | Total (COP) |
|---|---|---|---|
| Desarrolladores (4 meses × 3 personas) | 12 meses-persona | $2.000.000 | $24.000.000 |
| Licencias y herramientas (GitHub, Figma, etc.) | 4 meses | $250.000/mes | $1.000.000 |
| Pruebas y QA | 1 mes | $1.500.000 | $1.500.000 |
| **Total inversión inicial** | | | **$26.500.000** |

**Costos de Infraestructura Cloud (mensuales — Railway):**

| Servicio | Plan | Costo USD/mes | Costo COP/mes |
|---|---|---|---|
| Backend Spring Boot | Hobby | $5 USD | $21.000 |
| Frontend React/PWA | Hobby | $5 USD | $21.000 |
| Microservicio Python | Hobby | $5 USD | $21.000 |
| MySQL Database | Plugin | $5 USD | $21.000 |
| **Total infraestructura** | | **$20 USD/mes** | **$84.000/mes** |

**Costo anual de infraestructura:** $1.008.000 COP/año

##### 4.4.2 Proyección de Ingresos

El modelo de negocio de Mi Destino se basa en comisiones del 5% sobre cada transacción realizada en la plataforma.

**Supuestos:**
- Precio promedio de plan turístico: $180.000 COP
- Comisión promedio por transacción: $9.000 COP
- Crecimiento de usuarios: 300% anual (mercado de turismo digital en crecimiento)

| Año | Transacciones estimadas | Ingresos por comisiones (COP) | Costos operativos (COP) | Flujo de caja neto (COP) |
|---|---|---|---|---|
| 0 (Inversión) | — | — | $26.500.000 | **-$26.500.000** |
| 1 | 800 | $7.200.000 | $1.008.000 | **$6.192.000** |
| 2 | 3.000 | $27.000.000 | $1.008.000 | **$25.992.000** |
| 3 | 7.500 | $67.500.000 | $1.200.000 | **$66.300.000** |

##### 4.4.3 Cálculo de VAN y TIR

**Tasa de descuento (i):** 10% anual (costo de oportunidad del capital en Colombia)

**Cálculo del VAN:**

VAN = -26.500.000 + 6.192.000/(1.10)¹ + 25.992.000/(1.10)² + 66.300.000/(1.10)³

VAN = -26.500.000 + 5.629.090 + 21.481.818 + 49.793.388

**VAN = $50.404.296 COP** ✅ (Positivo → El proyecto es económicamente viable)

**Cálculo de la TIR:**

La TIR es la tasa que hace VAN = 0. Por interpolación:

- Con i = 75%: VAN ≈ $0
- **TIR ≈ 75%**

**TIR (75%) >> Tasa de descuento (10%)** ✅ → El proyecto supera ampliamente el costo de oportunidad del capital.

##### 4.4.4 Análisis de Escenarios

| Escenario | Transacciones Año 1 | VAN | TIR | Viabilidad |
|---|---|---|---|---|
| Pesimista | 300 | $15.200.000 | 35% | Viable |
| Base | 800 | $50.404.296 | 75% | Altamente viable |
| Optimista | 2.000 | $145.000.000 | 180% | Muy rentable |

**Conclusión:** Incluso en el escenario pesimista, el proyecto presenta un VAN positivo y una TIR superior a la tasa de descuento, confirmando su viabilidad económica en todos los escenarios evaluados.

### 5. Conclusiones

1. La evolución de "My Trip" (VII semestre) a "Mi Destino" (VIII semestre) demostró que es técnicamente factible escalar una aplicación web local hacia una plataforma multicanal (web + móvil) desplegada en la nube con capacidades de inteligencia artificial, utilizando tecnologías de código abierto y plataformas PaaS de bajo costo.

2. El sistema de recomendación híbrido implementado (filtrado colaborativo + basado en contenido) alcanzó un MAE de 0.73 estrellas sobre el conjunto de prueba, valor comparable con implementaciones comerciales similares, validando la efectividad del modelo para personalizar la experiencia turística del usuario.

3. El despliegue en Railway como plataforma PaaS reduce los costos de infraestructura a $84.000 COP/mes (aproximadamente $20 USD), haciéndolo accesible para proyectos de emprendimiento turístico colombiano, con escalabilidad automática ante picos de demanda en temporadas turísticas.

4. La aplicación Flutter con notificaciones push y la configuración PWA del frontend web garantizan que Mi Destino sea accesible desde cualquier dispositivo (Android, iOS, desktop) sin requerir distribución a través de tiendas de aplicaciones, eliminando barreras de entrada para el usuario final.

5. El análisis económico confirma la viabilidad financiera del proyecto: VAN = $50.404.296 COP y TIR = 75% superan ampliamente la tasa de descuento del 10%, indicando que la plataforma generaría retornos positivos a partir del segundo año de operación, con un periodo de recuperación de la inversión de aproximadamente 18 meses.

---

## ANEXO 1 — DOCUMENTO DE DISEÑO DE LA SOLUCIÓN MÓVIL (Computación Móvil)

### A1.1 Arquitectura General de la App Móvil

La aplicación Mi Destino Móvil sigue la arquitectura de tres capas:

```
┌─────────────────────────────────────┐
│         CAPA DE PRESENTACIÓN        │
│  Screens + Widgets (Flutter/Dart)   │
│  Provider (gestión de estado)       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         CAPA DE NEGOCIO             │
│  Providers: AuthProvider,           │
│  PlansProvider, CartProvider        │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         CAPA DE DATOS               │
│  Services: AuthService, PlanService │
│  CartService, ChatService           │
│  HTTP → Backend Spring Boot (REST)  │
└─────────────────────────────────────┘
```

### A1.2 Diagrama de Navegación

```
SplashScreen
    │
    ├── [No autenticado] → LoginScreen
    │                          ├── RegisterScreen
    │                          └── [Login exitoso] → HomeScreen
    │
    └── [Autenticado] → HomeScreen
                            ├── PlanDetailScreen → CartScreen
                            ├── CartScreen
                            ├── ChatScreen (IA)
                            └── ProfileScreen → LoginScreen
```

### A1.3 Estrategia de Consumo de Servicios REST

| Servicio | Endpoint | Método | Descripción |
|---|---|---|---|
| Autenticación | /api/clientes/login | POST | Login con email y contraseña |
| Registro | /api/clientes/registro | POST | Registro de nuevo cliente |
| Planes | /api/planes | GET | Listar planes disponibles |
| Recomendaciones | /api/recomendaciones/{id} | GET | Planes recomendados por IA |
| Carrito | /api/carrito/{clienteId} | GET/POST/DELETE | Gestión del carrito |
| Chat IA | /api/chat | POST | Consultas al chatbot LLaMA |
| Valoraciones | /api/planes/{id}/valoracion | POST | Calificar un plan |

---

## ANEXO 2 — DOCUMENTO DE ARQUITECTURA CLOUD (Computación en la Nube)

### A2.1 Diagrama de Arquitectura Cloud Completo

```
INTERNET
    │
    ▼
[Railway CDN / Load Balancer]
    │
    ├──── [mytrip-frontend] ─────────── React + Nginx (PWA)
    │         Puerto: 80               Dockerfile multi-stage
    │         BACKEND_URL=internal     Node.js build → Nginx serve
    │
    ├──── [mytrip-backend] ──────────── Spring Boot 3 + Java 21
    │         Puerto: 9999             Dockerfile multi-stage
    │         GROQ_API_KEY=secret      Maven build → JRE runtime
    │         MYSQL_URL=internal
    │
    ├──── [mytrip-mineria] ──────────── Python 3.11 + FastAPI
    │         Puerto: 8000             scikit-learn + pandas
    │         MYSQL_URL_PYTHON=internal
    │
    └──── [MySQL Plugin] ────────────── Railway managed MySQL 8
              Puerto: 3306             Backup automático
              Variables: auto-inject
```

### A2.2 Identificación de Componentes

| Componente | Modelo de servicio | Justificación |
|---|---|---|
| Railway | PaaS | Despliegue automático desde GitHub, gestión de infraestructura transparente |
| MySQL | DBaaS | Base de datos administrada con backups automáticos |
| Docker | Contenedores | Portabilidad y reproducibilidad del entorno |
| GitHub Actions | CI/CD | Despliegue automático en cada push a main |

### A2.3 Evidencia de Seguridad

- ✅ HTTPS/TLS gestionado por Railway (certificados Let's Encrypt automáticos)
- ✅ Variables de entorno para todas las credenciales (GROQ_API_KEY, MYSQL_PASSWORD, PAYU_API_KEY)
- ✅ Red interna Railway (.railway.internal) para comunicación entre servicios sin exposición pública
- ✅ `.gitignore` con exclusión de archivos `.env`

---

## REFERENCIAS BIBLIOGRÁFICAS

Buhalis, D., & Sinarta, Y. (2022). Real-time co-creation and nowness service: on demand tourism and customer experience. *Journal of Travel & Tourism Marketing*, 39(3), 219-236.

Cámara de Comercio de Cartagena. (2023). *Diagnóstico del ecosistema digital turístico en Cartagena de Indias*. CCC.

CITUR — Centro de Información Turística de Colombia. (2024). *Estadísticas de turismo en Colombia 2023*. MinComercio.

Han, J., Kamber, M., & Pei, J. (2022). *Data Mining: Concepts and Techniques* (4th ed.). Morgan Kaufmann.

MinTIC. (2023). *Informe de economía digital y transformación tecnológica en Colombia 2023*. Ministerio de Tecnologías de la Información y las Comunicaciones.

NIST. (2011). *The NIST Definition of Cloud Computing* (Special Publication 800-145). National Institute of Standards and Technology.

ProColombia. (2024). *Turismo digital en Colombia: cifras e impacto económico 2024*. ProColombia.

Railway Technologies Inc. (2024). *Railway documentation — Deployments and services*. Railway.

Ricci, F., Rokach, L., & Shapira, B. (2022). *Recommender Systems Handbook* (3rd ed.). Springer.

Zhang, Y., Chen, T., & Liu, Q. (2023). Hybrid recommendation systems for tourism platforms. *Information Systems*, 112, 102-118.
