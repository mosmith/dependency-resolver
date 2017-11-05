/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mosmith.tools.dependency.resolve.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.mosmith.tools.dependency.resolve.DependencyEntry;
import org.mosmith.tools.dependency.resolve.util.IOUtils;

/**
 *
 * @author mosmith
 */
public class DependencyXMLWriter {
    private XMLStreamWriter xmlWriter;
    private Map<DependencyEntry, Boolean> written=new HashMap<DependencyEntry, Boolean>();
    
    public void write(File file, List<DependencyEntry> dependencyEntries) throws FileNotFoundException, UnsupportedEncodingException, XMLStreamException, IOException {
        OutputStream os = new FileOutputStream(file);
        
        try{
            OutputStreamWriter writer=new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw=new BufferedWriter(writer);
            this.xmlWriter=XMLOutputFactory.newFactory().createXMLStreamWriter(bw);
            this.xmlWriter=new FormattedXMLStreamWriter(this.xmlWriter);
            doWrite(dependencyEntries);
            xmlWriter.close();
            bw.close();
        } finally {
            IOUtils.closeIfNotNull(os);
        }
    }
    
    private void doWrite(List<DependencyEntry> dependencyEntries) throws XMLStreamException {
        xmlWriter.writeStartDocument("UTF-8", "1.0");
        xmlWriter.writeStartElement("dependencies");
        
        for(DependencyEntry entry: dependencyEntries) {
            writeDependencyEntry(entry);
        }
        
        xmlWriter.writeEndElement();
        xmlWriter.writeEndDocument();
    }
    
    private void writeDependencyEntry(DependencyEntry dependencyEntry) throws XMLStreamException {
        xmlWriter.writeStartElement("dependencyEntry");
        
        writeSimpleElement("name", dependencyEntry.getClasspathEntry().getName());
        if(isWritten(dependencyEntry)==false){
            setWritten(dependencyEntry, true);

            xmlWriter.writeStartElement("dependencies");
            for (DependencyEntry entry : dependencyEntry.getDependencies()) {
                writeDependencyEntry(entry);
            }
            xmlWriter.writeEndElement();
        }
        
        
        xmlWriter.writeEndElement();
    }
    
    private void writeSimpleElement(String localName, String content) throws XMLStreamException {
        xmlWriter.writeStartElement(localName);
        xmlWriter.writeCharacters(content);
        xmlWriter.writeEndElement();
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
