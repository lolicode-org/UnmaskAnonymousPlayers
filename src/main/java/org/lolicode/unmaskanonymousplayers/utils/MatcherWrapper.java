package org.lolicode.unmaskanonymousplayers.utils;

import org.lolicode.unmaskanonymousplayers.UnmaskAnonymousPlayers;

import java.util.Optional;

public class MatcherWrapper {
    public static Optional<IpAddressMatcher> getMatcher(String ip) {
        try {
            return Optional.of(new IpAddressMatcher(ip));
        } catch (Exception e) {
            UnmaskAnonymousPlayers.LOGGER.error("Failed to create matcher for ip " + ip, e);
            return Optional.empty();
        }
    }

    public static boolean matches(IpAddressMatcher matcher, String ip) {
        try {
            return matcher.matches(ip);
        } catch (Exception e) {
            UnmaskAnonymousPlayers.LOGGER.error("Failed to match ip " + ip + " with matcher " + matcher, e);
            return false;
        }
    }
}