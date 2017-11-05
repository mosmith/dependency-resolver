package org.mosmith.tools.dependency.resolve.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mosmith
 */
public class IOUtils {
    public static void closeIfNotNull(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            // ignore
        }
    }
}
