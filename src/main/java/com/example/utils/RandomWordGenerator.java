package com.example.utils;

import java.util.List;
import java.util.Random;

import com.example.interfaces.IWordGenerator;

/**
 * Clase de utilidad para generar palabras aleatorias utilizando una API externa o una lista local.
 * @author David Esteban Valencia
 */
public class RandomWordGenerator implements IWordGenerator {
    /**
     * Lista predefinida e inmodificable de Strings que representan diversas palabras del español.
     * Esta lista está diseñada para servir como una colección localizada de palabras.
     */
    private final List<String> localWords = List.of(
            "manzana", "perro", "gato", "casa", "libro", "silla", "mesa", "puerta", "ventana", "coche",
            "camión", "bicicleta", "moto", "tren", "avión", "barco", "playa", "montaña", "río", "bosque",
            "ciudad", "pueblo", "edificio", "tienda", "mercado", "hospital", "escuela", "universidad",
            "profesor", "alumno", "médico", "ingeniero", "programador", "artista", "músico", "deporte",
            "fútbol", "baloncesto", "tenis", "natación", "correr", "saltar", "bailar", "pintar", "dibujar",
            "cantar", "tocar", "escribir", "leer", "computadora", "teclado", "ratón", "pantalla", "teléfono",
            "internet", "correo", "mensaje", "amigo", "familia", "hermano", "hermana", "padre", "madre",
            "abuelo", "abuela", "primo", "prima", "tío", "tía", "vecino", "trabajo", "empresa", "dinero",
            "comida", "bebida", "pan", "queso", "leche", "agua", "café", "té", "carne", "pollo", "pescado",
            "ensalada", "fruta", "verdura", "chocolate", "helado", "dulce", "salado", "caliente", "frío",
            "verano", "invierno", "otoño", "primavera", "día", "noche", "mañana", "tarde", "hora", "minuto",
            "alegría", "tristeza", "miedo", "ira", "sorpresa", "amor", "odio", "esperanza", "deseo", "sueño",
            "mente", "cuerpo", "alma", "espíritu", "corazón", "cerebro", "nervio", "sangre", "hueso", "piel",
            "cielo", "tierra", "luna", "sol", "estrella", "planeta", "espacio", "tiempo", "vida", "muerte",
            "verdad", "mentira", "justicia", "injusticia", "paz", "guerra", "libertad", "esclavitud", "orden",
            "caos", "belleza", "fealdad", "riqueza", "pobreza", "salud", "enfermedad", "inteligencia", "ignorancia",
            "humildad", "arrogancia", "valentía", "cobardía", "amistad", "enemistad", "virtud", "vicio", "lógica",
            "intuición", "razón", "emoción", "tradición", "innovación", "conservación", "destrucción", "creación",
            "conocimiento", "sabiduría", "entendimiento", "iluminación", "oscuridad", "silencio", "ruido", "luz",
            "sombra", "calma", "tormenta", "fuerza", "debilidad", "riqueza", "miseria", "éxito", "fracaso",
            "destino", "casualidad", "oportunidad", "riesgo", "seguridad", "peligro", "control", "liberación",
            "esfuerzo", "sacrificio", "recompensa", "castigo", "perseverancia", "resignación", "compasión", "crueldad",
            "optimismo", "pesimismo", "realidad", "ilusión", "imaginación", "creatividad", "inspiración", "motivación",
            "ambición", "pereza", "responsabilidad", "irresponsabilidad", "compromiso", "abandono", "cooperación",
            "competencia", "solidaridad", "egoísmo", "generosidad", "avaricia", "confianza", "desconfianza", "lealtad",
            "traición", "respeto", "desprecio", "tolerancia", "intolerancia", "obediencia", "rebeldía", "armonía",
            "conflicto", "equilibrio", "desorden", "estabilidad", "inestabilidad", "verdad", "falsedad", "certeza",
            "duda", "conocimiento", "creencia", "ciencia", "arte", "religión", "filosofía", "literatura", "música",
            "pintura", "escultura", "arquitectura", "teatro", "cine", "fotografía", "danza", "poesía", "narrativa",
            "ensayo", "drama", "comedia", "tragedia", "historia", "biografía", "autobiografía", "memoria", "olvido",
            "presente", "pasado", "futuro", "infancia", "juventud", "adultez", "vejez", "nacimiento", "renacimiento",
            "transformación", "evolución", "revolución", "progreso", "decadencia", "renovación", "eternidad", "infinito",
            "cosmos", "universo", "galaxia", "nebulosa", "agujero negro", "cuásar", "partícula", "átomo", "molécula",
            "célula", "organismo", "ecosistema", "biósfera", "conciencia", "subconsciente", "inconsciente", "mente colectiva",
            "arquetipo", "símbolo", "metáfora", "alegoría", "paradigma", "axioma", "teorema", "hipótesis", "inducción",
            "deducción", "análisis", "síntesis", "dialéctica", "retórica", "persuasión", "argumentación", "sofisma",
            "falacia", "verdad relativa", "verdad absoluta", "escepticismo", "dogmatismo", "fanatismo", "fundamentalismo",
            "nihilismo", "existencialismo", "humanismo", "altruismo", "cinismo", "egoísmo", "hedonismo", "estoicismo",
            "epicureísmo", "pragmatismo", "idealismo", "materialismo", "racionalismo", "empirismo", "positivismo",
            "estructuralismo", "postestructuralismo", "deconstrucción", "semiótica", "hermenéutica", "fenomenología",
            "psicoanálisis", "conductismo", "cognitivismo", "lingüística", "sociología", "antropología", "economía",
            "política", "derecho", "moral", "ética", "estética", "gnoseología", "ontología", "metafísica", "teología",
            "misticismo", "esoterismo", "ocultismo", "magia", "alquimia", "astrología", "numerología", "quiromancia",
            "tarot", "I Ching", "Feng Shui", "yoga", "meditación", "mindfulness", "chamanismo", "animismo", "totemismo",
            "sincretismo", "eclecticismo", "pluralismo", "relativismo cultural", "transculturalidad", "globalización",
            "posmodernidad", "singularidad tecnológica", "transhumanismo", "inteligencia artificial", "realidad virtual",
            "ciberespacio", "nanotecnología", "biotecnología", "ingeniería genética", "clonación", "criogenización",
            "colonización espacial", "terraformación", "paradoja de Fermi", "ecuación de Drake", "principio antrópico",
            "simulación de la realidad", "teoría de cuerdas", "multiverso", "incertidumbre", "efecto mariposa",
            "catástrofe", "entropía", "negentropía", "autopoiesis", "emergencia", "sinergia", "holarquía", "fractal",
            "caos determinista", "complejidad", "sistema", "mente colmena", "memética", "viralidad",
            "influencer", "troleo", "fake news", "posverdad", "capitalismo",
            "algoritmo", "sesgo algorítmico", "ética de la IA", "neuroética", "cibercultura", "identidad digital",
            "huella digital", "analfabetismo digital", "brecha digital", "desinformación", "propaganda", "manipulación",
            "censura", "libertad de expresión", "privacidad", "vigilancia masiva", "criptografía", "blockchain",
            "descentralización", "autonomía", "soberanía digital", "commons", "copyleft", "software libre", "open source",
            "crowdsourcing", "crowdfunding", "economía colaborativa", "procomún", "diseño abierto",
            "hardware libre", "biología sintética", "hacktivismo", "activismo digital", "ciberseguridad", "guerra cibernética",
            "quinto poder", "periodismo ciudadano", "wikileaks", "anonymous", "deep web", "dark web", "bitácora",
            "epistemología","heurístico","exégesis","hermenéutica","tautología","anátema", "eutanasia",
            "paroxismo","idiosincrasia", "escéptico", "melancolía","sinergia"
    );
    private final Random random = new Random();

    /**
     * Genera una palabra aleatoria de la lista de palabras disponibles.
     * @return Una palabra seleccionada aleatoriamente.
     */
    @Override
    public String generateWord() {
        return localWords.get(random.nextInt(localWords.size()));
    }
}
