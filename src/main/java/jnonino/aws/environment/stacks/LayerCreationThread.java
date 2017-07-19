package jnonino.aws.environment.stacks;

import com.amazonaws.services.cloudformation.model.*;
import jnonino.aws.client.AWSClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import static jnonino.aws.environment.stacks.StackUtils.*;

/**
 * Created by jnonino on 7/4/17.
 */
public class LayerCreationThread extends LayerThread {

    private static final Logger logger = LogManager.getLogger(LayerCreationThread.class);

    public LayerCreationThread(String environmentName, String layerName) {
        super(environmentName, layerName);
        this.operation = "CREATE";
        this.inProgress = StackStatus.CREATE_IN_PROGRESS;
        this.complete = StackStatus.CREATE_COMPLETE;
        this.failed = StackStatus.CREATE_FAILED;
    }

    public void run() {
        logger.info("STACK " + this.stackName + " CREATION STARTED");
        CreateStackRequest request = new CreateStackRequest();
        try {
            request.setStackName(this.stackName);

            URI templateLocation = this.getClass().getClassLoader().getResource(templatePath).toURI();
            Path path = Paths.get(templateLocation);
            String stringFromFile = Files.lines(path).collect(Collectors.joining());
            request.setTemplateBody(stringFromFile);

            Properties configFileProperties = new Properties();
            configFileProperties.load(this.getClass().getClassLoader().getResourceAsStream(configFilePath));

            if (configFileProperties.containsKey("dependsOn")) {
                String parentLayer = configFileProperties.getProperty("dependsOn");
                Properties parentProperties = getPropertiesFromParent(buildStackName(this.environmentName, parentLayer));
                if (parentProperties != null) {
                    configFileProperties.putAll(parentProperties);
                }
            }

            List<Parameter> parameters = new ArrayList<>();
            for (Map.Entry<Object, Object> entry : configFileProperties.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (key.equals("dependsOn")) {
                    continue;
                }
                if (key.equals("environmentName")) {
                    value = this.environmentName;
                }
                Parameter param = new Parameter();
                param.setParameterKey(key);
                param.setParameterValue(value);
                parameters.add(param);
            }

            request.setParameters(parameters);

            AWSClientFactory.getCloudFormationClient().createStack(request);

            waitForStackOperation(this.stackName);

        } catch (URISyntaxException | IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private Properties getPropertiesFromParent(String parentStackName) {
        Stack stack = getStack(parentStackName);
        while (stack == null) {
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Stack " + this.stackName + " - Waiting for parent (" + parentStackName + ")");
            stack = getStack(parentStackName);
        }

        StackStatus parentStatus = getStackStatus(parentStackName);
        while (parentStatus.equals(this.inProgress)) {
            try {
                sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Stack " + this.stackName + " - Waiting for parent (" + parentStackName + ")");
            parentStatus = getStackStatus(parentStackName);
        }
        if (parentStatus.equals(this.complete)) {
            Properties parentProperties = new Properties();
            List<Output> parentOutputs = getStackOutputs(parentStackName);
            if (parentOutputs != null) {
                for (Output output : parentOutputs) {
                    parentProperties.put(output.getOutputKey(), output.getOutputValue());
                }
            }
            return parentProperties;
        } else {
            return null;
        }
    }
}
