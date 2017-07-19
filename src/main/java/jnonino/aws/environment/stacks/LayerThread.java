package jnonino.aws.environment.stacks;

import com.amazonaws.services.cloudformation.model.StackStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static jnonino.aws.environment.stacks.StackUtils.buildStackName;

/**
 * Created by jnonino on 7/6/17.
 */
class LayerThread extends Thread {

    private static final Logger logger = LogManager.getLogger(LayerThread.class);
    final String environmentName;
    final String stackName;
    final String templatePath;
    final String configFilePath;
    String operation;
    StackStatus inProgress;
    StackStatus complete;
    StackStatus failed;

    LayerThread(String environmentName, String layerName) {
        this.environmentName = environmentName;
        this.stackName = buildStackName(environmentName, layerName);
        this.templatePath = layerName + "/" + layerName + ".json";
        this.configFilePath = layerName + "/" + layerName + ".cfg";
    }

    void waitForStackOperation(String stackName) {
        StackStatus status = StackUtils.getStackStatus(stackName);
        while (status.equals(this.inProgress)) {
            logger.info("Stack " + stackName + " - " + this.operation + " IN PROGRESS");
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            status = StackUtils.getStackStatus(stackName);
        }

        if (status.equals(this.complete)) {
            logger.info("Stack " + stackName + " - " + this.operation + " COMPLETE");
            return;
        }

        if (status.equals(this.failed)) {
            logger.error("Stack " + stackName + " - " + this.operation + " FAILED");
            return;
        }
    }

}
