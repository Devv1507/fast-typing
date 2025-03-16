package com.example.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Representa el progreso de una visualización de eclipse basada en los errores cometidos.
 * Esta clase mantiene una propiedad de errores y proporciona rutas de imágenes
 * correspondientes para cada nivel de error.
 * @author David Esteban Valencia
 */
public class EclipseProgress {
    /**
     * Representa el número de errores en el contexto actual.
     * Esta variable es una propiedad observable de tipo IntegerProperty,
     * inicializada a un valor predeterminado de 0. La propiedad de errores puede
     * ser actualizada y observada para cambios.
     */
    private final IntegerProperty errors = new SimpleIntegerProperty(0);
    /**
     * Array que contiene las rutas de archivo relativas a las imágenes que representan diferentes
     * etapas de una visualización de eclipse. Cada imagen corresponde a un progreso
     * nivel específico, que va desde 0% a 100% en incrementos de 25%.
     * Estas imágenes se indexan para su recuperación según los niveles de error.
     */
    private static final String[] ECLIPSE_IMAGES = {
            "/images/eclipse_0.png",    // 0 errores - 0% eclipse
            "/images/eclipse_25.png",   // 1 error - 25% eclipse
            "/images/eclipse_50.png",   // 2 errores - 50% eclipse
            "/images/eclipse_75.png",   // 3 errores - 75% eclipse
            "/images/eclipse_100.png"   // 4 errores - 100% eclipse (eclipse total)
    };

    /**
     * Establece el número de errores.
     * @param errors el número de errores a establecer
     */
    public void setErrors(int errors) {
        this.errors.set(errors);
    }


    /**
     * Obtiene la ruta de la imagen del eclipse correspondiente a un número específico de errores.
     * @param errorCount El número de errores para el que se requiere la imagen
     * @return La ruta de la imagen del eclipse para el número de errores especificado
     */
    public String getEclipseImageForErrors(int errorCount) {
        int index = Math.min(errorCount, ECLIPSE_IMAGES.length - 1);
        return ECLIPSE_IMAGES[index];
    }
}