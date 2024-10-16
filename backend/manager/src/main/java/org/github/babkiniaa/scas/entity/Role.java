package org.github.babkiniaa.scas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность, представляющая роль пользователя в системе.
 * Каждая роль используется для управления правами доступа пользователей.
 */
public enum Role {
    USER,
    ADMIN
}
