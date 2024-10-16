package org.github.babkiniaa.scas.entity;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Сущность, представляющая пользователя в системе.
 * Содержит информацию о пользователе, его ролях и состоянии верификации.
 */
@Table
@Getter
@Setter
@Entity(name = "users")
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String email;

    @Column
    private String login;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column
    private String verificationCode;

    @Column
    private boolean isEnable = false;
}