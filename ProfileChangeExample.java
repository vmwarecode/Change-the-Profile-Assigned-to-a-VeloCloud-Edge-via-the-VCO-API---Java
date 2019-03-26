import java.util.*;

import net.velocloud.swagger.client.VCApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.model.*;
import net.velocloud.swagger.api.*;

public class ProfileChangeExample {

    private static final VCApiClient client = new VCApiClient();
    private static final AllApi api = new AllApi();

    public static void main(String[] args) {

        // EDIT PARAMS AS NEEDED
        String HOSTNAME = "HOSTNAME";
        String USERNAME = "USERNAME";
        String PASSWORD = "PASSWORD";
        int enterpriseId = 2;
        int edgeId = 1;
        String targetProfileName = "New Profile";

        try {
            connectAndAuthenticate(HOSTNAME, USERNAME, PASSWORD);
        } catch (ApiException e) {
            System.out.println("connectAndAuthenticate error: " + e);
            return;
        }

        EnterpriseGetEnterpriseConfigurationsResultItem targetProfile = null;
        try {
            targetProfile = getProfileByName(enterpriseId, targetProfileName);
        } catch (ApiException e) {
            System.out.println("Error in getProfileByName: " + e);
            return;
        }

        if ( targetProfile == null ) {
            System.out.format("Error: profile [%s] does not exist.\n", targetProfileName);
        }

        try {
            updateEdgeProfile(enterpriseId, edgeId, targetProfile.getId());
        } catch (ApiException e) {
            System.out.println("Error in updateEdgeProfile: " + e);
            return;
        }

        System.out.format("Changed profile for Edge [%d] to [%s]\n", edgeId, targetProfileName);

    }

    public static void connectAndAuthenticate(String hostname, String username, String password) throws ApiException {

        client.setBasePath("https://"+hostname+"/portal/rest");
        api.setApiClient(client);

        AuthObject auth = new AuthObject().username(username).password(password);
        api.loginOperatorLogin(auth);

    }

    public static EnterpriseGetEnterpriseConfigurationsResultItem getProfileByName(int enterpriseId, String name) throws ApiException {

        EnterpriseGetEnterpriseConfigurations req = new EnterpriseGetEnterpriseConfigurations()
            .enterpriseId(enterpriseId);
        List<EnterpriseGetEnterpriseConfigurationsResultItem> profiles = api.enterpriseGetEnterpriseConfigurations(req);

        for ( EnterpriseGetEnterpriseConfigurationsResultItem profile : profiles ) {
            if ( profile.getName().equals(name) ) {
                return profile;
            }
        }

        return null;

    }

    public static void updateEdgeProfile(int enterpriseId, int edgeId, int profileId) throws ApiException {

        EdgeSetEdgeEnterpriseConfiguration req = new EdgeSetEdgeEnterpriseConfiguration()
            .enterpriseId(enterpriseId)
            .edgeId(edgeId)
            .configurationId(profileId);

        EdgeSetEdgeEnterpriseConfigurationResult res = api.edgeSetEdgeEnterpriseConfiguration(req);

    }


}
