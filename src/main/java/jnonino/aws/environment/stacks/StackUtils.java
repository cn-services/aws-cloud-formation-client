package jnonino.aws.environment.stacks;

import com.amazonaws.services.cloudformation.model.*;
import jnonino.aws.client.AWSClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by jnonino on 7/6/17.
 */
class StackUtils {

    private static final Logger logger = LogManager.getLogger(StackUtils.class);

    static String buildStackName(String environmentName, String layerName) {
        return environmentName + "-" + layerName;
    }

    static Stack getStack(String stackName) {
        DescribeStacksRequest request = new DescribeStacksRequest();
        request.setStackName(stackName);
        try {
            DescribeStacksResult result = AWSClientFactory.getCloudFormationClient().describeStacks(request);
            List<Stack> stacks = result.getStacks();
            Stack stack = stacks.get(0);
            return stack;
        } catch (AmazonCloudFormationException e) {
            logger.error("Stack " + stackName + " does not exists");
        }
        return null;
    }

    static StackStatus getStackStatus(String stackName) {
        Stack stack = getStack(stackName);
        if (stack != null) {
            return StackStatus.fromValue(stack.getStackStatus());
        }
        return StackStatus.DELETE_COMPLETE;
    }

    static List<Output> getStackOutputs(String stackName) {
        Stack stack = getStack(stackName);
        if (stack != null) {
            return stack.getOutputs();
        }
        return null;
    }
}
