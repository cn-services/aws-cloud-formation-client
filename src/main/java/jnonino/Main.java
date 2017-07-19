package jnonino;

import jnonino.aws.environment.EnvironmentManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jnonino on 7/4/17.
 */
public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length != 3) {
            logger.error("Should run with three arguments");
            logger.error("Uso: java -jar aws-poc.jar <ENVIRONMENT_NAME> <ACTION> <LAYERS_LIST>");
            logger.error("<ENVIRONMENT_NAME>: The name of the environment to work with");
            logger.error("<ACTION>: The action to perform on the environment (create, delete)");
            logger.error("<LAYERS_LIST>: Comma separated list of layers without spaces");
            System.exit(1);
        } else {
            String environmentName = args[0];
            String action = args[1];
            String layersParam = args[2];
            List<String> layers = Arrays.asList(layersParam.split(","));
            new EnvironmentManagement(environmentName, action, layers);
        }

    }
}
