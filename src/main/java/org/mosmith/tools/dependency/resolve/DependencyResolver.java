/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mosmith
 */
public class DependencyResolver {
    private Map<String, ClasspathEntry> classpathEntries=new HashMap<String, ClasspathEntry>();
    
    private Map<String, ClasspathEntry> classNameToEntry=new HashMap<String, ClasspathEntry>();
    
    private Map<ClasspathEntry, DependencyEntry> dependencyEntries=new HashMap<ClasspathEntry, DependencyEntry>();
    
    public DependencyResolver() {
        
    }
    
    public void addClasspathEntry(ClasspathEntry classpathEntry) {
        
        classpathEntries.put(classpathEntry.getName(), classpathEntry);
        for(String className:classpathEntry.getDefinedClasses()) {
            classNameToEntry.put(className, classpathEntry);
        }
        
        DependencyEntry dependencyEntry=new DependencyEntry(classpathEntry);
        dependencyEntries.put(classpathEntry, dependencyEntry);
    }
    
    public List<DependencyEntry> resolve() {
        for(Map.Entry<String, ClasspathEntry> entry: classpathEntries.entrySet()) {
            
            ClasspathEntry classpathEntry=entry.getValue();
            DependencyEntry dependencyEntry=dependencyEntries.get(classpathEntry);
            
            for(String refClass: classpathEntry.getRefClasses()) {
                ClasspathEntry refEntry=classNameToEntry.get(refClass);
                if(refEntry==null) {
                    continue;
                }
                
                if(refEntry==classpathEntry) {
                    continue;
                }
                
                DependencyEntry refDependency=dependencyEntries.get(refEntry);
                if(refDependency==null) {
                    continue;
                }
                
                dependencyEntry.addDependency(refDependency);
                refDependency.addDependent(refDependency);
            }
        }
        
        List<DependencyEntry> roots=new ArrayList<DependencyEntry>();
        for(DependencyEntry dependencyEntry: dependencyEntries.values()) {
            if(dependencyEntry.getDependents().isEmpty()) {
                roots.add(dependencyEntry);
            }
        }
        
        return roots;
    }
}
