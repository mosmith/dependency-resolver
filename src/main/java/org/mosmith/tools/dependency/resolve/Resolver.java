/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve;

import org.mosmith.tools.dependency.resolve.writer.ConsoleWriter;
import java.io.File;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author mosmith
 */
public class Resolver {
    
    private static final String JAR_SUFFIX=".jar";
    
    private DependencyResolver dependencyResolver=new DependencyResolver();
    
    public void resolve(String classpath) throws ResolveException {
        try{
            String[] paths=classpath.split("\\" + File.pathSeparator);
            for(String path:paths) {
                if(path.indexOf('*')>0) {
                    resolveWildcard(path);
                } else {
                    resolveFile(new File(path));
                }
            }

            List<DependencyEntry> roots=dependencyResolver.resolve();
            File file=new File("dependency.xml");
            // new DependencyXMLWriter().write(file,roots);
            new ConsoleWriter().write(file, roots);
            
            for(DependencyEntry entry: roots) {
                checkCycleDependency(entry);
            }
        } catch (Exception e) {
            throw new ResolveException(e);
        }
    }
    
    private void checkCycleDependency(DependencyEntry dependencyEntry) {
        try{
            Stack<DependencyEntry> visited=new Stack<DependencyEntry>();
            doCheckCycleDependency(visited, dependencyEntry);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void doCheckCycleDependency(Stack<DependencyEntry> visited, DependencyEntry entry) {
        visited.push(entry);

        if (visited.indexOf(entry) != visited.lastIndexOf(entry)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Found cycle dependency: \n");
            for (DependencyEntry cycleChainElement : visited) {
                sb.append(cycleChainElement.getClasspathEntry().getName());
                sb.append("\n");
            }
            throw new RuntimeException(sb.toString());
        }
        for(DependencyEntry dependency: entry.getDependencies()) {
            doCheckCycleDependency(visited, dependency);
        }
        visited.pop();
    }
    
    private void resolveWildcard(String path) {
        int wildcardIndex=path.indexOf('*');
        String prefix=path.substring(0, wildcardIndex);
        String suffix=path.substring(wildcardIndex+1);
        
        prefix=prefix.replace('\\', '/');
        int lastSeparatorIndex=prefix.lastIndexOf('/');
        String dirPath=prefix.substring(0, lastSeparatorIndex);
        
        File dir=new File(dirPath);
        
        for(File file:dir.listFiles()) {
            if(!file.getName().endsWith(suffix)) {
                continue;
            }
            
            if(file.isDirectory()) {
                continue;
            }
            
            if(file.getName().endsWith(JAR_SUFFIX)) {
                resolveJar(file);
            }
        }
    }
    
    private void resolveFile(File file) {
        if(file.isDirectory()) {
            resolveDirectory(file);
        } else if(file.getName().endsWith(JAR_SUFFIX)) {
            resolveJar(file);
        }
    }
    
    private void resolveDirectory(File dir) {
        try{
            DirectoryResolver resolver = new DirectoryResolver(dir);
            resolver.resolve();
            ClasspathEntry classpathEntry=resolver.getClasspathEntry();
            
            dependencyResolver.addClasspathEntry(classpathEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void resolveJar(File jarFile) {
        try{
            JarResolver resolver=new JarResolver(jarFile);
            resolver.resolve();
            ClasspathEntry classpathEntry=resolver.getClasspathEntry();
            
            dependencyResolver.addClasspathEntry(classpathEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
