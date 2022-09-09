/*
 * Copyright (c) Splendid Data Product Development B.V. 2020
 *
 * This program is free software: You may redistribute and/or modify under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at Client's option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, Client should obtain one via www.gnu.org/licenses/.
 */

package com.splendiddata.sqlparser.grammartojava;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

/**
 * Checks all Java source files for jakarta.xml.bind.annotation annotations. If any is found then the class is added to
 * the jaxb.index file of its package.
 *
 * @author Splendid Data Product Development B.V.
 * @since 5.1
 */
@Mojo(name = "createJaxbIndex", executionStrategy = "always", defaultPhase = LifecyclePhase.COMPILE)
@Execute(goal = "createJaxbIndex", phase = LifecyclePhase.COMPILE)
public class JaxbIndexCreator extends AbstractMojo implements FileVisitor<Path> {
    private Log log = getLog();

    /**
     * The maven that will provide the sources
     */
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    /**
     * The directory in which the output file will be generated
     */
    @Parameter(property = "targetDir", defaultValue = "classes")
    private String targetDir;

    /**
     * The directory where the externalised arrays will go
     */
    @Parameter(property = "project.build.directory")
    private String buildDirectory;

    /**
     * Map with for each package the simple names of each class that contains any jakarta.xml.bind.annotation annotation
     */
    private Map<String, Set<String>> jaxbAnnotatedClassesPerPackage = new HashMap<>();

    /**
     * will be reset at the start of each file, and set if the class is added to the
     * {@link #jaxbAnnotatedClassesPerPackage} map
     */
    private boolean classIsAlreadyIn;

    /**
     * @see org.apache.maven.plugin.Mojo#execute()
     * 
     *      Walks all source files to find any jakarta.xml.bind.annotatoin annotations and, if so, add class names to the
     *      package's jaxb.info files.
     *
     * @throws MojoExecutionException
     *             when source files cannot be reached
     */
    @Override
    public void execute() throws MojoExecutionException {
        List<String> compileSourceRoots = project.getCompileSourceRoots();

        /*
         * Walk all source files
         */
        for (String compileSourceRoot : compileSourceRoots) {
            try {
                Files.walkFileTree(Paths.get(compileSourceRoot), this);
            } catch (IOException e) {
                log.error("Error in Files.walkFileTree(Paths.get(compileSourceRoot=" + compileSourceRoot + "), this)",
                        e);
                throw new MojoExecutionException(
                        "Error in Files.walkFileTree(Paths.get(compileSourceRoot=" + compileSourceRoot + "), this)", e);
            }
        }

        /*
         * Create the jaxb.index files
         */
        jaxbAnnotatedClassesPerPackage.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(entry -> {
                    String fileName = new StringBuilder().append(buildDirectory).append(File.separatorChar)
                            .append(targetDir).append(File.separatorChar)
                            .append(entry.getKey().replace('.', File.separatorChar)).append(File.separator)
                            .append("jaxb.index").toString();
                    log.info("generate file " + fileName);
                    try {
                        Files.createDirectories(Paths.get(fileName).getParent());
                        try (PrintWriter jaxbIndex = new PrintWriter(fileName)) {
                            entry.getValue().stream().sorted().forEach(className -> jaxbIndex.println(className));
                        }
                    } catch (IOException e) {
                        log.error(e.toString(), e);
                    }
                });
    }

    /**
     * @see java.nio.file.FileVisitor#visitFile(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     * 
     *      If this is a Java source file, it will be parsed and searched for javax.xmlbind.annotation annotations. If
     *      found, the simple class name will be added to the jaxb.index file eventually.
     *
     * @param file
     *            The file to check
     * @param attrs
     *            Only regular files will be checked
     * @return FileVisitResult.CONTINUE in all cases
     */
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        classIsAlreadyIn = false;
        if (attrs.isRegularFile() && file.getFileName().toString().endsWith(".java")) {
            if (log.isDebugEnabled()) {
                log.debug("JaxbIndexCreator: process " + file);
            }
            try {
                ParseResult<CompilationUnit> parserResult = new JavaParser().parse(file);
                CompilationUnit cu = parserResult.getResult().get();
                processCompilationUnit(cu);
            } catch (ParseProblemException e) {
                if (log.isDebugEnabled()) {
                    log.warn("ParseProblemException in JaxbIndexCreator.visitFile(file=" + file + ", attrs)", e);
                }
            } catch (IOException e) {
                log.error("IOException in JaxbIndexCreator.visitFile(file=" + file + ", attrs)", e);
            }

        }
        return FileVisitResult.CONTINUE;
    }

    /**
     * Searches the CompilationUnit from the JavaParser for javax.xml.bin.annotation annotations
     *
     * @param cu
     *            The CompilationUnit to check
     */
    private void processCompilationUnit(CompilationUnit cu) {
        cu.getChildNodes().forEach(node -> {
            if (node instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cls = (ClassOrInterfaceDeclaration) node;

                cls.getAnnotations().forEach(annotation -> checkAnnotation(cu, cls, annotation));

                if (!classIsAlreadyIn) {
                    cls.getFields().forEach(field -> field.getAnnotations()
                            .forEach(annotation -> checkAnnotation(cu, cls, annotation)));

                    if (!classIsAlreadyIn) {
                        cls.getMethods().forEach(method -> method.getAnnotations()
                                .forEach(annotation -> checkAnnotation(cu, cls, annotation)));
                    }
                }
            } else if (node instanceof EnumDeclaration) {
                EnumDeclaration en = (EnumDeclaration) node;
                en.getAnnotations().forEach(annotation -> checkAnnotation(cu, en, annotation));

                if (!classIsAlreadyIn) {
                    en.getEntries().forEach(
                            entry -> entry.getAnnotations().forEach(annotation -> checkAnnotation(cu, en, annotation)));
                }
            }
        });
    }

    /**
     * Checks if the found annotation is one to consider. If it looks like a javax.xml.bin.annotation annotation, then
     * the simple class name from the cls variable will be added to the jaxb.index file of the package from the package
     * declaration of the compilation unit
     *
     * @param cu
     *            To obtain the package name from if applicable
     * @param cls
     *            To obtain the simple name of the class if applicable
     * @param annotation
     *            The annotation to check.
     */
    private void checkAnnotation(CompilationUnit cu, TypeDeclaration<?> cls, AnnotationExpr annotation) {
        if (classIsAlreadyIn) {
            /*
             * This class has been added earlier to the jaxbAnnotatedClassesPerPackage, so there is no use to try to do
             * that again.
             */
            return;
        }

        /*
         * Is the annotation name one that we want to process?
         */
        switch (annotation.getNameAsString()) {
        case "XmlAttribute":
        case "XmlElement":
        case "XmlElementWrapper":
        case "XmlEnum":
        case "XmlRootElement":
        case "XmlType":
        case "jakarta.xml.bind.annotation.XmlAttribute":
        case "jakarta.xml.bind.annotation.XmlElement":
        case "jakarta.xml.bind.annotation.XmlElementWrapper":
        case "jakarta.xml.bind.annotation.XmlEnum":
        case "jakarta.xml.bind.annotation.XmlRootElement":
        case "jakarta.xml.bind.annotation.XmlType":
            break;
        default:
            if (log.isDebugEnabled()) {
                log.debug("skip annotation " + annotation.getNameAsString() + " in class " + cls.getNameAsString());
            }
            return;
        }

        /*
         * This is an annotation that we are looking for. So add the class to the jaxbAnnotatedClassesPerPackage map so
         * it will end up in a jaxb.index file eventually
         */
        String packageName = "";
        if (cu.getPackageDeclaration().isPresent()) {
            packageName = cu.getPackageDeclaration().get().getNameAsString();
        }
        String className = cls.getNameAsString();
        Set<String> classNamesPerPackage = jaxbAnnotatedClassesPerPackage.get(packageName);
        if (classNamesPerPackage == null) {
            classNamesPerPackage = new HashSet<>();
            jaxbAnnotatedClassesPerPackage.put(packageName, classNamesPerPackage);
        }
        classNamesPerPackage.add(className);
        classIsAlreadyIn = true;
        if (log.isDebugEnabled()) {
            log.debug("add " + className + " to " + packageName + " because of annotation: "
                    + annotation.getNameAsString());
        }
    }

    /**
     * @see java.nio.file.FileVisitor#preVisitDirectory(java.lang.Object, java.nio.file.attribute.BasicFileAttributes)
     *
     * @param dir
     *            not used
     * @param attrs
     *            not used
     * @return FileVisitResult.CONTINUE in all cases
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * @see java.nio.file.FileVisitor#visitFileFailed(java.lang.Object, java.io.IOException)
     *
     * @param file
     *            not used
     * @param exc
     *            not used
     * @return FileVisitResult.CONTINUE in all cases
     */
    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    /**
     * @see java.nio.file.FileVisitor#postVisitDirectory(java.lang.Object, java.io.IOException)
     *
     * @param dir
     *            not used
     * @param exc
     *            not used
     * @return FileVisitResult.CONTINUE in all cases
     */
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
