/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import org.mosmith.tools.dependency.resolve.util.IOUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javassist.ClassPool;
import javassist.CtClass;

/**
 *
 * @author mosmith
 */
public class JarResolver {
    
    private static final String CLASS_SUFFIX=".class";
    
    private ClasspathEntry classpathEntry;
    private File file;
    
    public JarResolver(File jarFile) {
        this.file=jarFile;
        this.classpathEntry= new ClasspathEntry();
        // this.classpathEntry.setName(file.getAbsolutePath());
        this.classpathEntry.setName(file.getName());
    }
    
    public ClasspathEntry getClasspathEntry() {
        return classpathEntry;
    }
    
    public void resolve() throws IOException {
        JarFile jarFile=new JarFile(this.file);
        try{
            Enumeration<JarEntry> jarEntries=jarFile.entries();
            while(jarEntries.hasMoreElements()) {
                try{
                    JarEntry jarEntry=jarEntries.nextElement();

                    if(!jarEntry.getName().endsWith(CLASS_SUFFIX)) {
                        continue;
                    }

                    InputStream is = jarFile.getInputStream(jarEntry);
                    ClassPool classPool=ClassPool.getDefault();
                    CtClass ctClass=classPool.makeClass(is);

                    String className=ctClass.getName();
                    Collection<String> refClassNames=ctClass.getRefClasses();
                    ctClass.detach();

                    classpathEntry.addDefinedClass(className);
                    classpathEntry.addRefClasses(refClassNames);
                } catch (Exception e) {
                    // ignore
                }
            }
        } finally {
            IOUtils.closeIfNotNull(jarFile);
        }
    }
    
}
