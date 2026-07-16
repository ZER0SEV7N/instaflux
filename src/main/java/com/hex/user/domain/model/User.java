//domain/model/User.java
package com.hex.user.domain.model;

//clase de modelo de usuario (Record) que representa la entidad de usuario en el sistema. Contiene los atributos id, username, email y password.
public record User(
    String id, 
    String username, 
    String email, 
    String password,
    String bio
) {
    //Metodo puro de dominio para actualizar la biografía manteniendo la inmutabilidad
    public User updateBio(String newBio) {
        return new User(id, username, email, password, newBio);
    }
}
