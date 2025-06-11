package ua.nure.kryvko.roman.apz.subscription;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.user.User;
import ua.nure.kryvko.roman.apz.user.UserRepository;

import java.util.Optional;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Subscription saveSubscription(Subscription subscription) {
        User owner = userRepository.findById(subscription.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        subscription.setUser(owner);
        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> getSubscriptionById(Integer id) {
        return subscriptionRepository.findById(id);
    }

    @Transactional
    public void deleteSubscriptionById(Integer id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found for deletion");
        }
        subscriptionRepository.deleteById(id);
    }

    @Transactional
    public Subscription updateSubscription(Integer id, Subscription subscription) {
        subscription.setId(id);
        if (!subscriptionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Subscription not found for deletion");
        }
        User owner = userRepository.findById(subscription.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        subscription.setUser(owner);
        return subscriptionRepository.save(subscription);
    }
}
