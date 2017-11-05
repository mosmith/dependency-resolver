/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve.writer;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mosmith.tools.dependency.resolve.DependencyEntry;

/**
 *
 * @author mosmith
 */
public class ConsoleWriter {
    private int indent=-1;
    private Map<DependencyEntry, Boolean> written=new HashMap<DependencyEntry, Boolean>();
    
    public void write(File file, List<DependencyEntry> dependencyEntries) {
        for(DependencyEntry entry: dependencyEntries) {
            writeDependencyEntry(entry);
        }
    }
    
    private void writeDependencyEntry(DependencyEntry entry) {
        indent++;
        printIndent();
        System.out.print("-- ");

        System.out.println(entry.getClasspathEntry().getName());
        for(DependencyEntry dependency : entry.getDependencies()) {
            if (isWritten(dependency) == false) {
                setWritten(dependency, true);
            }
            if(indent<=5)
                writeDependencyEntry(dependency);
        }
        
        indent--;
    }
    
    private void printIndent() {
        for(int i=0;i<indent;i++) {
            System.out.print("|   ");
        }
    }
    
    private boolean isWritten(DependencyEntry dependencyEntry) {
        Boolean isWritten=written.get(dependencyEntry);
        if(isWritten==null) {
            return false;
        }
        return isWritten;
    }
    
    private void setWritten(DependencyEntry dependencyEntry, Boolean isWritten) {
        written.put(dependencyEntry, isWritten);
    }
}
