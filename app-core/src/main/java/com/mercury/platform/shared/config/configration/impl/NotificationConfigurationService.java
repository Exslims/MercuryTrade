package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
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
        defaultButtons.add(new ResponseButtonDescriptor(0,false,"1m","one minute", new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(1,true,"thx","thanks",new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(2,false,"no thx", "no thanks",new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(3,false,"sold", "sold",new HotKeyDescriptor()));
        notificationDescriptor.setButtons(defaultButtons);
        notificationDescriptor.setNotificationEnable(true);
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
        this.get().getButtons().forEach(it -> {
            if(it.getHotKeyDescriptor() == null) {
                it.setHotKeyDescriptor(new HotKeyDescriptor());
            }
        });
    }

    @Override
    public NotificationDescriptor get() {
        return this.selectedProfile.getNotificationDescriptor();
    }

    @Override
    public void set(NotificationDescriptor descriptor) {
        this.selectedProfile.setNotificationDescriptor(descriptor);
    }
}
