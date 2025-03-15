package com.example.utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

/**
 * Representa el progreso de una visualización de eclipse basada en los niveles de error.
 * Esta clase mantiene una propiedad de errores y proporciona una representación de imagen
 * correspondiente para cada nivel de error.
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
            "/images/eclipse_0.png",
            "/images/eclipse_25.png",
            "/images/eclipse_50.png",
            "/images/eclipse_75.png",
            "/images/eclipse_100.png"
    };

    /**
     * Establece el número de errores.
     * @param errors el número de errores a establecer
     */
    public void setErrors(int errors) {
        this.errors.set(errors);
    }

    /**
     * Devuelve la propiedad que representa el número de errores.
     * El valor de esta propiedad se puede observar y actualizar según sea necesario.
     * @return la propiedad de errores como un IntegerProperty.
     */
    public IntegerProperty errorsProperty() {
        return errors;
    }

    /**
     * Recupera la representación de imagen de eclipse apropiada basada en el nivel de error actual..
     * @return Un objeto Image correspondiente al nivel de error actual. Si el recuento de errores
     *  excede las imágenes disponibles, se devuelve la última imagen en el array.
     */
    public Image getEclipseImage() {
        int index = Math.min(errors.get(), ECLIPSE_IMAGES.length - 1);
        return new Image(getClass().getResourceAsStream(ECLIPSE_IMAGES[index]));
    }
}