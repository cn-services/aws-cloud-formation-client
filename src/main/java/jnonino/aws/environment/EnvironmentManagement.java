package jnonino.aws.environment;

import jnonino.aws.environment.stacks.LayerCreationThread;
import jnonino.aws.environment.stacks.LayerDeleteThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by jnonino on 7/6/17.
 */
public class EnvironmentManagement {

    private static final Logger logger = LogManager.getLogger(EnvironmentManagement.class);
    private final String environmentName;
    private final List<String> layers;

    public EnvironmentManagement(String environmentName, String action, List<String> layers) {
        this.environmentName = environmentName;
        this.layers = layers;

        if (action.equalsIgnoreCase("create")) {
            this.create();
        } else if (action.equalsIgnoreCase("delete")) {
            this.delete();
        } else {
            logger.error("No valid action");
            System.exit(1);
        }
    }

    private void create() {
        logger.info("CREATING ENVIRONMENT " + this.environmentName + " - LAYERS: " + layers);
        for (String layer : this.layers) {
            LayerCreationThread layerCreation = new LayerCreationThread(this.environmentName, layer);
            layerCreation.start();
        }
    }

    private void delete() {
        logger.info("DELETING ENVIRONMENT " + this.environmentName + " - LAYERS: " + layers);
        for (String layer : this.layers) {
            LayerDeleteThread layerDelete = new LayerDeleteThread(this.environmentName, layer);
            layerDelete.start();
        }
    }

}
