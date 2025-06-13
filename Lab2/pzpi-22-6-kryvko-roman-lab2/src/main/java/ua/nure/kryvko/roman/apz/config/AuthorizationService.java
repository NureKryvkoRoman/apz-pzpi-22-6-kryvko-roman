package ua.nure.kryvko.roman.apz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ua.nure.kryvko.roman.apz.automationAction.AutomationAction;
import ua.nure.kryvko.roman.apz.automationAction.AutomationActionRepository;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRule;
import ua.nure.kryvko.roman.apz.automationRule.AutomationRuleRepository;
import ua.nure.kryvko.roman.apz.automationRuleDetails.AutomationRuleDetails;
import ua.nure.kryvko.roman.apz.automationRuleDetails.AutomationRuleDetailsRepository;
import ua.nure.kryvko.roman.apz.controller.Controller;
import ua.nure.kryvko.roman.apz.controller.ControllerRepository;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;
import ua.nure.kryvko.roman.apz.notification.Notification;
import ua.nure.kryvko.roman.apz.notification.NotificationRepository;
import ua.nure.kryvko.roman.apz.registration.CustomUserDetails;
import ua.nure.kryvko.roman.apz.sensor.Sensor;
import ua.nure.kryvko.roman.apz.sensor.SensorRepository;
import ua.nure.kryvko.roman.apz.sensorState.SensorState;
import ua.nure.kryvko.roman.apz.sensorState.SensorStateRepository;
import ua.nure.kryvko.roman.apz.subscription.Subscription;
import ua.nure.kryvko.roman.apz.subscription.SubscriptionRepository;
import ua.nure.kryvko.roman.apz.userinfo.UserInfo;
import ua.nure.kryvko.roman.apz.userinfo.UserInfoRepository;

import java.util.Optional;

@Component("authorizationService")
public class AuthorizationService {
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private GreenhouseRepository greenhouseRepository;
    @Autowired
    private SensorStateRepository sensorStateRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private ControllerRepository controllerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private AutomationRuleRepository automationRuleRepository;
    @Autowired
    private AutomationActionRepository automationActionRepository;
    @Autowired
    private AutomationRuleDetailsRepository automationRuleDetailsRepository;

    public boolean canAccessSensor(Integer sensorId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);
        if (optionalSensor.isEmpty()) {
            return false;
        }

        Sensor sensor = optionalSensor.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return sensor.getGreenhouse().getUser().getId().equals(user.getId());
    }

    public boolean canAccessGreenhouse(Integer greenhouseId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<Greenhouse> optionalGreenhouse = greenhouseRepository.findById(greenhouseId);
        if (optionalGreenhouse.isEmpty()) {
            return false;
        }

        Greenhouse greenhouse = optionalGreenhouse.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return greenhouse.getUser().getId().equals(user.getId());
    }

    public boolean canAccessSensorState(Integer stateId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<SensorState> optionalSensorState = sensorStateRepository.findById(stateId);
        if (optionalSensorState.isEmpty()) {
            return false;
        }

        SensorState sensorState = optionalSensorState.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return sensorState.getSensor().getGreenhouse().getUser().getId().equals(user.getId());
    }

    public boolean canAccessUserInfo(Integer userInfoId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<UserInfo> optionalUserInfo = userInfoRepository.findById(userInfoId);
        if (optionalUserInfo.isEmpty()) {
            return false;
        }

        UserInfo userInfo = optionalUserInfo.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return userInfo.getUser().getId().equals(user.getId());
    }

    public boolean canAccessController(Integer controllerId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<Controller> optionalController = controllerRepository.findById(controllerId);
        if (optionalController.isEmpty()) {
            return false;
        }

        Controller controller = optionalController.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return controller.getGreenhouse().getUser().getId().equals(user.getId());
    }

    public boolean canAccessSubscription(Integer subscriptionId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<Subscription> optionalSubscription = subscriptionRepository.findById(subscriptionId);
        if (optionalSubscription.isEmpty()) {
            return false;
        }

        Subscription subscription = optionalSubscription.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return subscription.getUser().getId().equals(user.getId());
    }

    public boolean canAccessNotification(Integer notificationId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if (optionalNotification.isEmpty()) {
            return false;
        }

        Notification notification = optionalNotification.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return notification.getUser().getId().equals(user.getId());
    }

    public boolean canAccessAutomationRule(Integer ruleId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<AutomationRule> optionalAutomationRule = automationRuleRepository.findById(ruleId);
        if (optionalAutomationRule.isEmpty()) {
            return false;
        }

        AutomationRule automationRule = optionalAutomationRule.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return automationRule.getGreenhouse().getUser().getId().equals(user.getId());
    }

    public boolean canAccessAutomationAction(Integer actionId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<AutomationAction> optionalAutomationAction = automationActionRepository.findById(actionId);
        if (optionalAutomationAction.isEmpty()) {
            return false;
        }

        AutomationAction automationAction = optionalAutomationAction.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return automationAction.getAutomationRule().getGreenhouse().getUser().getId().equals(user.getId());
    }

    public boolean canAccessAutomationRuleDetails(Integer detailsId, Authentication auth) {
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        Optional<AutomationRuleDetails> optionalAutomationRuleDetails = automationRuleDetailsRepository.findById(detailsId);
        if (optionalAutomationRuleDetails.isEmpty()) {
            return false;
        }

        AutomationRuleDetails automationAction = optionalAutomationRuleDetails.get();
        CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
        return automationAction.getAutomationRule().getGreenhouse().getUser().getId().equals(user.getId());
    }
}
