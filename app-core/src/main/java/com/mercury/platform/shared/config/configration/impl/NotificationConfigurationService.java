package com.mercury.platform.shared.config.configration.impl;

import com.mercury.platform.shared.config.configration.BaseConfigurationService;
import com.mercury.platform.shared.config.configration.PlainConfigurationService;
import com.mercury.platform.shared.config.descriptor.HotKeyDescriptor;
import com.mercury.platform.shared.config.descriptor.NotificationSettingsDescriptor;
import com.mercury.platform.shared.config.descriptor.ProfileDescriptor;
import com.mercury.platform.shared.config.descriptor.ResponseButtonDescriptor;
import com.mercury.platform.shared.entity.message.FlowDirections;

import java.util.ArrayList;
import java.util.List;


public class NotificationConfigurationService extends BaseConfigurationService<NotificationSettingsDescriptor> implements PlainConfigurationService<NotificationSettingsDescriptor> {
    public NotificationConfigurationService(ProfileDescriptor selectedProfile) {
        super(selectedProfile);
    }

    @Override
    public NotificationSettingsDescriptor getDefault() {
        NotificationSettingsDescriptor notificationSettingsDescriptor = new NotificationSettingsDescriptor();
        List<ResponseButtonDescriptor> defaultButtons = new ArrayList<>();
        defaultButtons.add(new ResponseButtonDescriptor(0,false,"1m","one minute", new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(1,true,"thx","thanks",new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(2,false,"no thx", "no thanks",new HotKeyDescriptor()));
        defaultButtons.add(new ResponseButtonDescriptor(3,false,"sold", "sold",new HotKeyDescriptor()));
        List<ResponseButtonDescriptor> defaultOutButtons = new ArrayList<>();
        defaultOutButtons.add(new ResponseButtonDescriptor(0,false,"thanks","thanks", new HotKeyDescriptor()));
        notificationSettingsDescriptor.setButtons(defaultOutButtons);
        return notificationSettingsDescriptor;
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
        if(this.get().getOutButtons().size() == 0){
            this.get().getOutButtons().add(new ResponseButtonDescriptor(0,false,"thanks","thanks", new HotKeyDescriptor()));
        }
    }

    @Override
    public NotificationSettingsDescriptor get() {
        return this.selectedProfile.getNotificationDescriptor();
    }

    @Override
    public void set(NotificationSettingsDescriptor descriptor) {
        this.selectedProfile.setNotificationDescriptor(descriptor);
    }
}
