/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author mosmith
 */
public class DependencyEntry {
    private ClasspathEntry classpathEntry;
    private Set<DependencyEntry> dependencies=new HashSet<DependencyEntry>();
    private Set<DependencyEntry> dependents=new HashSet<DependencyEntry>();
    
    private boolean visited=false;
    
    public DependencyEntry(ClasspathEntry classpathEntry) {
        this.classpathEntry=classpathEntry;
    }

    public ClasspathEntry getClasspathEntry() {
        return classpathEntry;
    }
    
    public void setVisited(boolean visited) {
        this.visited=visited;
    }
    
    public void addDependency(DependencyEntry dependency) {
        this.dependencies.add(dependency);
    }
    
    public Set<DependencyEntry> getDependencies() {
        return Collections.unmodifiableSet(dependencies);
    }
    
    public void addDependent(DependencyEntry dependent) {
        this.dependents.add(dependent);
    }

    public Set<DependencyEntry> getDependents() {
        return Collections.unmodifiableSet(dependents);
    }
}
