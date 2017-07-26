package hudson.tasks.junit.pipeline;


import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.junit.JUnitResultArchiver;
import hudson.tasks.junit.TestResultAction;
import org.jenkinsci.plugins.workflow.actions.TagsAction;
import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.SynchronousNonBlockingStepExecution;

import javax.annotation.Nonnull;

public class JUnitResultsStepExecution extends SynchronousNonBlockingStepExecution<Void> {

    private transient final JUnitResultsStep step;

    public JUnitResultsStepExecution(@Nonnull JUnitResultsStep step, StepContext context) {
        super(context);
        this.step = step;
    }

    @Override
    protected Void run() throws Exception {
        FilePath workspace = getContext().get(FilePath.class);
        workspace.mkdirs();
        Run<?,?> run = getContext().get(Run.class);
        TaskListener listener = getContext().get(TaskListener.class);
        Launcher launcher = getContext().get(Launcher.class);
        FlowNode node = getContext().get(FlowNode.class);

        String nodeId = node.getId();

        TestResultAction testResultAction = JUnitResultArchiver.parseAndAttach(step, nodeId, run, workspace, launcher, listener);

        if (testResultAction != null) {
            // TODO: Once JENKINS-43995 lands, update this to set the step status instead of the entire build.
            if (testResultAction.getResult().getFailCount() > 0) {
                getContext().setResult(Result.UNSTABLE);
            }

            TagsAction tagsAction = node.getAction(TagsAction.class);
            if (tagsAction != null) {
                tagsAction.addTag(JUnitResultsStep.HAS_TEST_RESULTS_TAG_NAME, "true");
                node.save();
            } else {
                tagsAction = new TagsAction();
                tagsAction.addTag(JUnitResultsStep.HAS_TEST_RESULTS_TAG_NAME, "true");
                node.addAction(tagsAction);
            }
        }

        return null;
    }

    private static final long serialVersionUID = 1L;
}