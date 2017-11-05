/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mosmith
 */
public class ClasspathEntry {
    private String name;
    private Set<String> definedClasses=new HashSet<String>();
    private Set<String> refClasses=new HashSet<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void addDefinedClass(String className) {
        this.definedClasses.add(className);
    }

    public Set<String> getDefinedClasses() {
        return Collections.unmodifiableSet(definedClasses);
    }
    
    public void addRefClass(String className) {
        this.refClasses.add(className);
    }
    
    public void addRefClasses(Collection<String> classesNames) {
        this.refClasses.addAll(classesNames);
    }

    public Set<String> getRefClasses() {
        return Collections.unmodifiableSet(refClasses);
    }
}
