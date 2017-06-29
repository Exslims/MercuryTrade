package com.mercury.platform.shared.config;


import com.google.gson.reflect.TypeToken;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;

import java.util.List;

public class ProfilesManager {
    private JSONHelper jsonHelper;

    public ProfilesManager(ConfigurationSource dataSource) {
        this.jsonHelper = new JSONHelper(dataSource);
    }

    public List<ProfileDescriptor> getProfiles(){
        return this.jsonHelper.readArrayData(new TypeToken<List<ProfileDescriptor>>(){});
    }
    public void saveProfiles(List<ProfileDescriptor> profileDescriptors){
        this.jsonHelper.writeListObject(profileDescriptors,new TypeToken<List<ProfileDescriptor>>(){});
    }
}
