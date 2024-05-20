package org.ruananta.parser.config;

public enum Role {
    USER("ROLE_USER"),ADMIN("ROLE_ADMIN");
    private String role;
    private Role(String role) {
        this.role = role;
    }

    @Override
    public final String toString() {
        return role;
    }

    public static Role getRole(String role) {
        for (Role r : Role.values()) {
            if (r.toString().equals(role)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Invalid role: " + role);
    }
}
