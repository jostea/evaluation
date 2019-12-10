package com.internship.evaluation.codetask.compiler;

import com.internship.evaluation.codetask.CompilationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;

@Service
@Slf4j
@RequiredArgsConstructor
public class Compiler {

    private static final String JAVA_FILE_EXT = ".java";
    private static final String COMPILED_JAVA_FILE_EXT = ".class";

    public File compile(File file) throws CompilationException {
        log.info("Compiling file - {}", file.getAbsolutePath());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        JavaCompiler comp = ToolProvider.getSystemJavaCompiler();
        int i = comp.run(null,null, out, file.getAbsolutePath());
        if (i == 0) {
            log.info("Compilation success. File - {}", file.getName());
            return new File(file.getAbsolutePath().replace(JAVA_FILE_EXT, COMPILED_JAVA_FILE_EXT));
        } else {
            log.warn("Compilation failed. File - {}", file.getName());
            throw new CompilationException("Compilation failed! \n" + out.toString().substring(0, 300));
        }
    }
}
