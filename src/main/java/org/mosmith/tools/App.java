package org.mosmith.tools;

import org.mosmith.tools.dependency.resolve.ResolveException;
import org.mosmith.tools.dependency.resolve.Resolver;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws ResolveException
    {
        Resolver resolver=new Resolver();
        // resolver.resolve("/home/mosmith/Software/apache-tomcat-8.0.32/lib/*.jar");
        resolver.resolve("/home/mosmith/programcode/java/spring-jersey-test/target/spring-jersey-test/WEB-INF/lib/*.jar");
    }
}
