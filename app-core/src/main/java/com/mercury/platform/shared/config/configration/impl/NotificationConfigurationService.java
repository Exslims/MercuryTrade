package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.NotificationDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;

import java.util.ArrayList;
import java.util.List;


public class NotificationConfigurationService extends BaseConfigurationService<NotificationDescriptor> implements PlainConfigurationService<NotificationDescriptor> {
    public NotificationConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public NotificationDescriptor getDefault() {
        NotificationDescriptor notificationDescriptor = new NotificationDescriptor();
        List<ResponseButtonDescriptor> defaultButtons = new ArrayList<>();
        defaultButtons.add(new ResponseButtonDescriptor(0,false,false,"1m","one minute"));
        defaultButtons.add(new ResponseButtonDescriptor(1,true,false,"thx","thanks"));
        defaultButtons.add(new ResponseButtonDescriptor(2,false,false,"no thx", "no thanks"));
        defaultButtons.add(new ResponseButtonDescriptor(3,false,false,"sold", "sold"));
        notificationDescriptor.setButtons(defaultButtons);
        notificationDescriptor.setLimitCount(3);
        notificationDescriptor.setUnfoldCount(2);
        notificationDescriptor.setDismissAfterKick(true);
        notificationDescriptor.setShowLeague(false);
        notificationDescriptor.setFlowDirections(FlowDirections.DOWNWARDS);
        return notificationDescriptor;
    }

    @Override
    public void toDefault() {
        this.selectedProfile.setNotificationDescriptor(this.getDefault());
    }

    @Override
    public void validate() {
        if(this.selectedProfile.getNotificationDescriptor() == null) {
            this.selectedProfile.setNotificationDescriptor(this.getDefault());
        }
    }

    @Override
    public NotificationDescriptor get() {
        return this.selectedProfile.getNotificationDescriptor();
    }
}
