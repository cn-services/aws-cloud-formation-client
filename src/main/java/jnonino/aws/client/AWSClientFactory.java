package jnonino.aws.client;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.AmazonCloudFormationClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by jnonino on 7/6/17.
 */
public class AWSClientFactory {

    private static final Logger logger = LogManager.getLogger(AWSClientFactory.class);

    public static AWSCredentialsProvider getCredentialsProvider() {
        AWSCredentialsProvider credentials = new ProfileCredentialsProvider();
        return credentials;
    }

    public static AmazonCloudFormation getCloudFormationClient() {
        AmazonCloudFormation cfClient = AmazonCloudFormationClient.builder()
                .withCredentials(getCredentialsProvider())
                .build();
        return cfClient;
    }

}
