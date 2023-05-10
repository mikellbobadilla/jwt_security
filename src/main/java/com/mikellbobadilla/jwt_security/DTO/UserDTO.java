package com.mikellbobadilla.jwt_security.DTO;

import com.mikellbobadilla.jwt_security.entities.UserRole;

public record UserDTO(Long id, String username, String name, UserRole role) {
}
