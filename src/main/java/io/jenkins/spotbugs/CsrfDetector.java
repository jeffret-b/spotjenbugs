package io.jenkins.spotbugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.bcel.AnnotationDetector;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Method;

public class CsrfDetector extends AnnotationDetector {

    private static final String POST_ANNOTATION = "Lorg/kohsuke/stapler/verb/POST;";
    private static final String REQUIREPOST_ANNOTATION = "Lorg/kohsuke/stapler/interceptor/RequirePOST;";
    private static final String FORMVALIDATION = "Lhudson/util/FormValidation;";

    private final BugReporter bugReporter;

    public CsrfDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visitClassContext(ClassContext classContext) {
        super.visitClassContext(classContext);
    }

    @Override
    public void visitMethod(Method method) {
        if (method.getName().startsWith("doTest") && !(hasAnnotation(method, POST_ANNOTATION) || hasAnnotation(method, REQUIREPOST_ANNOTATION))
            && method.getReturnType().getSignature().equals(FORMVALIDATION)) {
            BugInstance bug = new BugInstance(this, "CSRF", NORMAL_PRIORITY)
                    .addClassAndMethod(this);
            bugReporter.reportBug(bug);
        }
    }

    private boolean hasAnnotation(Method method, String targetAnnotation) {
        AnnotationEntry[] annotationEntries = method.getAnnotationEntries();
        for (AnnotationEntry annotationEntry : annotationEntries) {
            if (annotationEntry.getAnnotationType().equals(targetAnnotation)) {
                return true;
            }
        }
        return false;
    }
}
