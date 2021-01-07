/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.maestro.lib.xmlcore.utils;

/**
 *
 * @author maestro
 */
public class PzConstantsUtils {
    public static final int STAN_NEW = 1; // Документ занесен вручную через систему
    public static final int STAN_ERR_RULE = 2; // Документ имеет ошибки
    public static final int STAN_ERR_XSD = 4; // Документ не прошел XSD валидацию
    public static final int STAN_ERR_CONTROL = 8; // Документ имеет ошибки целосности документа
}
