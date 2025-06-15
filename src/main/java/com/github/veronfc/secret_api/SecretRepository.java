package com.github.veronfc.secret_api;

import org.springframework.data.jpa.repository.JpaRepository;

interface SecretRepository extends JpaRepository<Secret, Long> {

}
