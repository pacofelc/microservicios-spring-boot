package com.micro.commons.usuarios.models.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
@Table(name="usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = -7112842892629252017L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_name",unique = true,length=20)
    private String userName;
    @Column(length=60)
    private String password;
    private Boolean enabled;
    private String nombre;
    private String apellidos;
    @Column (unique=true)
    private String email;

    private Integer intentos;

    @ManyToMany(fetch=FetchType.LAZY)
    @JoinTable(name="usuarios_roles",
                joinColumns = @JoinColumn(name="usuario_id"),
                inverseJoinColumns = @JoinColumn(name="rol_id"),
                uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id","rol_id"})}
    )
    // ====================================================
    // EJEMPLO NOMBRADO DE TABLA DE RELACIÓN Y SUS COLUMNAS
    // ====================================================
    // @JoinTable(name="usuarios_roles",
    //        joinColumns = @JoinColumn(name="user_id"),
    //        inverseJoinColumns = @JoinColumn(name="role_id")
    //)
    private List<Rol> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

}
