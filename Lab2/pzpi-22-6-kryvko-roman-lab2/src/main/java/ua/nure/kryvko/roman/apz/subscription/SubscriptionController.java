package ua.nure.kryvko.roman.apz.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private static SubscriptionResponse toDto(Subscription subscription) {
        return new SubscriptionResponse(
                subscription.getId(),
                subscription.getUser().getId(),
                subscription.getStartDate(),
                subscription.getEndDate(),
                subscription.getStatus()
        );
    }

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody Subscription subscription) {
        try {
            Subscription savedSubscription = subscriptionService.saveSubscription(subscription);
            return new ResponseEntity<>(toDto(savedSubscription), HttpStatus.CREATED);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSubscription(#id, authentication)")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Integer id) {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionById(id);
        if (subscription.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(toDto(subscription.get()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSubscription(#id, authentication)")
    public ResponseEntity<SubscriptionResponse> updateSubscription(@PathVariable Integer id, @RequestBody Subscription subscription) {
        try {
            Subscription updatedSubscription = subscriptionService.updateSubscription(id, subscription);
            return ResponseEntity.ok(toDto(updatedSubscription));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessSubscription(#id, authentication)")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Integer id) {
        try {
            subscriptionService.deleteSubscriptionById(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
