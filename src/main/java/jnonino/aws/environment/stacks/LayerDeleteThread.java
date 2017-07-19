package jnonino.aws.environment.stacks;

import com.amazonaws.services.cloudformation.model.DeleteStackRequest;
import com.amazonaws.services.cloudformation.model.StackStatus;
import jnonino.aws.client.AWSClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jnonino on 7/6/17.
 */
public class LayerDeleteThread extends LayerThread {

    private static final Logger logger = LogManager.getLogger(LayerDeleteThread.class);

    public LayerDeleteThread(String environmentName, String layerName) {
        super(environmentName, layerName);
        this.operation = "DELETE";
        this.inProgress = StackStatus.DELETE_IN_PROGRESS;
        this.complete = StackStatus.DELETE_COMPLETE;
        this.failed = StackStatus.DELETE_FAILED;
    }

    public void run() {
        logger.info("STACK " + this.stackName + " DELETION STARTED");
        DeleteStackRequest request = new DeleteStackRequest();
        request.setStackName(this.stackName);
        AWSClientFactory.getCloudFormationClient().deleteStack(request);
        waitForStackOperation(this.stackName);
    }
}
