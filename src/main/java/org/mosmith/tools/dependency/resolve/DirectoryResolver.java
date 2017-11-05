/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import org.mosmith.tools.dependency.resolve.util.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import javassist.ClassPool;
import javassist.CtClass;

/**
 *
 * @author mosmith
 */
public class DirectoryResolver {
    
    private ClasspathEntry classpathEntry=new ClasspathEntry();
    private File dir;
    
    public DirectoryResolver(File dir) {
        this.dir=dir;
    }
    
    public void resolve() throws FileNotFoundException, IOException {
        resolveRecursive(dir);
    }
    
    public ClasspathEntry getClasspathEntry() {
        return this.classpathEntry;
    }
    
    private void resolveRecursive(File dir) throws FileNotFoundException, IOException {
        for(File file: dir.listFiles()) {
            if(file.isDirectory()) {
                resolveRecursive(file);
            }
            
            if(file.getName().endsWith(".class")) {
                resolveClass(file);
            }
        }
    }
    
    private void resolveClass(File file) throws FileNotFoundException, IOException {
        ClassPool classPool=ClassPool.getDefault();
        InputStream is = new FileInputStream(file);
        try{
            CtClass ctClass=classPool.makeClass(is);

            String className=ctClass.getName();
            Collection<String> refClasses=ctClass.getRefClasses();

            ctClass.detach();

            this.classpathEntry.addDefinedClass(className);
            this.classpathEntry.addRefClasses(refClasses);
        } finally {
            IOUtils.closeIfNotNull(is);
        }
    }

}
